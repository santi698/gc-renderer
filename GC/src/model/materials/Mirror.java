package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.light.Light;
import model.texture.PlainTexture;
import model.texture.Texture;

public class Mirror extends Phong {
	public Mirror() {
		super(new PlainTexture(new Color3f(1,1,1)), 2000, 0.5, 0);
	}
	public Mirror(Texture texture) {
		super(texture, 2000, 0, 1, 0);
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Color3f rfColor;
		if (reflectionDepth < REFLECTIONDEPTH)
			rfColor = reflect(ic).trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth+1);
		else
			rfColor = new Color3f();
		Color3f mirrorColor = getColor(ic.getU(), ic.getV());
		rfColor.x = rfColor.x*mirrorColor.x;
		rfColor.y = rfColor.y*mirrorColor.y;
		rfColor.z = rfColor.z*mirrorColor.z;
		Color3f lightReflectColor = super.shade(ic, lights, bodies, refractionDepth, reflectionDepth);
		rfColor.add(lightReflectColor);
		return rfColor;
	}
}