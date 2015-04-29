package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.light.Light;

public class Transparent extends Material {
	private double refractionIndex;
	private double kR;
	public Transparent(Color3f color, double refractionIndex) {
		super(color);
		this.refractionIndex = refractionIndex;
		this.kR = 0.2;
	}
	public Transparent(Color3f color, double refractionIndex, double kR) {
		this(color, refractionIndex);
		this.kR = kR;
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
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Color3f rrColor;
		Color3f rfColor;
		Ray refracted = refract(ic, refractionIndex);
		Ray reflected = reflect(ic);
		if (tir(ic, refractionIndex)) {
			if (reflectionDepth < REFLECTIONDEPTH)
				return reflected.trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth+1);
			else
				return new Color3f(1,0,1);
		}
		if (refractionDepth < REFRACTIONDEPTH)
			rrColor = refracted.trace(bodies).shade(lights, bodies, refractionDepth+1, reflectionDepth);
		else
			rrColor = new Color3f(1,0,1);
		if (reflectionDepth < REFLECTIONDEPTH)
			rfColor = reflected.trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth+1);
		else
			rfColor = new Color3f(1,0,1);
		kR = calculateReflectionCoefficient(refracted, reflected, ic);
		rrColor.scale((float)(1-kR));
		rfColor.scale((float)kR);
		rrColor.add(rfColor);
		return rrColor;
	}
}
