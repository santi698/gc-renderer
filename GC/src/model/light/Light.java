package model.light;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Transformations;
import model.Body;
import model.WorldObject;

public abstract class Light extends WorldObject {
	private Color3f color;
	private double intensity;
	
	protected Light(Color3f color, double intensity) {
		this(color, intensity, Transformations.getIdentity());
	}
	public Light(Color3f color, double intensity, Matrix4d transform) {
		super(transform);
		this.color = color;
		this.intensity = intensity;
	}
	public abstract double isVisible(Point3d point, List<Body> bodies);
	
	public abstract Vector3d getDirectionFromTo(Point3d point);
	
	public Color3f getColor() {
		return color;
	}
	
	public double getIntensity(Point3d p) {
		return intensity;
	};
}