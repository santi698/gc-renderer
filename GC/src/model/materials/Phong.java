package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.IntersectionContext;
import model.brdfs.PerfectDiffuse;
import model.brdfs.PerfectSpecular;
import model.light.Light;

public class Phong extends Material {
	private double ka, ks, kd, is, id;
	private PerfectDiffuse lambert;
	private PerfectSpecular phong;
	public Phong(Color3f bodyColor, double alpha, double ks, double kd) {
		super(bodyColor);
		this.lambert = new PerfectDiffuse();
		this.phong = new PerfectSpecular(alpha);
		this.ka = 0.2;
		this.ks = ks;
		this.kd = kd;
	}
	
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Point3d p = ic.getIntersectionPoint();
		Vector3d v = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d n = ic.getNormal();

		Color3f color = new Color3f(getColor());
		Color3f totalLightColor = new Color3f();
		
		for (Light light : lights) {
			if (light.isVisible(p, bodies) > 0) {
				Vector3d l = light.getDirectionFromTo(p);
				id = is = light.getIntensity(p)*0.5;
				
				Vector3d r = new Vector3d(n);
				r.scale(2*l.dot(n));
				r.sub(l);
				Color3f lightColor = new Color3f(light.getColor());
				lightColor.x = (float)((kd * lambert.apply(l, n, v) * lightColor.x * id) + ks * phong.apply(l, n, v) * lightColor.x * is);
				lightColor.y = (float)((kd * lambert.apply(l, n, v) * lightColor.y * id) + ks * phong.apply(l, n, v) * lightColor.y * is);
				lightColor.z = (float)((kd * lambert.apply(l, n, v) * lightColor.z * id) + ks * phong.apply(l, n, v) * lightColor.z * is);				
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
