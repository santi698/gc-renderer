package model.light;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Ray;
import model.Scene;
import model.bodies.Body;

public class DirectionalLight extends Light {
	private Vector3d direction;
	
	public DirectionalLight(Vector3d direction, Color3f color, double intensity) {
		super(color, intensity);
		this.direction = direction;
	}
	@Override
	public double isVisible(Point3d point, Scene scene) {
		Ray ray = new Ray(direction, point);
		for (Body body : scene.getObjects()) {
			//Si no la intercepta ningun objeto, es visible
			if (body.getShape().intersect(ray).getHit())
				return 0;
		}
		return 1;
	}
}
