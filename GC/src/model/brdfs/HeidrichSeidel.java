package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.DistributionFunction;

public class HeidrichSeidel implements DistributionFunction {
	public Vector3d globalDirection;
	public double exponent;
	
	public HeidrichSeidel(Vector3d globalDirection, double exponent) {
		this.globalDirection = globalDirection;
		this.exponent = exponent;
	}

	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		Vector3d t = Vectors.cross(n, Vectors.cross(globalDirection, n));
		float result = (float) Math.pow(Math.cos(l.angle(t)+v.angle(t)), exponent);
		return new Color3f (result, result, result);
	}

}
