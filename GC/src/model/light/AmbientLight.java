package model.light;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;

public class AmbientLight extends Light {
	public AmbientLight(Color3f color, double intensity) {
		super(color, intensity);
	}
	@Override
	public double isVisible(Point3d point, List<Body> bodies) {
		return 1;
	}

	@Override
	public Vector3d getDirectionFromTo(Point3d point) {
		return new Vector3d();
	}

}
