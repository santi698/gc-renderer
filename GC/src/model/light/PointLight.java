package model.light;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.shapes.Shape;
import util.Vectors;

public class PointLight extends Light {
	private static final int LDECAY = 100;
	private Point3d position;
	
	public PointLight(Point3d position, Color3f color, double intensity) {
		super(color, intensity);
		this.position = position;
	}	
	@Override
	public double isVisible(Point3d globalPoint, List<Body> bodies) {
		Point3d localPoint = toLocal(globalPoint);
		Vector3d direction = Vectors.normalize(Vectors.sub(position, localPoint));
		Ray ray = new Ray(direction, localPoint);
		double maxT = position.distance(localPoint);
		Ray globalRay = new Ray(toGlobal(ray.getDirection()), toGlobal(ray.getOrigin()));
		IntersectionContext ic = globalRay.trace(bodies);
		if (ic.getHit() && maxT - ic.getT() > Shape.EPS)
			return 0;
		return 1;
	}
	@Override
	public double getIntensity(Point3d globalPoint) {
		Point3d localPoint = toLocal(globalPoint);
		double distance = position.distance(localPoint);
		return LDECAY/(distance*distance)*super.getIntensity(localPoint);
	}
	
	protected Point3d getPosition() {
		return toGlobal(position);
	}
	@Override
	public Vector3d getDirectionFromTo(Point3d point) {
		return toGlobal(Vectors.normalize(Vectors.sub(position, toLocal(point))));
	}
}