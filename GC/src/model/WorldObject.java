package model;

import static util.Transformations.rotateMatrix;
import static util.Transformations.scaleMatrix;
import static util.Transformations.translateMatrix;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class WorldObject {
	public static double EPS = 1e-6;
	private Matrix4d toGlobal;
	private Matrix4d toLocal;
	private Matrix4d toGlobalT;
	public WorldObject(Matrix4d transform) {
		toGlobal = transform;
		toGlobal.invert();
		updateMatrices();
	}
	public WorldObject(Vector3d translation, Vector3d rotation, double scale) {
		toGlobal = new Matrix4d();
		toGlobal.setIdentity();
		toGlobal.mul(translateMatrix(translation));
		toGlobal.mul(rotateMatrix(rotation));
		toGlobal.mul(scaleMatrix(scale));
		updateMatrices();
	}
	
	public void updateMatrices() {
		toLocal = new Matrix4d(toGlobal);
		toLocal.invert();
		toGlobalT = new Matrix4d(toGlobal);
		toGlobalT.transpose();
	}	
	public static Vector3d getRotation(Vector3d initialAxis, Vector3d finalAxis) {
		Vector3d rotation = new Vector3d();
		rotation.cross(initialAxis, finalAxis);
		if (rotation.length() == 0)
			return rotation;
		rotation.normalize();
		rotation.scale(initialAxis.angle(finalAxis));
		return rotation;
	}
	
	public Vector3d toLocal(Vector3d globalVector) {
		Vector3d localVector = new Vector3d(globalVector);
		toLocal.transform(localVector);
		return localVector;
	}
	public Point3d toLocal(Point3d globalPoint) {
		Point3d localPoint = new Point3d(globalPoint);
		toLocal.transform(localPoint);
		return localPoint;
	}
	
	public Vector3d toGlobal(Vector3d localVector) {
		Vector3d globalVector = new Vector3d(localVector);
		toGlobal.transform(globalVector);
		return globalVector;
	}
	public Point3d toGlobal(Point3d localPoint) {
		Point3d globalPoint = new Point3d(localPoint);
		toGlobal.transform(globalPoint);
		return globalPoint;
	}
	
	public Vector3d normalToGlobal(Vector3d normal) {
		Vector3d globalNormal = new Vector3d(normal);
		toGlobalT.transform(globalNormal);
		globalNormal.normalize();
		return globalNormal;
	}
	public void transform(Matrix4d transform) {
		toGlobal.mul(transform);
		updateMatrices();
	}
	public void rotate(Vector3d rotation) {
		toGlobal.mul(rotateMatrix(rotation));
		updateMatrices();
	}
	public void rotate(Matrix4d rotation) {
		toGlobal.mul(rotation);
		updateMatrices();
	}
	public void translate(Vector3d translation) {
		toGlobal.mul(translateMatrix(translation));
		updateMatrices();
	}
	public void scale(double scale) {
		toGlobal.mul(scaleMatrix(scale));
		updateMatrices();
	}
}
