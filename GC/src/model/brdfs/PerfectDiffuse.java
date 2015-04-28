package model.brdfs;

import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class PerfectDiffuse extends DistributionFunction {
	@Override
	public double apply(Vector3d l, Vector3d n, Vector3d v) {
		return l.dot(n);
	}
}
