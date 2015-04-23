package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Plane implements Shape {
	private Vector3d normal;
	private Point3d point;
	
	public Plane(Vector3d normal, Point3d point) {
		this.normal = Vectors.normalize(normal);
		this.point = point;
	}
	
	public IntersectionContext intersect (Ray ray) {
		Vector3d rayOrigin = new Vector3d(ray.getOrigin());
		Vector3d rayDirection = new Vector3d(ray.getDirection());
		Vector3d difference = Vectors.sub(point, rayOrigin);
		double t = difference.dot(normal)/
			(rayDirection.dot(normal));
		if (t > EPS) {
			Vector3d displacement = new Vector3d(rayDirection);
			displacement.scale(t);
			Point3d hitPoint = new Point3d(rayOrigin);
			hitPoint.add(displacement);
			return new IntersectionContext(t, normal, ray, true);
		}
		else return IntersectionContext.noHit();
	}
}
