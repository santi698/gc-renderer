package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class NullBrdf implements DistributionFunction {

	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		return new Color3f();
	}

}
