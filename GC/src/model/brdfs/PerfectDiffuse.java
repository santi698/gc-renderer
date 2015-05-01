package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class PerfectDiffuse extends DistributionFunction {
	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		float result = (float)l.dot(n); 
		return new Color3f(result, result, result);
	}
}
