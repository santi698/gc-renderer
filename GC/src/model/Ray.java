package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;

public class Ray {
	private Vector3d direction;
	private Point3d origin;
	private double originRefractionIndex;

	public Ray(Vector3d direction, Point3d origin, double originRefractionIndex) {
		this.direction = Vectors.normalize(direction);
		this.origin = new Point3d(origin);
		this.originRefractionIndex = originRefractionIndex;
	}
	public Vector3d getDirection() {
		return direction;
	}
	public Point3d getOrigin() {
		return origin;
	}
	public double getOriginRefractionIndex() {
		return originRefractionIndex;
	}
	public IntersectionContext trace(Body[] bodies) {
		Body closestBody = null;
		IntersectionContext effectiveIC = null;
		double minDistance = Double.MAX_VALUE;
		for (Body body : bodies) {
			IntersectionContext ic = body.intersect(this);
			if (ic.getHit()) {
				double distance = origin.distance(ic.getIntersectionPoint());
				if (distance < minDistance) {
					minDistance = distance;
					closestBody = body;
					effectiveIC = ic;
				}
			}
		}
		if (closestBody != null) {
			return effectiveIC;
		}
		else
			return IntersectionContext.noHit();
	}
}
