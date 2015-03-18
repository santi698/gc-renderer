package model;

import java.awt.Color;

import javax.vecmath.Point3d;

public abstract class Light {
	private Color color;
	private double intensity;
	
	protected Light(Color color, double intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	public abstract double isVisible(Point3d point, Scene scene);
	
	public Color getColor() {
		return color;
	}
	public double getIntensity() {
		return intensity;
	};
}
