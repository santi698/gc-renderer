package model;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.bodies.Body;
import model.light.Light;

public class IntersectionContext {
	private Point3d intersectionPoint;
	private Body body;
	private Vector3d normal;
	private boolean hit;
	private double t;
	private static final IntersectionContext notHit = new IntersectionContext(null, null, false);
	public IntersectionContext(Point3d intersectionPoint, Vector3d normal, boolean hit) {
		super();
		this.intersectionPoint = intersectionPoint;
		this.normal = normal;
		this.hit = hit;
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
	public boolean getHit() {
		return hit;
	}
	public Color3f shade(Light[] lights, Body[] bodies) {
		//TODO
		if (body != null)
			return body.getMaterial().getColor();
		return new Color3f();
	}
}
