package model.light;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.Body;
import model.Ray;

public class DirectionalLight extends Light {
	private Vector3d direction;
	
	public DirectionalLight(Vector3d direction, Color3f color, double intensity) {
		super(color, intensity);
		this.direction = Vectors.normalize(direction);
	}
	@Override
	public double isVisible(Point3d point, List<Body> bodies) {
		Ray ray = new Ray(Vectors.scale(direction, -1), toLocal(point));
		for (Body body : bodies) {
			//Si no la intercepta ningun objeto, es visible
			if (body.trace(ray).getHit())
				return 0;
		}
		return 1;
	}
	@Override
	public Vector3d getDirectionFromTo(Point3d point) {
		return Vectors.scale(toLocal(direction), -1);
	}
}