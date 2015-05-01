package model.shapes;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import static util.Transformations.*;

public abstract class Shape {
	//Necesario para evitar self-occlusion.
	public static double EPS = 1e-6;
	private Matrix4d toGlobal;
	private Matrix4d toLocal;
	private Matrix4d toGlobalT;
	private Matrix4d translation;
	private Matrix4d rotation;
	private Matrix4d scaling;
	private Matrix4d invTranslation;
	private Matrix4d invRotation;
	private Matrix4d invScaling;
	private boolean scaleTexture = true;
	private boolean rotateTexture = true;
	public Shape(Vector3d translation, Vector3d rotation, double scale) {
		this.invTranslation = translateMatrix(translation);
		this.invRotation = rotateMatrix(rotation);
		this.invScaling = scaleMatrix(scale);
		updateMatrices();
	}
	public void updateMatrices() {
		toGlobal = new Matrix4d();
		toGlobal.setIdentity();
		toGlobal.mul(this.invTranslation);
		toGlobal.mul(this.invRotation);
		toGlobal.mul(this.invScaling);
		this.scaling = new Matrix4d();
		this.rotation = new Matrix4d();
		this.translation = new Matrix4d();
		this.scaling.invert(invScaling);
		this.rotation.invert(invRotation);
		this.translation.invert(invTranslation);
		toLocal = new Matrix4d(toGlobal);
		toLocal.invert();
		toGlobalT = new Matrix4d(toGlobal);
		toGlobalT.transpose();
	}
	public abstract IntersectionContext intersect(Ray ray);
	
	public static Vector3d getRotation(Vector3d initialAxis, Vector3d finalAxis) {
		Vector3d rotation = new Vector3d();
		rotation.cross(initialAxis, finalAxis);
		if (rotation.length() == 0)
			return rotation;
		rotation.normalize();
		rotation.scale(initialAxis.angle(finalAxis));
		return rotation;
	}
	
	public void setScaleTexture(boolean value) {
		scaleTexture = value;
	}
	public void setRotateTexture(boolean value) {
		rotateTexture = value;
	}
	public Vector3d toLocal(Vector3d globalVector) {
		Vector3d localVector = new Vector3d(globalVector.x, globalVector.y, globalVector.z);
		toLocal.transform(localVector);
		return localVector;
	}
	public Point3d toLocal(Point3d globalPoint) {
		Point3d localPoint = new Point3d(globalPoint.x, globalPoint.y, globalPoint.z);
		toLocal.transform(localPoint);
		return localPoint;
	}
	
	public Vector3d toGlobal(Vector3d localVector) {
		Vector3d globalVector = new Vector3d(localVector.x, localVector.y, localVector.z);
		toGlobal.transform(globalVector);
		return globalVector;
	}
	public Point3d toGlobal(Point3d localPoint) {
		Point3d globalPoint = new Point3d(localPoint.x, localPoint.y, localPoint.z);
		toGlobal.transform(globalPoint);
		return globalPoint;
	}
	
	public Vector3d normalToGlobal(Vector3d normal) {
		Vector3d globalNormal = new Vector3d(normal.x, normal.y, normal.z);
		toGlobalT.transform(globalNormal);
		globalNormal.normalize();
		return globalNormal;
	}
	public Vector3d rotateVector(Vector3d vector) {
		Vector3d result = new Vector3d(vector.x, vector.y, vector.z);
		invRotation.transform(vector);
		return result;
	}
	public void rotate(Vector3d rotation) {
		this.invRotation.mul(rotateMatrix(rotation));
		updateMatrices();
	}
	public void translate(Vector3d translation) {
		this.invTranslation.mul(translateMatrix(translation));
		updateMatrices();
	}
	public void scale(double scale) {
		this.invScaling.mul(scaleMatrix(scale));
		updateMatrices();
	}
	public Point3d localToTexture(Point3d localPoint) {
		Point3d texturePoint = new Point3d(localPoint);
		if (scaleTexture)
			scaling.transform(texturePoint);
		if (rotateTexture)
			rotation.transform(texturePoint);
		texturePoint.x = texturePoint.x % 1;
		texturePoint.y = texturePoint.y % 1;
		texturePoint.z = texturePoint.z % 1;
		return texturePoint;
	}
}
