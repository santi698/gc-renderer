package model;

import javafx.geometry.Point3D;

public class Plane implements Shape {
	private Point3D normal;
	private Point3D point;
	public Plane(Point3D normal, Point3D point) {
		this.normal = normal.normalize();
		this.point = point;
	}
	public Point3D getNormal() {
		return normal;
	}
	public void setNormal(Point3D normal) {
		this.normal = normal.normalize();
	}
	public Point3D getPoint() {
		return point;
	}
	public void setPoint(Point3D point) {
		this.point = point;
	}
	public double intersect (Ray ray) {
		return ray.getPoint().subtract(point).dotProduct(normal)/
			(ray.getDirection().dotProduct(normal));
	}
}
