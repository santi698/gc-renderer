package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class PerfectSpecular implements DistributionFunction {
	private double alpha;
	private double kR;
	public PerfectSpecular(double alpha, double kR) {
		this.alpha = alpha;
		this.kR = kR;
	}
	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		Vector3d r = new Vector3d(n);
		r.scale(2*l.dot(n));
		r.sub(l);
		if (v.dot(r) > 0) {
			float value = (float)(kR*Math.pow(Math.abs(v.dot(r)), alpha));
			return new Color3f(value, value, value);
		}
		else return new Color3f();
	}
}
