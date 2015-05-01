package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.IntersectionContext;
import model.brdfs.PerfectDiffuse;
import model.light.Light;
import model.texture.Texture;

public class Lambert extends Material {
	private double ka =0.2, kd=1;
	private PerfectDiffuse lambert;
	public Lambert(Texture texture) {
		super(texture);
		this.lambert = new PerfectDiffuse();
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
				Color3f diffuseColor = lambert.apply(light.getDirectionFromTo(p), n, v);
				diffuseColor.scale((float)(kd * visibility * light.getIntensity(p)));
				Color3f lightColor = new Color3f(light.getColor());
				diffuseColor.x *= lightColor.x;
				diffuseColor.y *= lightColor.y;
				diffuseColor.z *= lightColor.z;
				totalDiffuseColor.add(totalDiffuseColor);
			}
		}
		color.x = (float)(color.x * totalDiffuseColor.x + color.x * ka);
		color.y = (float)(color.y * totalDiffuseColor.y + color.y * ka);
		color.z = (float)(color.z * totalDiffuseColor.z + color.z * ka);
		return color;
	}
}
