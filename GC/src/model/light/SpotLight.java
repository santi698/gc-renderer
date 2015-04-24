package model.light;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.Ray;
import util.Vectors;

public class SpotLight extends PointLight {
	private double angle;
	private Vector3d direction;
	
	public SpotLight(Point3d position, Color3f color, double intensity, Point3d pointAt, double angle) {
		super(position, color, intensity);
		this.angle = angle;
		this.direction = Vectors.normalize(Vectors.sub(pointAt, position));
	}
	
	@Override
	public double isVisible(Point3d point, Body[] bodies) {
		Vector3d direction = Vectors.normalize(Vectors.sub(point, getPosition()));
		double incidenceAngle = direction.angle(this.direction);
		if (incidenceAngle > angle/2)
			return 0;
		return super.isVisible(point, bodies);
	}
}
