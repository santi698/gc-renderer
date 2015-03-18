package model;

import javafx.geometry.Point3D;

public class PinholeCamera implements Camera {
	private Point3D position;
	private Point3D direction;
	private Point3D up;
	private Point3D right;
	private double distanceToCamera;
	private double horizFOV;
	private double vertFOV;
	private int xres;
	private int yres;
	
	public PinholeCamera(Point3D position, Point3D direction, Point3D up, double distanceToCamera,
			double horizFOV, double vertFOV, int xres, int yres) {
		super();
		this.position = position;
		this.direction = direction.normalize();
		this.up = up.normalize();
		this.right = direction.crossProduct(up);
		this.distanceToCamera = distanceToCamera;
		this.horizFOV = horizFOV;
		this.vertFOV = vertFOV;
		this.xres = xres;
		this.yres = yres;
	}
	
	public Point3D getPosition() {
		return position;
	}
	public void setPosition(Point3D position) {
		this.position = position;
	}
	public Point3D getDirection() {
		return direction;
	}
	public void setDirection(Point3D direction) {
		this.direction = direction.normalize();
	}
	public double getVertFOV() {
		return vertFOV;
	}
	public double getHorizFOV() {
		return horizFOV;
	}
	public double getDistanceToCamera() {
		return distanceToCamera;
	}
	public Point3D getUp() {
		return up;
	}

	public void setUp(Point3D up) {
		this.up = up.normalize();
		this.right = direction.crossProduct(up);
	}
	
	public Point3D getRight() {
		return right;
	}
	
	public int getXres() {
		return xres;
	}

	public int getYres() {
		return yres;
	}
}
