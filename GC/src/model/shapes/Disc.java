package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.IntersectionContext;
import model.Ray;

public class Disc extends Plane {
	public Disc(Vector3d normal, Point3d center, double radius) {
		super(normal, center, radius);
	}
	@Override
	public IntersectionContext intersect(Ray ray) {
		IntersectionContext ic = super.intersect(ray);
		if (ic.getHit()) {
			Point3d hitPoint = toLocal(Vectors.add(ic.getRay().getOrigin(), Vectors.scale(ic.getRay().getDirection(), ic.getT())));
			if (hitPoint.x*hitPoint.x + hitPoint.z*hitPoint.z > 1)
				return IntersectionContext.noHit();
		}
		return ic;
	}
}
