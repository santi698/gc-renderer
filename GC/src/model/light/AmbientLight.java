package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import model.Scene;

public class AmbientLight extends Light {
	public AmbientLight(Color3f color, double intensity) {
		super(color, intensity);
	}

	@Override
	public double isVisible(Point3d point, Scene scene) {
		return 1;
	}

}
