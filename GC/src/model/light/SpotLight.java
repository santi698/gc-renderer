package model.light;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Ray;
import model.Scene;
import model.bodies.Body;

public class SpotLight extends PointLight {
	private double angle;
	private Vector3d direction;
	
	public SpotLight(Point3d position, Color color, double intensity, Vector3d direction, double angle) {
		super(position, color, intensity);
		this.angle = angle;
		this.direction = direction;
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
