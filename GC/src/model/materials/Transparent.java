package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.light.Light;
import model.texture.Texture;

public class Transparent extends Phong {
	private double refractionIndex;
	private float kR;
	private float kT;
	private Texture cFIn = super.getTexture();
	private Color3f cFOut = new Color3f(1,1,1);
	public Transparent(Texture texture, double refractionIndex) {
		this(texture, refractionIndex, 1, 1);
	}
	public Transparent(Texture texture, double refractionIndex, float kR, float kT) {
		super(texture, 200000, 0, 1, 0);
		this.refractionIndex = refractionIndex;
		this.kR = kR;
		this.kT = kT;
		System.out.println(kR);
	}
	public double calculateReflectionCoefficient(Ray refracted, Ray reflected, IntersectionContext ic) {
		double n1,n2;
		double costt = Math.abs(refracted.getDirection().dot(ic.getNormal()));
		double costi = Math.abs(ic.getRay().getDirection().dot(ic.getNormal()));
		if (ic.getRay().getDirection().dot(ic.getNormal()) < 0) {
			n1 = 1;
			n2 = refractionIndex;
		} else {
			n1 = refractionIndex;
			n2 = 1;
		}
		double r0 = (n1*costi-n2*costt)/(n1*costi+n2*costt);
		return r0*r0;
	}
	public Color3f filterColor(Color3f targetColor, IntersectionContext ic, boolean in) {
		targetColor = new Color3f(targetColor);
		Color3f filterColor;
		if (in) {
			filterColor = cFIn.get(ic.getU(), ic.getV());
		} else {
			filterColor = cFOut;
		}
		targetColor.x = (float)Math.pow(filterColor.x, ic.getT())*targetColor.x;
		targetColor.y = (float)Math.pow(filterColor.y, ic.getT())*targetColor.y;
		targetColor.z = (float)Math.pow(filterColor.z, ic.getT())*targetColor.z;
		return targetColor;
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Color3f rrColor;
		Color3f rfColor;
		Color3f phongColor = super.shade(ic, lights, bodies, refractionDepth, reflectionDepth);
		Ray refracted = refract(ic, refractionIndex);
		Ray reflected = reflect(ic);
		double costi = -ic.getRay().getDirection().dot(ic.getNormal());
		boolean in = costi < 0;
		if (tir(ic, refractionIndex)) {
			if (reflectionDepth < REFLECTIONDEPTH) {
				IntersectionContext reflectedIc = reflected.trace(bodies);
				Color3f reflectedColor = reflectedIc.shade(lights, bodies, refractionDepth, reflectionDepth+1);
				Color3f result = filterColor(reflectedColor, reflectedIc, in);
				result.add(phongColor);
				return result;
			}
			else
				return new Color3f(1,0,1);
		}
		if (refractionDepth < REFRACTIONDEPTH) {
			IntersectionContext refractedIc = refracted.trace(bodies);
			rrColor = refractedIc.shade(lights, bodies, refractionDepth+1, reflectionDepth);
			rrColor = filterColor(rrColor, refractedIc, !in);
		}
		else
			rrColor = new Color3f(1,0,1);
		if (reflectionDepth < REFLECTIONDEPTH) {
			IntersectionContext reflectedIc = reflected.trace(bodies);
			rfColor = reflectedIc.shade(lights, bodies, refractionDepth, reflectionDepth+1);
			rfColor = filterColor(rfColor, reflectedIc, in);
		}
		else
			rfColor = new Color3f(1,0,1);
		float rC = (float)calculateReflectionCoefficient(refracted, reflected, ic);
		float localKr = kR*rC;
		float localKt = kT*(1-rC);
		rrColor.scale(localKt);
		rfColor.scale(localKr);
		phongColor.scale(localKr);
		rrColor.add(rfColor);
		rrColor.add(phongColor);
		return rrColor;
	}
}
