package model.materials;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.light.Light;
import model.texture.Texture;

public class PolishedMarble extends Phong {
	public PolishedMarble(Texture bodyTexture) {
		super(bodyTexture, 300, 0.5, 0.75/Math.PI);
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies,
			int refractionDepth, int reflectionDepth) {
		Color3f phongColor = super.shade(ic, lights, bodies, refractionDepth, reflectionDepth);
		Color3f rlColor;
		if (reflectionDepth > REFLECTIONDEPTH)
			rlColor = new Color3f();
		else
			rlColor = reflect(ic).trace(bodies).shade(lights, bodies, refractionDepth, reflectionDepth + 1);
		phongColor.scale(0.75f);
		rlColor.scale(0.25f);
		phongColor.add(rlColor);
		return phongColor;
	}

}
