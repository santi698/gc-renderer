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
		this.center = center;
		this.radius = radius;
	}
	
	public IntersectionContext intersect(Ray ray) {
		double t;
		Vector3d temp = Vectors.sub(ray.getOrigin(), center);
		double a = ray.getDirection().dot(ray.getDirection());
		double b = ray.getDirection().dot(Vectors.scale(temp,2));
		double c = temp.dot(temp) - radius * radius;
		double disc = b * b - 4 * a * c;
		if (disc < 0)
			return IntersectionContext.noHit();
		else {
			double e = Math.sqrt(disc);
			double denom = a*2;

			t = (-b-e)/denom;
			
			if (t > EPS) {
				Vector3d dt = Vectors.scale(ray.getDirection(), t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();			
				Vector3d pPlusDt = Vectors.add(dt, ray.getOrigin());
				Point3d hitPoint = new Point3d(pPlusDt);
				Vector3d normal = Vectors.normalize(Vectors.sub(hitPoint, center));
				return new IntersectionContext(t, normal, ray, true);
			}
			
			t = (-b+e)/denom;
			
			if (t > EPS) {
				Vector3d dt = Vectors.scale(ray.getDirection(), t);
				temp.add(dt);
				temp.scale(1/radius);
				temp.normalize();
				Vector3d pPlusDt = Vectors.add(dt, ray.getOrigin());
				Point3d hitPoint = new Point3d(pPlusDt);
				Vector3d normal = Vectors.normalize(Vectors.sub(hitPoint, center));
				return new IntersectionContext(t, normal, ray, true);
			}
		}
		return IntersectionContext.noHit();
	}
}