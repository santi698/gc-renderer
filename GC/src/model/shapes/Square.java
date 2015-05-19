package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Square extends Plane {
	public Square(Vector3d normal, Point3d center, double side) {
		super(normal, center, side);
	}
	@Override
	public IntersectionContext trace(Ray ray) {
		IntersectionContext ic = super.trace(ray);
		if (ic.getHit()) {
			Point3d hitPoint = toLocal(Vectors.add(ic.getRay().getOrigin(), Vectors.scale(ic.getRay().getDirection(), ic.getT())));
			if (hitPoint.x < -0.5 || hitPoint.x > 0.5 || hitPoint.z < -0.5 ||hitPoint.z > 0.5)
				return IntersectionContext.noHit();
		}
		return ic;
	}
}
