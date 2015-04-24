package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;

public abstract class Light {
	private Color3f color;
	private double intensity;
	
	protected Light(Color3f color, double intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	public abstract double isVisible(Point3d point, Body[] bodies);
	
	public abstract Vector3d getDirectionFromTo(Point3d point);
	
	public Color3f getColor() {
		return color;
	}
	
	public double getIntensity(Point3d p) {
		return intensity;
	};
}