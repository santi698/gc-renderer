package model;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.light.Light;
import util.Vectors;

public class IntersectionContext {
	private static final IntersectionContext notHit = new IntersectionContext(Double.MAX_VALUE, null, null, false);
	private static final Color3f bgColor = new Color3f(173/255f, 216/255f, 230/255f);
	public static IntersectionContext noHit() {
		return notHit;
	}
	private Body body;
	private boolean hit;
	private Point3d intersectionPoint;
	private Vector3d normal;
	private Ray ray;
	private double t;
	public IntersectionContext(double t, Vector3d normal, Ray ray, boolean hit) {
		super();
		if (hit)
			this.intersectionPoint = new Point3d(Vectors.add(ray.getOrigin(), Vectors.scale(ray.getDirection(),t)));
		this.normal = normal;
		this.hit = hit;
		this.ray = ray;
		this.t = t;
	}
	public boolean getHit() {
		return hit;
	}
	public Point3d getIntersectionPoint() {
		return intersectionPoint;
	}
	public Vector3d getNormal() {
		return normal;
	}
	public Ray getRay() {
		return ray;
	}
	public Color3f reflect(Light[] lights, Body[] bodies, int reflectionDepth, int refractionDepth) {
		if (reflectionDepth == 0) {
			return new Color3f();
		}
		Vector3d d = new Vector3d(ray.getDirection());
		double m = normal.dot(d);
		d.scale(2);
		d.sub(Vectors.scale(normal,2*m));
		return new Ray(d, intersectionPoint, body.getMaterial().getRefractionIndex()).trace(bodies).shade(lights, bodies, reflectionDepth-1, refractionDepth);
	}
	public Color3f refract(Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		if (refractionDepth == 0)
			return new Color3f();
		Vector3d t = new Vector3d(ray.getDirection());
		double no = (t.dot(normal)>0)?1:body.getMaterial().getRefractionIndex();
		double ni = ray.getOriginRefractionIndex();
		double costi = Math.cos(t.angle(normal));
		double costt = Math.sqrt(1- 1/((ni/no)*(ni/no)) * (1-costi*costi));
		if (costt == Double.NaN)
			return reflect(lights, bodies, reflectionDepth, refractionDepth);
		t.scale(ni/no);
		t.sub(Vectors.scale(normal, costt-ni/no*costi));
		return new Ray(t, intersectionPoint, no).trace(bodies).shade(lights, bodies, reflectionDepth, refractionDepth-1);
	}
	public double getT() {
		return t;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public Color3f shade(Light[] lights, Body[] bodies, int reflectionDepth, int refractionDepth) {
		if (body == null || !this.hit) {
			return bgColor;
		}
		Color3f bodyColor = body.getMaterial().getColor();
		Color3f color = new Color3f();
		if (body.getMaterial().getAbsorptionCoefficient() > 1e-5) {			
			Color3f AbColor = body.getMaterial().getShader().shade(intersectionPoint, normal, Vectors.scale(ray.getDirection(),-1), lights, bodies, bodyColor);
			AbColor.scale((float)body.getMaterial().getAbsorptionCoefficient());
			color.add(AbColor);
		}
		if (body.getMaterial().getReflectionCoefficient() > 1e-5) {
			Color3f RlColor = reflect(lights, bodies, reflectionDepth, refractionDepth);
			RlColor.scale((float)body.getMaterial().getReflectionCoefficient());
			color.add(RlColor);
		}
		if (body.getMaterial().getTransmissionCoefficient() > 1e-5) {
			Color3f RrColor = refract(lights, bodies, refractionDepth, reflectionDepth);
			RrColor.scale((float)body.getMaterial().getTransmissionCoefficient());
			color.add(RrColor);
		}
		return color;
	}
}
