package model;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;

public class PointLight extends Light {
	private Point3d position;
	
	public PointLight(Point3d position, Color color, double intensity) {
		super(color, intensity);
		this.position = position;
	}	
	@Override
	public double isVisible(Point3d point, Scene scene) {
		Vector3d direction = Vectors.normalize(Vectors.sub(position, point));
		Ray ray = new Ray(direction, point);
		for (Body body : scene.getObjects()) {
			if (body.getShape().intersect(ray).getHit()) {
				return 0;
			}
		}
		return 1;
	}

}
