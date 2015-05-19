package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.Body;
import model.DistributionFunction;
import model.IntersectionContext;
import model.Ray;
import model.light.Light;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import model.shapes.Shape;
import model.texture.Texture;
import util.Vectors;

public class GeneralRealistic extends Phong{
	private double refractionIndex = 1.52;
	private DistributionFunction brdf;
	private DistributionFunction btdf;
	private double reflectionExponent;
	private double refractionExponent;
	private Sampler sampler;
	private float reflectionCoefficient;
	private int numSamples;
	private float absorptionCoefficient;
	public GeneralRealistic(Texture colorTexture, double reflectionExponent, double refractionExponent, int numSamples, float reflectionCoefficient, double absorptionCoefficient) {
		super(colorTexture, reflectionExponent, 0, reflectionCoefficient, absorptionCoefficient);
		sampler = new Multijittered(numSamples);
		sampler.generateSamples();
		sampler.genShuffledIndices();
		this.reflectionCoefficient = reflectionCoefficient;
		this.absorptionCoefficient = (float) absorptionCoefficient;
		this.reflectionExponent = reflectionExponent;
		this.refractionExponent = refractionExponent;
		this.numSamples = numSamples;
	}
@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies,
			int refractionDepth, int reflectionDepth) {
	    Color3f color = new Color3f();
	    Color3f reflectedColor;
	    Color3f refractedColor; 
	    if (reflectionDepth > REFLECTIONDEPTH) {
	    	reflectedColor = new Color3f();
	    } else {
		Ray reflected = reflect(ic);
		reflectedColor = calculateColor(reflected, ic.getNormal(), brdf, reflectionExponent, lights, bodies, refractionDepth, reflectionDepth+1);
	    }
		if (reflectionCoefficient - 1 < Shape.EPS && !tir(ic, refractionIndex)) {
			if (refractionDepth > REFRACTIONDEPTH) {
				refractedColor = new Color3f();
			} else {
				Ray refracted = refract(ic, refractionIndex);
				refractedColor = calculateColor(refracted, ic.getNormal(), btdf, refractionExponent, lights, bodies, refractionDepth+1, reflectionDepth);
				refractedColor.scale(1-reflectionCoefficient);
				refractedColor.scale(1-absorptionCoefficient);
			}
			color.add(refractedColor);
			reflectedColor.scale(reflectionCoefficient);
		}
		reflectedColor.scale(1-absorptionCoefficient);
		color.add(reflectedColor);
		Color3f absorptedColor = super.shade(ic, lights, bodies, refractionDepth, reflectionDepth);
		absorptedColor.scale(absorptionCoefficient);
		color.add(absorptedColor);
		return color;
	}
	private Color3f calculateColor(Ray mainRay, Vector3d normal, DistributionFunction bxdf, double exponent, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		if (Double.isInfinite(exponent)) {
			return mainRay.trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth);
		}
		Vector3d direction = mainRay.getDirection();
		Vector3d fixedNormal = new Vector3d(normal);
		if (direction.dot(fixedNormal) < 0) //la normal siempre se toma en la direccion del rayo
			fixedNormal.negate();
		Color3f color = new Color3f();
		Vector3d[] samples = sampler.sampleHemisphereSet(exponent);
		Vector3d x = Vectors.cross(new Vector3d(0.00123, 1, 0.00321), direction);
		x.normalize();
		for (Vector3d v : samples) {
			Vector3d sampledDirection = new Vector3d();
			sampledDirection.add(Vectors.scale(direction, v.z));
			sampledDirection.add(Vectors.scale(x, v.x));
			sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), v.y));
			if (sampledDirection.dot(fixedNormal) < 0) { //Reflejar cuando esta debajo de la normal
				sampledDirection.add(Vectors.scale(x, -2*v.x));
				sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), -2*v.y));
			}
			sampledDirection.normalize();
			Color3f sampleColor;
			sampleColor = new Ray(sampledDirection, mainRay.getOrigin()).trace(bodies).shade(lights, bodies, refractionDepth+1, reflectionDepth+1);
			float pdf = (float) Math.pow(sampledDirection.dot(direction), exponent);
			if (Double.isInfinite(pdf))
				return sampleColor;
			sampleColor.scale(pdf);
			color.add(sampleColor);
		}
		color.scale(1f/numSamples);;
		return color;
	}
}

