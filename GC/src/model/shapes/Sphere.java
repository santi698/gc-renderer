package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Sphere implements Shape {
	private Point3d center;
	private double radius;
	
	public Sphere(Point3d center, double radius) {
		super();
		this.center = new Point3d(center);
		this.radius = radius;
	}
	
	public IntersectionContext intersect(Ray ray) {
		Vector3d rayDirection = new Vector3d(ray.getDirection());
		Point3d rayOrigin = new Point3d(ray.getOrigin());
		double t;
		Vector3d temp = Vectors.sub(rayOrigin, new Point3d(center));
		double a = rayDirection.dot(rayDirection);
		double b = rayDirection.dot(Vectors.scale(temp,2));
		double c = temp.dot(temp) - (radius * radius);
		double disc = (b * b) - (4 * a * c);
		if (disc < 0)
			return IntersectionContext.noHit();
		else {
			double e = Math.sqrt(disc);
			double denom = a*2;

			t = (-b-e)/denom;
			
			if (t > EPS) {
				Vector3d dt = Vectors.scale(rayDirection, t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();
				return new IntersectionContext(t, temp, ray, true);
			}
			
			t = (-b+e)/denom;
			
			if (t > EPS) {
				Vector3d dt = Vectors.scale(rayDirection, t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();
				return new IntersectionContext(t, temp, ray, true);
			}
		}
		return IntersectionContext.noHit();
	}
}