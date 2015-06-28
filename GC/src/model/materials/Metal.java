package model.materials;

import java.util.List;

import javax.vecmath.Color3f;

import model.Body;
import model.IntersectionContext;
import model.light.Light;
import model.texture.Texture;

public class Metal extends Phong {
	private double reflectivity;
	public Metal(Texture texture) {
		this(texture, 0.4);
	}
	public Metal(Texture texture, double roughness) {
		super(texture, roughness,0.5, 0.75/Math.PI);
		reflectivity = 1/(1 + 100*(roughness));
	}
	@Override
	public Color3f directShade(IntersectionContext ic, List<Light> lights, List<Body> bodies,
			int refractionDepth, int reflectionDepth) {
		Color3f color = new Color3f();
		Color3f phongColor = super.directShade(ic, lights, bodies, refractionDepth, reflectionDepth);
		phongColor.scale((float)(1-reflectivity));
		Color3f reflectionColor;
		if (reflectionDepth <= REFLECTIONDEPTH)
			reflectionColor = reflect(ic).trace(bodies).directShade(lights, bodies, refractionDepth, reflectionDepth+1);
		else
			reflectionColor = new Color3f();
		reflectionColor.scale((float)reflectivity);
		color.add(phongColor);
		color.add(reflectionColor);
		return color;
	}
}
