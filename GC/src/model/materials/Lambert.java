package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.IntersectionContext;
import model.brdfs.PerfectDiffuse;
import model.light.Light;

public class Lambert extends Material {
	private double ka =0.2, kd=1;
	private PerfectDiffuse lambert;
	public Lambert(Color3f bodyColor) {
		super(bodyColor);
		this.lambert = new PerfectDiffuse();
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Point3d p = ic.getIntersectionPoint();
		Vector3d v = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d n = ic.getNormal();

		Color3f color = new Color3f(getColor());
		Color3f totalLightColor = new Color3f();

		float intensity = 0;
		for (Light light: lights) {
			double visibility = light.isVisible(p, bodies);
			if (visibility > 0) {
				intensity = (float) ((kd * lambert.apply(light.getDirectionFromTo(p), n, v)) * visibility * light.getIntensity(p));
				Color3f lightColor = new Color3f(light.getColor());
				lightColor.scale(intensity);
				totalLightColor.add(lightColor);
			}
		}
		color.x = (float)(color.x * totalLightColor.x + color.x * ka);
		color.y = (float)(color.y * totalLightColor.y + color.y * ka);
		color.z = (float)(color.z * totalLightColor.z + color.z * ka);
		color.clamp(0, 1);
		return color;
	}
}
