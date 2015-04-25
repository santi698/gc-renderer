package model.shaders;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.Shader;
import model.light.Light;

public class Lambert implements Shader {
	double ka =0.2, kd=1;
	@Override
	public Color3f shade(Point3d p, Vector3d n, Vector3d v, Light[] lights,	Body[] bodies, Color3f baseColor) {
		
		Color3f color = new Color3f(baseColor);
		Color3f totalLightColor = new Color3f();

		float intensity = 0;
		for (Light light: lights) {
			double visibility = light.isVisible(p, bodies);
			if (visibility > 0) {
				intensity = (float) ((kd * light.getDirectionFromTo(p).dot(n)) * visibility * light.getIntensity(p));
				Color3f lightColor = new Color3f(light.getColor());
				lightColor.scale(intensity);
				totalLightColor.add(lightColor);
			}
		}
		color.x *= totalLightColor.x + ka;
		color.y *= totalLightColor.y + ka;
		color.z *= totalLightColor.z + ka;
		color.clamp(0, 1);
		return color;
	}
}
