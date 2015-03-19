package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;

public class Ray {
	// Como soporta valores negativos es direccion y sentido
	private Vector3d direction;
	private Point3d origin;

	public Ray(Vector3d direction, Point3d origin) {
		this.direction = Vectors.normalize(direction);
		this.origin = origin;
	}
	public Vector3d getDirection() {
		return direction;
	}
	public Point3d getOrigin() {
		return origin;
	}	
}
