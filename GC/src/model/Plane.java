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
	public IntersectionContext intersect (Ray ray) {
		double t = ray.getPoint().subtract(point).dotProduct(normal)/
			(ray.getDirection().dotProduct(normal));
		if (t > EPS) {
			Point3D hitPoint = ray.getPoint().add(ray.getDirection().multiply(t));
			return new IntersectionContext(hitPoint, normal, true);
		}
		else return new IntersectionContext(null, null, false);
	}
}
