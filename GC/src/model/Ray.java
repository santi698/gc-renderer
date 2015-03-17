package model;

import javafx.geometry.Point3D;

public class Ray {
	// Como soporta valores negativos es direccion y sentido
	private Point3D direction;
	private Point3D point;

	public Ray(Point3D direction, Point3D point) {
		this.direction = direction.normalize();
		this.point = point;
	}

	public Point3D getDirection() {
		return direction;
	}

	public void setDirection(Point3D direction) {
		this.direction = direction.normalize();
	}

	public Point3D getPoint() {
		return point;
	}

	public void setPoint(Point3D point) {
		this.point = point;
	}
	
}
