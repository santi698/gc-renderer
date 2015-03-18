package model;
import javafx.geometry.Point3D;


public class DirectionalLight implements Light {
	private Point3D direction;
	
	public DirectionalLight(Point3D direction) {
		super();
		this.direction = direction;
	}
	@Override
	public double isVisible(Point3D point) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Point3D getDirection() {
		return direction;
	}
	
}
