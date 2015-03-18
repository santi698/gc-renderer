package model;

import javafx.geometry.Point3D;

public class IntersectionContext {
	private Point3D intersectionPoint;
	private Point3D normal;
	private boolean hit;
	
	public IntersectionContext(Point3D intersectionPoint, Point3D normal, boolean hit) {
		super();
		this.intersectionPoint = intersectionPoint;
		this.normal = normal;
		this.hit = hit;
	}
	public Point3D getIntersectionPoint() {
		return intersectionPoint;
	}
	public Point3D getNormal() {
		return normal;
	}
	public boolean getHit() {
		return hit;
	}
}
