package model.shapes.sphere;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class ThinSphere extends Sphere {
	public ThinSphere(Point3d center, double radius, double minPhi, double maxPhi, double minTheta, double maxTheta) {
		super(center, radius, minPhi, maxPhi, minTheta, maxTheta);
	}
	@Override
	protected Vector3d getNormalAt(Point3d localHitPoint, boolean isOutside) {
		Vector3d normal = new Vector3d(localHitPoint);
		if (isOutside)
			normal.negate();
		return normal;
	}
}
