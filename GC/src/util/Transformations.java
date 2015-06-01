package util;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

public class Transformations {
	public static Matrix4d identityMatrix;
	static {
		identityMatrix = new Matrix4d();
		identityMatrix.setIdentity();
	}
	public static Matrix4d translateMatrix(Vector3d translation) {
		Matrix4d tMatrix = new Matrix4d();
		tMatrix.setIdentity();
		tMatrix.setTranslation(translation);
		return tMatrix;
	}
	public static Matrix4d scaleMatrix(double scale) {
		Matrix4d tMatrix = new Matrix4d();
		tMatrix.setIdentity();
		tMatrix.setScale(scale);
		return tMatrix;
	}
	public static Matrix4d rotateXMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d();
		tMatrix.setIdentity();
		tMatrix.rotX(a);
		return tMatrix;
	}
	public static Matrix4d rotateYMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d();
		tMatrix.setIdentity();
		tMatrix.rotY(a);
		return tMatrix;
	}
	public static Matrix4d rotateZMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d();
		tMatrix.rotZ(a);
		return tMatrix;
	}
	public static Matrix4d rotateMatrix(Vector3d rotationVector) {
		Matrix4d matrix = new Matrix4d();
		matrix = rotateXMatrix(rotationVector.x);
		matrix.mul(rotateYMatrix(rotationVector.y));
		matrix.mul(rotateZMatrix(rotationVector.z));
		return matrix;
	}
}
