package model.shapes;

import javax.vecmath.Point2d;
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
	public Vector3d calculateNormal(Vector3d rayDisplacement, Vector3d translation) {
		Vector3d temp = new Vector3d(translation);
		temp.add(rayDisplacement);
		temp.scale(1/radius);
		temp.normalize();
		return temp;
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
				Vector3d rayDisplacement = Vectors.scale(rayDirection, t);
				Point3d localHitPoint = new Point3d(Vectors.sub(Vectors.add(rayOrigin,rayDisplacement), center));
				Point2d uv = getUVCoordinates(localHitPoint);
				return new IntersectionContext(t, calculateNormal(rayDisplacement, temp), ray, true, uv.x, uv.y);
			}
			
			t = (-b+e)/denom;
			
			if (t > EPS) {
				Vector3d rayDisplacement = Vectors.scale(rayDirection, t);
				Point3d localHitPoint = new Point3d(Vectors.sub(Vectors.add(rayOrigin,rayDisplacement), center));
				Point2d uv = getUVCoordinates(localHitPoint);
				return new IntersectionContext(t, calculateNormal(rayDisplacement, temp), ray, true, uv.x, uv.y);
			}
		}
		return IntersectionContext.noHit();
	}
	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point2d uv = new Point2d();
		double theta = Math.acos(localHitPoint.y);
		double phi = Math.atan2(localHitPoint.x, localHitPoint.z);
		if (phi < 0)
			phi += 2*Math.PI;
		uv.x = phi / (2*Math.PI);
		uv.y = 1f	 - theta/Math.PI;
		return uv;
	}
}