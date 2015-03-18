package model;

import java.awt.Color;

import javax.vecmath.Point3d;

public class AmbientLight extends Light {
	public AmbientLight(Color color, double intensity) {
		super(color, intensity);
	}

	@Override
	public double isVisible(Point3d point, Scene scene) {
		return 1;
	}

}
