package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.DistributionFunction;

public class CookTorrance implements DistributionFunction {
	private double roughness;
	private double refractionIndex;
	private double R0;
	private double k;
	private double m2;
	private double m3;
	private double mm1m2;
	private double mm1;
	public CookTorrance(double roughness, double refractionIndex) {
		this.roughness = roughness;
		this.refractionIndex = refractionIndex;
		this.R0 = (1-refractionIndex/1+refractionIndex);
		this.R0 *= R0;
		double m = roughness;
		this.k = Math.sqrt(2*m*m/Math.PI);
		this.m2 = m*m; // m²
		this.m3 = m2*m; // m³
		this.mm1 = m-1;
		this.mm1m2 = mm1*m2; // (m-1)*m²
	}
	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		Vector3d h = new Vector3d(l);
		h.add(v);
		h.normalize();
		float value = (float) (D(h.dot(n))*F(h.dot(v))*G(v.dot(n)) / (4*l.dot(n)*v.dot(n)));
		Color3f color = new Color3f(value, value, value);
		return color;
	}
	public double D(double cosa) {
//		double x = cosa+mm1;
//		double x2 = x*x;
//		double aux = mm1m2*x2+m2;
//		return m3*x /(cosa*(aux*aux));
        double r1 = 1.0 / ( 4.0 * m2 * Math.pow(cosa, 4.0));
        double r2 = (cosa * cosa - 1.0) / (m2 * cosa * cosa);
        return r1 * Math.exp(r2);
	}
	public double F(double cosb) {
		return R0 + (1-R0)*Math.pow(1-cosb,5);
	}
	public double G(double cost) {
		return cost/((1-k)*cost+k);
	}
}
