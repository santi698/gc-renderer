package model.materials;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.light.Light;
import model.texture.Texture;
import util.Vectors;

public abstract class Material {
	public static int REFRACTIONDEPTH = 10;
	public static int REFLECTIONDEPTH = 4;
	private Texture texture;
	
	public Material(Texture texture) {
		super();
		this.texture = texture;
	}
	public abstract Color3f shade(IntersectionContext ic, List<Light> lights, List<Body> bodies, int refractionDepth, int reflectionDepth);
	
	public Color3f getColor(double u, double v) {
		return texture.get(u, v);
	}
	public boolean isThin() {
		return false;
	}
	public double getThickness() {
		return 0;
	}
	public static Ray reflect(IntersectionContext ic) {
		Vector3d d = new Vector3d(ic.getRay().getDirection());
		double m = ic.getNormal().dot(d);
		d.scale(2);
		d.sub(Vectors.scale(ic.getNormal(),2*m));
		return new Ray(d, ic.getIntersectionPoint());
	}
	
	public static Ray refract(IntersectionContext ic, double refractionIndex) {
		
		Vector3d t = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d normal = new Vector3d(ic.getNormal());
		double no = refractionIndex;
		double costi = t.dot(ic.getNormal());
		if (costi < 0) { //Si el rayo sale del objeto
			costi = -costi;
			no = 1/no;
			normal.scale(-1);
		}
		double costt = Math.sqrt(1-(1-costi*costi)/(no*no));
		if (Double.isNaN(costt)) {
			return reflect(ic);
		}
		t.scale(-1/no);
		t.sub(Vectors.scale(normal, costt-costi/no));
//		if (t.length() != 1)
//			throw new RuntimeException();
		t.normalize();
		if (ic.getMaterial().isThin()) {
			Point3d p = new Point3d(ic.getIntersectionPoint());
			t.scale(ic.getMaterial().getThickness());
			p.add(t);
			return new Ray(ic.getRay().getDirection(), p);
		}
		return new Ray(t, ic.getIntersectionPoint());
	}
	public static boolean tir(IntersectionContext ic, double refractionIndex) {
		double costi = -ic.getRay().getDirection().dot(ic.getNormal());
		double no = refractionIndex;
		if (costi < 0) { //Si el rayo sale del objeto
			no = 1/no;
		}
		return 1-(1-costi*costi)/(no*no) < 0;
	}
	public Texture getTexture() {
		return texture;
	}
	@Override
	public String toString() {
		return "Material " + getClass().getName();
	}
}
