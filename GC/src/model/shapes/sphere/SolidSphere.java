package model.shapes.sphere;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class SolidSphere extends Sphere {
	public SolidSphere(Point3d center, double radius) {
		super(center, radius);
	}
	public SolidSphere(Point3d center, double radius, double minPhi, double maxPhi, double minTheta, double maxTheta) {
		super(center, radius, minPhi, maxPhi, minTheta, maxTheta);
	}
	@Override
	protected Vector3d getNormalAt(Point3d localHitPoint, boolean isOutside) {
		return new Vector3d(localHitPoint); //Siempre apunta hacia afuera.
	}
}