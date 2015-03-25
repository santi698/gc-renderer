package model.light;

import java.awt.Color;

import javax.vecmath.Point3d;

import model.IntersectionContext;
import model.Scene;

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
	public double getIntensity(IntersectionContext ic) {
		return intensity;
	};
}
