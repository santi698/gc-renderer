package model.brdfs;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.DistributionFunction;

public class OrenNayar implements DistributionFunction {
	private double albedo;
	private double A;
	private double B;
	
	public OrenNayar(double sigma, double albedo) {
		this.albedo = albedo;
		double sigmaSq = sigma*sigma;
		this.A = 1-0.5*sigmaSq/(sigmaSq+0.57);
		this.B = 0.45*sigmaSq/(sigmaSq+0.09);
	}
	public OrenNayar(double sigma) {
		this(sigma, 1);
	}
	@Override
	public Color3f apply(Vector3d l, Vector3d n, Vector3d v) {
		if (v.dot(n)<0)
			n.negate();
		double vAn = v.angle(n);
		double lAn = l.angle(n);
		double alpha = Math.max(vAn, lAn);
		double beta = Math.min(lAn, vAn);
		//    float gamma    = dot( v - n * dot( v, n ), l - n * dot( l, n ) );
		double gamma = Vectors.sub(v, Vectors.scale(n, v.dot(n))).dot(Vectors.sub(l, Vectors.scale(n, l.dot(n))));
		
		float result = (float)(albedo/Math.PI*l.dot(n)*(A+B*Math.max(0, gamma)*Math.sin(alpha)*Math.tan(beta)));
		return new Color3f(result, result, result);
	}
}
