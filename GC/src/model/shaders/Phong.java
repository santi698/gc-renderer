package model.shaders;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.Shader;
import model.light.Light;

public class Phong implements Shader {
	private double alpha, ka = 0.2, ks=0.5, kd=1, is, id;
	public Phong(double alpha) {
		this.alpha = alpha;
	}
	@Override
	public Color3f shade(Point3d p, Vector3d n, Vector3d v, Light[] lights, Body[] bodies, Color3f baseColor) {
		Color3f color = new Color3f(baseColor);
		Color3f totalLightColor = new Color3f();
		for (Light light : lights) {
			if (light.isVisible(p, bodies) > 0) {
				Vector3d l = light.getDirectionFromTo(p);
				id = is = light.getIntensity(p)/2;
				Vector3d r = new Vector3d(n);
				r.scale(2*l.dot(n));
				r.sub(l);
				Color3f lightColor = new Color3f(light.getColor());
				lightColor.x = (float)((kd * l.dot(n) * lightColor.x*id) + ks*Math.pow(v.dot(r), alpha)*lightColor.x*is);
				lightColor.y = (float)((kd * l.dot(n) * lightColor.y*id) + ks*Math.pow(v.dot(r), alpha)*lightColor.y*is);
				lightColor.z = (float)((kd * l.dot(n) * lightColor.z*id) + ks*Math.pow(v.dot(r), alpha)*lightColor.z*is);
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
