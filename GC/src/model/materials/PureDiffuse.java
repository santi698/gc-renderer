package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.DistributionFunction;
import model.IntersectionContext;
import model.light.Light;
import model.texture.Texture;

public abstract class PureDiffuse extends Material {
	private double ka =0.2;
	private DistributionFunction brdf;
	public PureDiffuse(Texture texture, DistributionFunction brdf) {
		super(texture);
		this.brdf = brdf;
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Point3d p = ic.getIntersectionPoint();
		Vector3d v = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d n = ic.getNormal();

		Color3f color = new Color3f(getColor(ic.getU(), ic.getV()));
		Color3f totalDiffuseColor = new Color3f();

		for (Light light: lights) {
			double visibility = light.isVisible(p, bodies);
			if (visibility > 0) {
				Color3f diffuseColor = brdf.apply(light.getDirectionFromTo(p), n, v);
				if (diffuseColor.x < 0) {
					System.out.println("Diffuse color out of range. Value: " + diffuseColor.x+ "\n" + ic);
					diffuseColor.absolute();
				}
				diffuseColor.scale((float)(visibility * light.getIntensity(p)));
				Color3f lightColor = new Color3f(light.getColor());
				diffuseColor.x *= lightColor.x;
				diffuseColor.y *= lightColor.y;
				diffuseColor.z *= lightColor.z;
				totalDiffuseColor.add(diffuseColor);
			}
		}
		color.x = (float)(color.x * totalDiffuseColor.x + color.x * ka);
		color.y = (float)(color.y * totalDiffuseColor.y + color.y * ka);
		color.z = (float)(color.z * totalDiffuseColor.z + color.z * ka);
		return color;
	}
}
