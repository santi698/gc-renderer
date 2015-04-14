package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Ray;
import model.Scene;
import model.bodies.Body;
import util.Vectors;

public class SpotLight extends PointLight {
	private double angle;
	private Vector3d direction;
	
	public SpotLight(Point3d position, Color3f color, double intensity, Point3d pointAt, double angle) {
		super(position, color, intensity);
		this.angle = angle;
		this.direction = Vectors.normalize(Vectors.sub(pointAt, position));
	}
	
	@Override
	public double isVisible(Point3d point, Scene scene) {
		Vector3d direction = Vectors.normalize(Vectors.sub(getPosition(), point));
		if (direction.angle(this.direction) > angle/2)
			return 0;
		Ray ray = new Ray(direction, getPosition());
		for (Body body : scene.getObjects()) {
			if (body.getShape().intersect(ray).getHit()) {
				return 0;
			}
		}
		return 1;
	}
}
