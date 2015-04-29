package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.light.Light;
import model.texture.Texture;

public class Mirror extends Material {
	public Mirror() {
		super(null);
	}
	public Mirror(Texture texture) {
		super(texture);
	}
	public Mirror(Texture texture, double opacity) {
		super(texture);
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Color3f rfColor;
		if (reflectionDepth < REFLECTIONDEPTH)
			rfColor = reflect(ic).trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth+1);
		else
			rfColor = new Color3f();
		return rfColor;
	}
}