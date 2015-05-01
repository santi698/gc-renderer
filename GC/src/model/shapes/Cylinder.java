package model.shapes;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.IntersectionContext;
import model.Ray;

public class Cylinder extends Shape {
	private double heightFactor;
	public Cylinder(Point3d center, Vector3d axis, double radius, double height) {
		super(new Vector3d(center), getRotation(axis, new Vector3d(0,1,0)), radius);
		this.heightFactor = radius/height;
	}
	@Override
	public IntersectionContext intersect(Ray ray) {
		Vector3d rayDirection = toLocal(ray.getDirection());
		Point3d rayOrigin = toLocal(ray.getOrigin());
		double a = rayDirection.x*rayDirection.x + rayDirection.z*rayDirection.z;
		double b = 2*(rayOrigin.x*rayDirection.x + rayOrigin.z*rayDirection.z);
		double c = rayOrigin.x*rayOrigin.x + rayOrigin.z*rayOrigin.z - 1;
		double disc = b * b - 4 * a * c;
		if (disc < 0)
			return IntersectionContext.noHit();
		double e = Math.sqrt(disc);
		double t;
		t = (-b - e)/(2*a);
		if (t > EPS) {
			Point3d localHitPoint = Vectors.add(rayOrigin, Vectors.scale(rayDirection, t));
			Vector3d normal = new Vector3d(localHitPoint.x, 0, localHitPoint.z);
			if (localHitPoint.y*heightFactor < 1 && localHitPoint.y > 0) {
				Point2d uv = getUVCoordinates(localHitPoint);
				return new IntersectionContext(t, normalToGlobal(normal), ray, true, uv.x, uv.y);
			}
		}
		t = (-b + e)/(2*a);
		if (t > EPS) {
			Point3d localHitPoint = Vectors.add(rayOrigin, Vectors.scale(rayDirection, t));
			Vector3d normal = new Vector3d(-localHitPoint.x, 0, -localHitPoint.z);
			if (localHitPoint.y*heightFactor < 1 && localHitPoint.y > 0) {
				Point2d uv = getUVCoordinates(localHitPoint);
				return new IntersectionContext(t, normalToGlobal(normal), ray, true, uv.x, uv.y);
			}
		}
		return IntersectionContext.noHit();
	}
	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point3d texturePoint = localToTexture(localHitPoint);
		Point2d uv = new Point2d();
		double phi = Math.atan2(texturePoint.x, texturePoint.z);
		if (phi < 0)
			phi += 2*Math.PI;
		uv.x = phi / (2*Math.PI);
		uv.y = texturePoint.y;
		return uv;
	}
}
