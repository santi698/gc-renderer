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
import model.texture.Texture;

public class Phong extends Material {
	private double ka, ks, kd, is, id;
	private PerfectDiffuse lambert;
	private PerfectSpecular phong;
	public Phong(Texture bodyTexture, double alpha, double ks, double kd) {
		super(bodyTexture);
		this.lambert = new PerfectDiffuse();
		this.phong = new PerfectSpecular(alpha);
		this.ka = 0.2;
		this.ks = ks;
		this.kd = kd;
	}
	public void setKs(double ks) {
		this.ks = ks;
	}
	@Override
	public Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		Point3d p = ic.getIntersectionPoint();
		Vector3d v = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d n = ic.getNormal();

		Color3f color = new Color3f(getColor(ic.getU(), ic.getV()));
		Color3f totalDiffuseColor = new Color3f();
		Color3f totalSpecularColor = new Color3f();
		
		for (Light light : lights) {
			if (light.isVisible(p, bodies) > 0) {
				Vector3d l = light.getDirectionFromTo(p);
				id = is = light.getIntensity(p)*0.5;
				
				Vector3d r = new Vector3d(n);
				r.scale(2*l.dot(n));
				r.sub(l);
				Color3f diffuseColor = new Color3f(light.getColor());
				diffuseColor.x = (float)((kd * lambert.apply(l, n, v) * diffuseColor.x * id));
				diffuseColor.y = (float)((kd * lambert.apply(l, n, v) * diffuseColor.y * id));
				diffuseColor.z = (float)((kd * lambert.apply(l, n, v) * diffuseColor.z * id));
				Color3f specularColor = new Color3f(light.getColor());
				specularColor.x = (float) (ks * phong.apply(l, n, v) * specularColor.x * is);
				specularColor.y = (float) (ks * phong.apply(l, n, v) * specularColor.y * is);
				specularColor.z = (float) (ks * phong.apply(l, n, v) * specularColor.z * is);
				totalDiffuseColor.add(diffuseColor);
				totalSpecularColor.add(specularColor);
			}
		}
		color.x = (float)(color.x * totalDiffuseColor.x + color.x * ka + totalSpecularColor.x);
		color.y = (float)(color.y * totalDiffuseColor.y + color.y * ka + totalSpecularColor.y);
		color.z = (float)(color.z * totalDiffuseColor.z + color.z * ka + totalSpecularColor.z);
		color.clamp(0, 1);
		
		return color;
	}
}
