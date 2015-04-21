package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import util.Vectors;

public class PinholeCamera implements Camera {
	private Point3d position;
	private Vector3d direction;
	private Vector3d up;
	private Vector3d right;
	private double distanceToCamera;
	private int xRes;
	private int yRes;
	
	public PinholeCamera(Point3d position, Point3d lookAt, Vector3d up, double distanceToCamera, int xres, int yres) {
		this.position = position;
		this.direction = Vectors.normalize(Vectors.sub(lookAt, position));
		this.up = Vectors.normalize(up);
		this.right = Vectors.cross(direction, up);
		this.up = Vectors.normalize(Vectors.cross(right, direction));
		this.distanceToCamera = distanceToCamera;
		this.xRes = xres;
		this.yRes = yres;
		assert(this.up.dot(this.right) == 0);
		assert(this.direction.dot(this.right) == 0);
	}
	
	public Point3d getPosition() {
		return position;
	}
	public void setPosition(Point3d position) {
		this.position = position;
	}
	public Vector3d getDirection() {
		return direction;
	}
	public void setDirection(Vector3d direction) {
		direction.normalize();
		this.direction = direction;
	}
	public double getDistanceToCamera() {
		return distanceToCamera;
	}
	public Vector3d getUp() {
		return up;
	}

	public void setUp(Vector3d up) {
		this.up = Vectors.normalize(up);
		this.right = Vectors.cross(direction, up);
	}
	
	public Vector3d getRight() {
		return right;
	}
	
	public int getXRes() {
		return xRes;
	}

	public int getYRes() {
		return yRes;
	}
}
