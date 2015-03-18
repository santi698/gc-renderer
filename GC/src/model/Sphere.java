package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Sphere implements Shape {
	private Point3d center;
	private double radius;
	public Sphere(Point3d center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}
	public Point3d getCenter() {
		return center;
	}
	public void setCenter(Point3d center) {
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
		Vector3d temp = new Vector3d(ray.getPoint());
		temp.sub(center);
		double a = ray.getDirection().dot(ray.getDirection());
		Vector3d temp2 = new Vector3d(temp); temp2.scale(2);
		double b = ray.getDirection().dot(temp2);
		double c = temp.dot(temp) - radius * radius;
		double disc = b * b - 4 * a * c;
		if (disc < 0)
			return new IntersectionContext(null, null, false);
		else {
			double e = Math.sqrt(disc);
			double denom = a*2;

			t = (-b-e/denom);
			
			if (t > EPS) {
				Vector3d dt = new Vector3d(ray.getDirection());
				dt.scale(t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();
				Vector3d normal = new Vector3d(temp);
				Point3d pPlusDt = new Point3d(ray.getPoint());
				pPlusDt.add(dt);
				Point3d hitPoint = new Point3d(pPlusDt);
				return new IntersectionContext(hitPoint, normal, true);
			}
			
			t = (-b-e/denom);
			
			if (t > EPS) {
				Vector3d dt = new Vector3d(ray.getDirection());
				dt.scale(t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();
				Vector3d normal = new Vector3d(temp);
				Point3d pPlusDt = new Point3d(ray.getPoint());
				pPlusDt.add(dt);
				Point3d hitPoint = new Point3d(pPlusDt);
				return new IntersectionContext(hitPoint, normal, true);
			}
		}
		return new IntersectionContext(null, null, false);
	}
}
