package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Triangle extends Shape {
	private Point3d p1;
	private Vector3d d1;
	private Vector3d d2;
	private Vector3d normal;
	
	public Triangle(Point3d p1, Point3d p2, Point3d p3) {
		super(new Vector3d(), new Vector3d(), 1);
		this.p1 = new Point3d(p1);
		this.d1 = Vectors.sub(p2, p1);
		this.d2 = Vectors.sub(p3, p1);	
		this.normal = Vectors.normalize(Vectors.cross(d1, d2));
	}

	@Override
	public IntersectionContext intersect(Ray ray) {
		Vector3d P = Vectors.cross(ray.getDirection(), d2);
		double det = P.dot(d1);
		if (det > -EPS && det < EPS) return IntersectionContext.noHit();
		double invDet = 1/det;
		Vector3d T = Vectors.sub(ray.getOrigin(), p1);
		double u = T.dot(P)*invDet;
		if (u < 0 || u > 1) 
			return IntersectionContext.noHit();
		Vector3d Q = Vectors.cross(T,d1);
		double v = ray.getDirection().dot(Q)*invDet;
		if (v < 0 || v > 1 || u + v > 1) 
			return IntersectionContext.noHit();
		double t = d2.dot(Q)*invDet;
		if (t > EPS) {
			if (normal.dot(ray.getDirection()) > 0)
				normal.negate();
			return new IntersectionContext(t, normal, ray, true, u, v);
		}
		return IntersectionContext.noHit();
	}
}
