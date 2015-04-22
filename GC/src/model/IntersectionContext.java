package model;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.light.Light;
import model.shapes.Sphere;

public class IntersectionContext {
	private Point3d intersectionPoint;
	private Ray ray;
	private Body body;
	private Vector3d normal;
	private boolean hit;
//	private double t;
	private static final IntersectionContext notHit = new IntersectionContext(null, null, null, false);
	public IntersectionContext(Point3d intersectionPoint, Vector3d normal, Ray ray, boolean hit) {
		super();
		this.intersectionPoint = intersectionPoint;
		this.normal = normal;
		this.hit = hit;
		this.ray = ray;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public static IntersectionContext noHit() {
		return notHit;
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
	public boolean getHit() {
		return hit;
	}
	public Color3f shade(Light[] lights, Body[] bodies) {
		if (body == null)
			return new Color3f();
		Color3f color = new Color3f(body.getMaterial().getColor());
		if (body.getShape() instanceof Sphere)
			body.getShape();
		return body.getMaterial().getShader().shade(intersectionPoint, normal, ray.getDirection(), lights, bodies, color);
	}
}
