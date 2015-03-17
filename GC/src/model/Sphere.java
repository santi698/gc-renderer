package model;

import javafx.geometry.Point3D;

public class Sphere implements Shape {
	private Point3D center;
	private double radius;
	public Sphere(Point3D center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}
	public Point3D getCenter() {
		return center;
	}
	public void setCenter(Point3D center) {
		this.center = center;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double intersect(Ray ray) {
		//TODO
		return 0;
	}
}
