package model;

import javafx.geometry.Point3D;

public class Sphere implements Shape {
	private Point3D center;
	private double radius;
	public Sphere(Point3D center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}
	public Point3D getCenter() {
		return center;
	}
	public void setCenter(Point3D center) {
		this.center = center;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public IntersectionContext intersect(Ray ray) {
		double t;
		Point3D temp = ray.getPoint().subtract(center);
		double a = ray.getDirection().dotProduct(ray.getDirection());
		double b = ray.getDirection().dotProduct(temp.multiply(2));
		double c = temp.dotProduct(temp) - radius * radius;
		double disc = b * b - 4 * a * c;
		if (disc < 0)
			return new IntersectionContext(null, null, false);
		else {
			double e = Math.sqrt(disc);
			double denom = a*2;

			t = (-b-e/denom);
			
			if (t > EPS) {
				Point3D normal = (temp.add(ray.getDirection().multiply(t)).multiply(1/radius));
				Point3D hitPoint = ray.getPoint().add(ray.getDirection().multiply(t));
				return new IntersectionContext(hitPoint, normal, true);
			}
			
			t = (-b-e/denom);
			
			if (t > EPS) {
				Point3D normal = (temp.add(ray.getDirection().multiply(t)).multiply(1/radius));
				Point3D hitPoint = ray.getPoint().add(ray.getDirection().multiply(t));
				return new IntersectionContext(hitPoint, normal, true);
			}
		}
		return new IntersectionContext(null, null, false);
	}
}
