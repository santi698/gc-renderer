package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.shapes.Shape;
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
		Ray ray = new Ray(direction, point);
		double maxT = position.distance(point);
		for (Body body : bodies) {
			IntersectionContext ic = body.getShape().intersect(ray);
			if (ic.getHit()) {
				double t = ic.getT();
				if (maxT - t < Shape.EPS) {
					return 0;
				}
			}
		}
		return 1;
	}
	@Override
	public double getIntensity(Point3d p) {
		double distance = position.distance(p);
		return 100/(distance*distance)*super.getIntensity(p);
	}
	
	protected Point3d getPosition() {
		return position;
	}
	@Override
	public Vector3d getDirectionFromTo(Point3d point) {
		return Vectors.normalize(Vectors.sub(position, point));
	}
}