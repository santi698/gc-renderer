package model.materials;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.DistributionFunction;
import model.IntersectionContext;
import model.light.AmbientLight;
import model.light.Light;
import model.texture.Texture;

public abstract class PureDiffuse extends Material {
	private DistributionFunction brdf;
	public PureDiffuse(Texture texture, DistributionFunction brdf) {
		super(texture);
		this.brdf = brdf;
	}
	@Override
	public Color3f shade(IntersectionContext ic, List<Light> lights, List<Body> bodies, int refractionDepth, int reflectionDepth) {
		Point3d p = ic.getIntersectionPoint();
		Vector3d v = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d n = ic.getNormal();

		Color3f color = new Color3f(getColor(ic.getU(), ic.getV()));
		Color3f totalDiffuseColor = new Color3f();
		Color3f totalAmbientColor = new Color3f();

		for (Light light: lights) {
			double visibility = light.isVisible(p, bodies);
			if (light instanceof AmbientLight) {
				Color3f lightColor = new Color3f(light.getColor());
				lightColor.scale((float) light.getIntensity(new Point3d()));
				totalAmbientColor.add(lightColor);
			}
			else if (visibility > 0) {
				Color3f diffuseColor = brdf.apply(light.getDirectionFromTo(p), n, v);
				if (diffuseColor.x < 0) {
//					System.out.println("Diffuse color out of range. Value: " + diffuseColor.x+ "\n" + ic);
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
		color.x = (float)(color.x * totalDiffuseColor.x + color.x * totalAmbientColor.x);
		color.y = (float)(color.y * totalDiffuseColor.y + color.y * totalAmbientColor.y);
		color.z = (float)(color.z * totalDiffuseColor.z + color.z * totalAmbientColor.z);
		return color;
	}
}
