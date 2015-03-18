package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class IntersectionContext {
	private Point3d intersectionPoint;
	private Vector3d normal;
	private boolean hit;
	private static final IntersectionContext notHit = new IntersectionContext(null, null, false);
	public IntersectionContext(Point3d intersectionPoint, Vector3d normal, boolean hit) {
		super();
		this.intersectionPoint = intersectionPoint;
		this.normal = normal;
		this.hit = hit;
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
}
