package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import model.bodies.Body;

public class AmbientLight extends Light {
	public AmbientLight(Color3f color, double intensity) {
		super(color, intensity);
	}

	@Override
	public double isVisible(Point3d point, Body[] bodies) {
		return 1;
	}

}
