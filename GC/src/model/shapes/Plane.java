package model.shapes;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Transformations;
import util.Vectors;

public class Plane implements Shape {
	private Vector3d normal;
	private Point3d point;
	private int tileHeight = 1;
	private int tileWidth = 1;
	
	public Plane(Vector3d normal, Point3d point) {
		this.normal = Vectors.normalize(normal);
		this.point = new Point3d(point);
	}
	
	public IntersectionContext intersect (Ray ray) {
		Vector3d rayOrigin = new Vector3d(ray.getOrigin());
		Vector3d rayDirection = new Vector3d(ray.getDirection());
		Vector3d difference = Vectors.sub(point, rayOrigin);
		double t = difference.dot(normal)/
			(rayDirection.dot(normal));
		if (normal.dot(rayDirection) < 0) {
			normal.negate();
		}
		if (t > EPS) {
			Vector3d displacement = new Vector3d(rayDirection);
			displacement.scale(t);
			Point3d hitPoint = new Point3d(rayOrigin);
			hitPoint.add(displacement);
			Point3d localHitPoint = new Point3d(hitPoint); //FIXME falta calcular los verdaderos valores
			localHitPoint.sub(point);
			Vector3d rotation = Vectors.scale(normal, normal.angle(new Vector3d(0,1,0)));
			Transformations.rotateMatrix(rotation).transform(localHitPoint);
			Point2d uv = getUVCoordinates(localHitPoint);
			return new IntersectionContext(t, normal, ray, true, uv.x, uv.y);
		}
		else return IntersectionContext.noHit();
	}

	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point2d uv = new Point2d();
		uv.x = (((localHitPoint.x % (tileWidth)) / tileWidth)+1)/2d;
		uv.y = (((localHitPoint.z % (tileHeight)) / tileHeight)+1)/2d;
		return uv;
	}
}
