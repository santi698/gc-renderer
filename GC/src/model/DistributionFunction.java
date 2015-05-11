package model;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

public interface DistributionFunction {
	public abstract Color3f apply(Vector3d l, Vector3d n, Vector3d v);
}
