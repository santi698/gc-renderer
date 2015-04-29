package model;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.light.Light;
import util.Vectors;

public class IntersectionContext {
	private static final IntersectionContext notHit = new IntersectionContext(Double.MAX_VALUE, null, null, false, 0, 0);
	private static final Color3f BGCOLOR = new Color3f(173f/255f, 216f/255f, 230f/255f);
//	private static final Color3f bgColor = new Color3f(1f, 1f, 1f);
	public static IntersectionContext noHit() {
		return notHit;
	}
	private Body body;
	private boolean hit;
	private Point3d intersectionPoint;
	private Vector3d normal;
	private Ray ray;
	private double t, u, v;
	public IntersectionContext(double t, Vector3d normal, Ray ray, boolean hit, double u, double v) {
		super();
		if (hit)
			this.intersectionPoint = new Point3d(Vectors.add(ray.getOrigin(), Vectors.scale(ray.getDirection(),t)));
		this.normal = normal;
		this.hit = hit;
		this.ray = ray;
		this.t = t;
		this.u = u;
		this.v = v;
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
	public double getT() {
		return t;
	}
	public double getU() {
		return u;
	}
	public double getV() {
		return v;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public Color3f shade(Light[] lights, Body[] bodies, int refractionDepth, int reflectionDepth) {
		if (body == null || !this.hit) {
			return new Color3f(BGCOLOR);
		}
		return body.getMaterial().shade(this, lights, bodies, refractionDepth, reflectionDepth);
	}
}
