package model.light;

import java.awt.Color;

import javax.vecmath.Point3d;

import model.Scene;

public class AmbientLight extends Light {
	public AmbientLight(Color color, double intensity) {
		super(color, intensity);
	}

	@Override
	public double isVisible(Point3d point, Scene scene) {
		return 1;
	}

}
