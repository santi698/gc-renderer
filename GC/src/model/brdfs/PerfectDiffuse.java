package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class PerfectDiffuse implements DistributionFunction {
	private double kD;
	public PerfectDiffuse(double kD) {
		this.kD = kD;
	}

	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		float result = (float)(kD*l.dot(n)); 
		return new Color3f(result, result, result);
	}
}
