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

public class DiffuseAndSpecular extends Material {
	private double ka, is, id;
	private DistributionFunction diffuse;
	private DistributionFunction specular;
	public DiffuseAndSpecular(Texture bodyTexture, double ka, DistributionFunction diffuse, DistributionFunction specular) {
		super(bodyTexture);
		this.diffuse = diffuse;
		this.specular = specular;
		this.ka = ka;
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
				
				Color3f diffuseColor = diffuse.apply(l, n, v);
				if (diffuseColor.x < 0) {
					diffuseColor.absolute();
//					System.out.println(ic);
				}
				diffuseColor.scale((float)id);
				diffuseColor.x *= lightColor.x;
				diffuseColor.y *= lightColor.y;
				diffuseColor.z *= lightColor.z;
				
				Color3f specularColor = specular.apply(l, n, v);
				specularColor.scale((float)is);
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
