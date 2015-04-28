package model.materials;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.light.Light;

public abstract class Material {
	public static final int REFRACTIONDEPTH = 10;
	public static final int REFLECTIONDEPTH = 4;
	private Color3f color;
	
	public Material(Color3f color) {
		super();
		this.color = color;
	}
	public abstract Color3f shade(IntersectionContext ic, Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth);
	
	public Color3f getColor() {
		return color;
	}
	public static Ray reflect(IntersectionContext ic) {
		Vector3d d = new Vector3d(ic.getRay().getDirection());
		double m = ic.getNormal().dot(d);
		d.scale(2);
		d.sub(Vectors.scale(ic.getNormal(),2*m));
		return new Ray(d, ic.getIntersectionPoint());
	}
	
	public static Ray refract(IntersectionContext ic, double refractionIndex) {
		
		Vector3d t = new Vector3d(ic.getRay().getDirection());
		
		double no = refractionIndex;
		double costi = t.dot(ic.getNormal());
		if (costi < 0) //Si el rayo sale del objeto
			no = 1/no;
		double costt = Math.sqrt(1- 1/((1/no)*(1/no)) * (1-costi*costi));
		if (costt == Double.NaN)
			throw new RuntimeException();
		t.scale(1/no);
		t.sub(Vectors.scale(ic.getNormal(), costt-1/no*costi));
		return new Ray(t, ic.getIntersectionPoint());
	}
}
