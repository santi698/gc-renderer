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
	public Phong(Texture bodyTexture, double alpha, double ka, double ks, double kd) {
		super(bodyTexture);
		this.lambert = new PerfectDiffuse();
		this.phong = new PerfectSpecular(alpha);
		this.ka = ka;
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
				
				Color3f lightColor = light.getColor();
				
				Vector3d l = light.getDirectionFromTo(p);
				id = is = light.getIntensity(p)*0.5;
				
				Vector3d r = new Vector3d(n);
				r.scale(2*l.dot(n));
				r.sub(l);
				
				Color3f diffuseColor = new Color3f(lambert.apply(l, n, v));
				diffuseColor.scale((float)(kd*id));
				diffuseColor.x *= lightColor.x;
				diffuseColor.y *= lightColor.y;
				diffuseColor.z *= lightColor.z;
				
				Color3f specularColor = new Color3f(phong.apply(l, n, v));
				specularColor.scale((float)(ks*is));
				specularColor.x *= lightColor.x;
				specularColor.y *= lightColor.y;
				specularColor.z *= lightColor.z;
				
				totalDiffuseColor.add(diffuseColor);
				totalSpecularColor.add(specularColor);
			}
		}
		color.x = (float)(color.x * totalDiffuseColor.x + color.x * ka + totalSpecularColor.x);
		color.y = (float)(color.y * totalDiffuseColor.y + color.y * ka + totalSpecularColor.y);
		color.z = (float)(color.z * totalDiffuseColor.z + color.z * ka + totalSpecularColor.z);
		
		return color;
	}
}
