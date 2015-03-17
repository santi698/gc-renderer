package model;

import javafx.geometry.Point3D;

public class PinholeCamera implements Camera {
	private Point3D position;
	private Point3D direction;
	private double horizFOV;
	private double vertFOV;
	private double horizTilt;
	private double vertTilt;
	public PinholeCamera(Point3D position, Point3D direction, double horizFOV,
			double vertFOV, double horizTilt, double vertTilt) {
		super();
		this.position = position;
		this.direction = direction;
		this.horizFOV = horizFOV;
		this.vertFOV = vertFOV;
		this.horizTilt = horizTilt;
		this.vertTilt = vertTilt;
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
		this.direction = direction;
	}
	public double getVertFOV() {
		return vertFOV;
	}
	public void setVertFOV(double vertFOV) {
		this.vertFOV = vertFOV;
	}
	public double getHorizTilt() {
		return horizTilt;
	}
	public void setHorizTilt(double horizTilt) {
		this.horizTilt = horizTilt;
	}
	public double getVertTilt() {
		return vertTilt;
	}
	public void setVertTilt(double vertTilt) {
		this.vertTilt = vertTilt;
	}
	public double getHorizFOV() {
		return horizFOV;
	}
	@Override
	public Ray[][] getRaysThroughScreen() {
		//TODO Iterar por todos los puntos de la pantalla y tirar rectas desde el centro.
		return null;
	}
}
