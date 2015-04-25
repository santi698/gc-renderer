package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public interface Camera {
	public Point3d getPosition();
	public Vector3d getUp();
	public Vector3d getRight();
	public Vector3d getDirection();
	public int getXRes();
	public int getYRes();
	public double getDistanceToCamera();
	public double getPixelSize();
}
