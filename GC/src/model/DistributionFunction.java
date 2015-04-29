package model;

import javax.vecmath.Vector3d;

public abstract class DistributionFunction {
	public abstract double apply(Vector3d l, Vector3d n, Vector3d v);
}
