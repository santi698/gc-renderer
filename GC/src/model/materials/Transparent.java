package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.light.Light;

public class Transparent extends Material {
	private double n;
	private double kR;
	public Transparent(Color3f color, double refractionIndex) {
		super(color);
		this.n = refractionIndex;
		this.kR = 0;
	}
	public Transparent(Color3f color, double refractionIndex, double kR) {
		this(color, refractionIndex);
		this.kR = kR;
	}
	
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Color3f rrColor;
		Color3f rfColor;
		if (refractionDepth < REFRACTIONDEPTH)
			rrColor = refract(ic, n).trace(bodies).shade(lights, bodies, refractionDepth+1, reflectionDepth);
		else
			rrColor = new Color3f();
		if (this.kR == 0)
			return rrColor;
		else {
			if (reflectionDepth < REFLECTIONDEPTH)
				rfColor = reflect(ic).trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth+1);
			else
				rfColor = new Color3f();
			rrColor.scale((float)(1-kR));
			rfColor.scale((float)kR);
			rrColor.add(rfColor);
			return rrColor;
		}
	}

}
