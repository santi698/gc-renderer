package model.shapes.sphere;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;
import model.shapes.Shape;
import util.Vectors;

public abstract class Sphere extends Shape {
	private double radius;
	private double minPhi = -Math.PI;
	private double maxPhi = Math.PI;
	private double minTheta = -Math.PI/2;
	private double maxTheta = Math.PI/2;
	private Point3d center;
	
	public Sphere(Point3d center, double radius) {
		super(new Vector3d(center), new Vector3d(), radius);
		this.center = center;
		this.radius = radius;
	}
	public Sphere(Point3d center, double radius, double minPhi, double maxPhi, double minTheta, double maxTheta) {
		this(center, radius);
		this.minPhi = minPhi;
		this.maxPhi = maxPhi;
		this.minTheta = minTheta;
		this.maxTheta = maxTheta;
	}
	public IntersectionContext trace(Ray ray) {
		Vector3d rayDirection = toLocal(ray.getDirection());
		Point3d rayOrigin = toLocal(ray.getOrigin());
		double t;
		Vector3d temp = new Vector3d(rayOrigin);
		double a = rayDirection.dot(rayDirection);
		double b = rayDirection.dot(Vectors.scale(temp,2));
		double c = temp.dot(temp) - 1;
		double disc = (b * b) - (4 * a * c);
		if (disc < 0)
			return IntersectionContext.noHit();
		else {
			double e = Math.sqrt(disc);
			double denom = a*2;

			t = (-b-e)/denom;
			
			if (t > EPS) {
				Vector3d rayDisplacement = Vectors.scale(rayDirection, t);
				Point3d localHitPoint = new Point3d(Vectors.add(rayOrigin,rayDisplacement));
				if (checkBounds(localHitPoint)) {
					Point2d uv = getUVCoordinates(localHitPoint);
					Vector3d normal = normalToGlobal(getNormalAt(localHitPoint, true));
					normal.normalize();
					return new IntersectionContext(t, normal, ray, true, uv.x, uv.y);
				}
			}
			
			t = (-b+e)/denom;
			
			if (t > EPS) {
				Vector3d rayDisplacement = Vectors.scale(rayDirection, t);
				Point3d localHitPoint = new Point3d(Vectors.add(rayOrigin,rayDisplacement));
				if (checkBounds(localHitPoint)) {
					Point2d uv = getUVCoordinates(localHitPoint);
					Vector3d normal = normalToGlobal(getNormalAt(localHitPoint, false));
					normal.normalize();
					return new IntersectionContext(t, normal, ray, true, uv.x, uv.y);
				}
			}
		}
		return IntersectionContext.noHit();
	}
	protected abstract Vector3d getNormalAt(Point3d localHitPoint, boolean isOutside);
	private boolean checkBounds(Point3d localHitPoint) {
		double theta = Math.asin(localHitPoint.y); 
		double phi = Math.atan2(localHitPoint.x, localHitPoint.z);
		return theta > minTheta && theta < maxTheta && phi > minPhi && phi < maxPhi;
	}
	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point3d texturePoint = localToTexture(localHitPoint);
		Point2d uv = new Point2d();
		double theta;
		if (getNotScaleTexture())
			theta = Math.asin(texturePoint.y*radius);
		else
			theta = Math.asin(texturePoint.y); 
		double phi = Math.atan2(texturePoint.x, texturePoint.z);
		uv.x = 0.5f + phi / (maxPhi-minPhi);
		uv.y = 0.5f - theta/(maxTheta-minTheta);
		if (getNotScaleTexture()) {
			uv.x *= radius;
			uv.y *= radius;
		}
			
		return uv;
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(center.x-radius, center.y-radius, center.z-radius, center.x+radius, center.y+radius, center.z+radius);
	}
}