package model.shapes;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Plane extends Shape {
	private Vector3d normal;
	public Plane(Vector3d normal, Point3d point) {
		super(new Vector3d(point), getRotation(normal, new Vector3d(0,1,0)), 1);
		this.normal = Vectors.normalize(normal);
	}
	public Plane(Vector3d normal, Point3d point, double scale) {
		super(new Vector3d(point), Vectors.scale(Vectors.normalize(normal), normal.angle(new Vector3d(0,1,0))), scale);
		this.normal = Vectors.normalize(normal);
	}
	public IntersectionContext trace (Ray ray) {
		Point3d rayOrigin = toLocal(ray.getOrigin());
		Vector3d rayDirection = toLocal(ray.getDirection());
		Vector3d normal = new Vector3d(this.normal);
		double differenceY = -rayOrigin.y;
		double normalY = 1;
		double t = differenceY/rayDirection.y;
		if (normalY*rayDirection.y > 0) {
			normal.negate();
		}
		if (t > EPS) {
			Vector3d displacement = new Vector3d(rayDirection);
			displacement.scale(t);
			Point3d hitPoint = new Point3d(rayOrigin);
			hitPoint.add(displacement);
			Point2d uv = getUVCoordinates(hitPoint);
			return new IntersectionContext(t, normal, ray, true, uv.x, uv.y);
		}
		else return IntersectionContext.noHit();
	}

	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point3d texturePoint = localToTexture(localHitPoint);
		Point2d uv = new Point2d();
		uv.x = texturePoint.x;
		uv.y = texturePoint.z;
		return uv;
	}
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}
}
