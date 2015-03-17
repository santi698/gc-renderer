package model;

import javafx.geometry.Point3D;

public class Screen {
	/* La normal de la pantalla deberia ser
	 * la direccion de un rayo desde la camara,
	 * y la interseccion de ese rayo 
	 * con el plano de la camara, su centro.
	 */
	private Plane plane;
	private Point3D center;
	/* Ancho y alto en medidas, 
	 * a la hora de correr el 
	 * programa se da la resolucion.
	 */
	private double width;
	private double height;
	public Screen(Plane plane, Point3D center, double width, double height) {
		super();
		this.plane = plane;
		this.center = center;
		this.width = width;
		this.height = height;
	}
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	public Point3D getCenter() {
		return center;
	}
	public void setCenter(Point3D center) {
		this.center = center;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
}
