package model;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Ray {
	// Como soporta valores negativos es direccion y sentido
	private Vector3d direction;
	private Point3d point;

	public Ray(Vector3d direction, Point3d point) {
		direction.normalize();
		this.direction = direction;
		this.point = point;
	}

	public Vector3d getDirection() {
		return direction;
	}

	public void setDirection(Vector3d direction) {
		direction.normalize();
		this.direction = direction;
	}

	public Point3d getPoint() {
		return point;
	}

	public void setPoint(Point3d point) {
		this.point = point;
	}
	
}
