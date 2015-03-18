package model;

import javafx.geometry.Point3D;

public interface Camera {
	public Point3D getPosition();
	public Point3D getUp();
	public Point3D getRight();
	public Point3D getDirection();
	public double getHorizFOV();
	public double getVertFOV();
	public int getXRes();
	public int getYRes();
	public double getDistanceToCamera();
}
