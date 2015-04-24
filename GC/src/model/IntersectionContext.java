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
	public double getT() {
		return t;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public Color3f shade(Light[] lights, Body[] bodies) {
		if (body == null | !this.hit)
			return bgColor;
		Color3f color = new Color3f(body.getMaterial().getColor());
		return body.getMaterial().getShader().shade(intersectionPoint, normal, Vectors.scale(ray.getDirection(),-1), lights, bodies, color);
	}
}
