package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.bodies.Body;
import util.Vectors;

public class PointLight extends Light {
	private Point3d position;
	
	public PointLight(Point3d position, Color3f color, double intensity) {
		super(color, intensity);
		this.position = position;
	}	
	@Override
	public double isVisible(Point3d point, Body[] bodies) {
		Vector3d direction = Vectors.normalize(Vectors.sub(position, point));
		Ray ray = new Ray(direction, position);
		for (Body body : bodies) {
			if (body.getShape().intersect(ray).getHit()) {
				return 0;
			}
		}
		return 1;
	}
	@Override
	public double getIntensity(IntersectionContext ic) {
		return 100/(position.distance(ic.getIntersectionPoint())*position.distance(ic.getIntersectionPoint()))*super.getIntensity(ic)*Math.abs(ic.getNormal().dot(Vectors.normalize(Vectors.sub(ic.getIntersectionPoint(), position))));
	}
	
	protected Point3d getPosition() {
		return position;
	}
}