package model.materials;

import java.util.List;
import java.util.Random;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.DistributionFunction;
import model.IntersectionContext;
import model.Ray;
import model.brdfs.OrenNayar;
import model.light.Light;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import model.texture.Texture;

public class CookTorrance extends Material {
	private double reflectionCoefficient;
	private double refractionCoefficient;
	private DistributionFunction specularBrdf;
	private DistributionFunction diffuseBrdf;
	private Random random;
	private Sampler sampler;
	private double roughness;
	private double refractionIndex;
	public CookTorrance(Texture texture, double reflectionCoefficient, double refractionCoefficient, 
			double roughness, double refractionIndex) {
		super(texture);
		this.roughness = roughness;
		this.refractionIndex = refractionIndex;
		this.reflectionCoefficient = reflectionCoefficient;
		this.refractionCoefficient = refractionCoefficient;
		this.specularBrdf = new model.brdfs.CookTorrance(roughness, refractionIndex);
		this.diffuseBrdf = new OrenNayar(roughness);
		this.random = new Random();
		this.sampler = new Multijittered(1);
		sampler.generateSamples();
		sampler.genShuffledIndices();
	}
	
	@Override
	public Color3f directShade(IntersectionContext ic, List<Light> lights,
			List<Body> bodies, int refractionDepth, int reflectionDepth) {
		double r  = random.nextDouble();
		if (r < reflectionCoefficient) {
			//reflect
			if (reflectionDepth < REFLECTIONDEPTH) {
				Ray ray = reflect(ic);
				return computeDerivedColor(ray, ic, bodies, lights, refractionDepth, reflectionDepth);
			}
		} else if (r > reflectionCoefficient+refractionCoefficient) {
			return absorbedDirectColor(ic, lights, bodies);
		} else {
			//refract
			if (refractionDepth < REFRACTIONDEPTH) {
				Ray ray = refract(ic, refractionIndex);
				computeDerivedColor(ray, ic, bodies, lights, refractionDepth, reflectionDepth);
			}
		}
		return new Color3f();
	}
	private Color3f computeDerivedColor(Ray ray, IntersectionContext ic, List<Body> bodies, List<Light> lights, int refractionDepth, int reflectionDepth) {
		Vector3d direction = ray.getDirection();
		Vector3d fixedNormal = new Vector3d(ic.getNormal());
		if (direction.dot(fixedNormal) < 0) //la normal siempre se toma en la direccion del rayo
			fixedNormal.negate();
		Vector3d sample = sampler.sampleHemisphere(roughness);
		Vector3d x = Vectors.cross(new Vector3d(0.00123, 1, 0.00321), direction);
		x.normalize();
		Vector3d sampledDirection = new Vector3d();
		sampledDirection.add(Vectors.scale(direction, sample.z));
		sampledDirection.add(Vectors.scale(x, sample.x));
		sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), sample.y));
		if (sampledDirection.dot(fixedNormal) < 0) { //Reflejar cuando esta debajo de la normal
			sampledDirection.add(Vectors.scale(x, -2*sample.x));
			sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), -2*sample.y));
		}
		sampledDirection.normalize();
		Color3f sampleColor;
		sampleColor = new Ray(sampledDirection, ray.getOrigin()).trace(bodies).directShade(lights, bodies, refractionDepth+1, reflectionDepth+1);
		float pdf = (float) Math.pow(sampledDirection.dot(direction), roughness);
		if (Double.isInfinite(pdf))
			return sampleColor;
		sampleColor.scale(pdf);
		return sampleColor;
	}
	private Color3f absorbedDirectColor(IntersectionContext ic, List<Light> lights, List<Body> bodies) {
		Color3f color = new Color3f();
		Color3f totalSpecularColor = new Color3f();
		Color3f totalDiffuseColor = new Color3f();
		
		Color3f localColor = getColor(ic.getU(), ic.getV());
		for (Light light : lights) {
			Point3d p = ic.getIntersectionPoint();
			if (light.isVisible(p, bodies)>0) {
				Vector3d n = ic.getNormal();
				Vector3d v = ic.getRay().getDirection();
				Color3f diffuseColor = diffuseBrdf.apply(light.getDirectionFromTo(p), n, v);
				Color3f lightColor = new Color3f(light.getColor());
				//Diffuse
				if (diffuseColor.x < 0) {
					System.out.println("Diffuse color out of range. Value: " + diffuseColor.x+ "\n" + ic);
					diffuseColor.absolute();
				}
				diffuseColor.scale((float)(light.getIntensity(p)));
				diffuseColor.x *= lightColor.x * localColor.x;
				diffuseColor.y *= lightColor.y * localColor.y;
				diffuseColor.z *= lightColor.z * localColor.z;
				totalDiffuseColor.add(diffuseColor);
				// Specular
				Color3f brdfC = specularBrdf.apply(light.getDirectionFromTo(p), n, v);
				System.out.println(brdfC.x);
				brdfC.x *= lightColor.x * localColor.x;
				brdfC.y *= lightColor.y * localColor.y;
				brdfC.z *= lightColor.z * localColor.z;
				totalSpecularColor.add(brdfC);
			}
			totalSpecularColor.scale((float) reflectionCoefficient);
			totalDiffuseColor.scale((float) (1-reflectionCoefficient-refractionCoefficient));
			color.add(totalDiffuseColor);
			color.add(totalSpecularColor);
		}
		return color;
	}
	@Override
	public Color3f indirectShade(IntersectionContext ic, List<Light> lights,
			List<Body> bodies, int refractionDepth, int reflectionDepth) {
		return absorbedIndirectColor(ic, lights, bodies, refractionDepth, reflectionDepth);
	}

	private Color3f absorbedIndirectColor(IntersectionContext ic,
			List<Light> lights, List<Body> bodies, int refractionDepth, int reflectionDepth) {
		
		Color3f bodyColor = getColor(ic.getU(), ic.getV());
		Vector3d direction = ic.getRay().getDirection();
		Vector3d l = Vectors.negate(ic.getRay().getDirection());
		
		Vector3d fixedNormal = new Vector3d(ic.getNormal());
		if (direction.dot(fixedNormal) < 0) //la normal siempre se toma en la direccion del rayo
			fixedNormal.negate();
		Vector3d sample = sampler.sampleHemisphere(roughness);
		Vector3d x = Vectors.cross(new Vector3d(0.00123, 1, 0.00321), direction);
		x.normalize();
		Vector3d sampledDirection = new Vector3d();
		sampledDirection.add(Vectors.scale(direction, sample.z));
		sampledDirection.add(Vectors.scale(x, sample.x));
		sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), sample.y));
		if (sampledDirection.dot(fixedNormal) < 0) { //Reflejar cuando esta debajo del plano normal
			sampledDirection.add(Vectors.scale(x, -2*sample.x));
			sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), -2*sample.y));
		}
		sampledDirection.normalize();
		Color3f sampleColor;
		sampleColor = new Ray(sampledDirection, ic.getRay().getOrigin()).trace(bodies).directShade(lights, bodies, refractionDepth+1, reflectionDepth+1);
		float pdf = (float) Math.pow(sampledDirection.dot(direction), roughness);
		if (Double.isInfinite(pdf))
			return sampleColor;
		sampleColor.scale(pdf);
		Vector3d n = ic.getNormal();
		Vector3d v = ic.getRay().getDirection();
		Color3f diffuseColor = diffuseBrdf.apply(l, n, v);
		Color3f lightColor = new Color3f(sampleColor);
		//Diffuse
		if (diffuseColor.x < 0) {
			System.out.println("Diffuse color out of range. Value: " + diffuseColor.x+ "\n" + ic);
			diffuseColor.absolute();
		}
		diffuseColor.x *= lightColor.x * bodyColor.x;
		diffuseColor.y *= lightColor.y * bodyColor.y;
		diffuseColor.z *= lightColor.z * bodyColor.z;
		// Specular
		Color3f brdfC = specularBrdf.apply(l, n, v);
		System.out.println(brdfC.x);
		brdfC.x *= lightColor.x * bodyColor.x;
		brdfC.y *= lightColor.y * bodyColor.y;
		brdfC.z *= lightColor.z * bodyColor.z;
		
		return sampleColor;
	}
}
