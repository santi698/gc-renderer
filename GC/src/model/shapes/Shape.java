package model.shapes;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.WorldObject;
import model.trees.Traceable;

public abstract class Shape extends WorldObject implements Traceable {
	//Necesario para evitar self-occlusion.
	private boolean scaleTexture = true;
	private boolean rotateTexture = true;
	public Shape(Vector3d translation, Vector3d rotation, double scale) {
		super(translation, rotation, scale);
	}
	public Shape(Matrix4d transform) {
		super(transform);
	}
	public abstract IntersectionContext trace(Ray ray);
	
	public void setNotScaleTexture() {
		scaleTexture = false;
	}
	public boolean getNotScaleTexture() {
		return !scaleTexture;
	}
	public boolean getNotRotateTexture() {
		return !rotateTexture;
	}
	public void setNotRotateTexture() {
		rotateTexture = false;
	}
	public Point3d localToTexture(Point3d localPoint) {
		Point3d texturePoint = new Point3d(localPoint);
		if (!scaleTexture)
//			getScaling().transform(texturePoint); TODO
		if (!rotateTexture)
//			getRotation().transform(texturePoint); TODO
		texturePoint.x = texturePoint.x;
		texturePoint.y = texturePoint.y;
		texturePoint.z = texturePoint.z;
		return texturePoint;
	}
	
	public abstract BoundingBox getBoundingBox();
	public boolean intersectsBox(BoundingBox box) {
		return box.intersectsBox(getBoundingBox());
	}
}
