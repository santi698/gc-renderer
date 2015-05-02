package model.shapes;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.WorldObject;

public abstract class Shape extends WorldObject {
	//Necesario para evitar self-occlusion.
	private boolean scaleTexture = true;
	private boolean rotateTexture = true;
	public Shape(Vector3d translation, Vector3d rotation, double scale) {
		super(translation, rotation, scale);
	}
	public abstract IntersectionContext intersect(Ray ray);
	
	public void setScaleTexture(boolean value) {
		scaleTexture = value;
	}
	public void setRotateTexture(boolean value) {
		rotateTexture = value;
	}
	public Point3d localToTexture(Point3d localPoint) {
		Point3d texturePoint = new Point3d(localPoint);
		if (scaleTexture)
			getScaling().transform(texturePoint);
		if (rotateTexture)
			getRotation().transform(texturePoint);
		texturePoint.x = texturePoint.x % 1;
		texturePoint.y = texturePoint.y % 1;
		texturePoint.z = texturePoint.z % 1;
		return texturePoint;
	}
}
