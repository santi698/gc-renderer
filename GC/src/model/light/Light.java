package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import model.IntersectionContext;
import model.Scene;

public abstract class Light {
	private Color3f color;
	private double intensity;
	
	protected Light(Color3f color, double intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	public abstract double isVisible(Point3d point, Scene scene);
	
	public Color3f getColor() {
		return color;
	}
	public double getIntensity(IntersectionContext ic) {
		return intensity;
	};
}
