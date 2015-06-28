package model.materials;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.brdfs.OrenNayar;
import model.light.Light;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import model.texture.Texture;
import util.Vectors;

public abstract class Material {
	public static int REFRACTIONDEPTH = 10;
	public static int REFLECTIONDEPTH = 4;
	private double reflectionCoefficient;
	private double refractionCoefficient;
	private Texture texture;
	private Sampler sampler;
	OrenNayar orenNayar;
	
	public Material(Texture texture) {
		super();
		this.texture = texture;
		this.sampler = new Multijittered(1);
		this.sampler.generateSamples();
		this.sampler.genShuffledIndices();
		this.reflectionCoefficient = 0;
		this.refractionCoefficient = 0;
		this.orenNayar = new OrenNayar(getRoughness());
	}
	public abstract Color3f directShade(IntersectionContext ic, List<Light> lights, 
			List<Body> bodies, int refractionDepth, int reflectionDepth);
	public Color3f indirectShade(IntersectionContext ic, List<Light> lights, 
			List<Body> bodies, int refractionDepth, int reflectionDepth) {
	Ray reflected = reflect(ic);
	Color3f bodyColor = getColor(ic.getU(), ic.getV());
	Vector3d direction = reflected.getDirection();
	Vector3d l = reflected.getDirection();
	Vector3d n = ic.getNormal();
	Vector3d v = ic.getRay().getDirection();

	Vector3d fixedNormal = new Vector3d(n);
	if (direction.dot(fixedNormal) < 0) //la normal siempre se toma en la direccion del rayo
		fixedNormal.negate();
	Vector3d sample = sampler.sampleHemisphere(getRoughness());
	Vector3d x = Vectors.cross(new Vector3d(0.00123, 1, 0.00321), direction);
	x.normalize();
	Vector3d sampledDirection = new Vector3d();
	sampledDirection.add(Vectors.scale(direction, sample.z));
	sampledDirection.add(Vectors.scale(x, sample.x));
	sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), sample.y));
	if (sampledDirection.dot(fixedNormal) < 0) { //Reflejar cuando esta debajo del plano normal
		sampledDirection.add(Vectors.scale(x, -2*sample.x));
		sampledDirection.add(Vectors.scale(Vectors.cross(x, direction), -2*sample.y));
	}
	sampledDirection.normalize();
	Color3f sampleColor;
	sampleColor = new Ray(sampledDirection, reflected.getOrigin()).trace(bodies).directShade(lights, bodies, refractionDepth+1, reflectionDepth+1);
	float pdf = (float) Math.pow(sampledDirection.dot(direction), getRoughness());
	if (Double.isInfinite(pdf))
		return sampleColor;
	sampleColor.scale(pdf);
	Color3f diffuseColor = orenNayar.apply(sampledDirection, n, v);
	Color3f lightColor = new Color3f(sampleColor);
	//Diffuse
	if (diffuseColor.x < 0) {
//		System.out.println("Diffuse color out of range. Value: " + diffuseColor.x+ "\n" + ic);
		diffuseColor.absolute();
	}
	diffuseColor.x *= lightColor.x * bodyColor.x;
	diffuseColor.y *= lightColor.y * bodyColor.y;
	diffuseColor.z *= lightColor.z * bodyColor.z;
	diffuseColor.scale((float) (1-reflectionCoefficient-refractionCoefficient));
	// Specular
	Color3f brdfC = orenNayar.apply(sampledDirection, n, v);
	brdfC.x *= lightColor.x * bodyColor.x;
	brdfC.y *= lightColor.y * bodyColor.y;
	brdfC.z *= lightColor.z * bodyColor.z;
	brdfC.scale((float)reflectionCoefficient);
	
	Color3f color = new Color3f();
	color.add(brdfC);
	color.add(diffuseColor);
	color.absolute();
	return color;
	}
//	public abstract Color3f indirectShade(IntersectionContext ic, List<Light> lights, 
//	List<Body> bodies, int refractionDepth, int reflectionDepth);

	
	private double getRoughness() {
		return 1;
	}
	public Color3f getColor(double u, double v) {
		return texture.get(u, v);
	}
	public boolean isThin() {
		return false;
	}
	public double getThickness() {
		return 0;
	}
	public static Ray reflect(IntersectionContext ic) {
		Vector3d d = new Vector3d(ic.getRay().getDirection());
		double m = ic.getNormal().dot(d);
		d.scale(2);
		d.sub(Vectors.scale(ic.getNormal(),2*m));
		return new Ray(d, ic.getIntersectionPoint());
	}
	
	public static Ray refract(IntersectionContext ic, double refractionIndex) {
		
		Vector3d t = Vectors.scale(ic.getRay().getDirection(), -1);
		Vector3d normal = new Vector3d(ic.getNormal());
		double no = refractionIndex;
		double costi = t.dot(ic.getNormal());
		if (costi < 0) { //Si el rayo sale del objeto
			costi = -costi;
			no = 1/no;
			normal.scale(-1);
		}
		double costt = Math.sqrt(1-(1-costi*costi)/(no*no));
		if (Double.isNaN(costt)) {
			return reflect(ic);
		}
		t.scale(-1/no);
		t.sub(Vectors.scale(normal, costt-costi/no));
//		if (t.length() != 1)
//			throw new RuntimeException();
		t.normalize();
		if (ic.getMaterial().isThin()) {
			Point3d p = new Point3d(ic.getIntersectionPoint());
			t.scale(ic.getMaterial().getThickness());
			p.add(t);
			return new Ray(ic.getRay().getDirection(), p);
		}
		return new Ray(t, ic.getIntersectionPoint());
	}
	public static boolean tir(IntersectionContext ic, double refractionIndex) {
		double costi = -ic.getRay().getDirection().dot(ic.getNormal());
		double no = refractionIndex;
		if (costi < 0) { //Si el rayo sale del objeto
			no = 1/no;
		}
		return 1-(1-costi*costi)/(no*no) < 0;
	}
	public Texture getTexture() {
		return texture;
	}
	@Override
	public String toString() {
		return "Material " + getClass().getName();
	}
}
