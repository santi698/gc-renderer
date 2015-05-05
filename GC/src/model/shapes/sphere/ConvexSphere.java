package model.shapes.sphere;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class ConvexSphere extends Sphere {
	public ConvexSphere(Point3d center, double radius, double minPhi, double maxPhi, double minTheta, double maxTheta) {
		super(center, radius, minPhi, maxPhi, minTheta, maxTheta);
	}
	@Override
	protected Vector3d getNormalAt(Point3d localHitPoint, boolean isOutside) {
		return new Vector3d(localHitPoint);
	}
}
