package util;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

public class Transformations {
	public Matrix4d translateMatrix(Vector3d translation) {
		Matrix4d tMatrix = new Matrix4d(
										1,	0,	0,	0,
										0,	1,	0,	0,
										0,	0,	1,	0,
										translation.x, translation.y, translation.z, 1);
		return tMatrix;
	}
	public Matrix4d scaleMatrix(Vector3d scale) {
		Matrix4d tMatrix = new Matrix4d(
										scale.x,0,		0,		0,
										0,		scale.y,0,		0,
										0,		0,		scale.z,0,
										0, 		0, 		0, 		1);
		return tMatrix;
	}
	public Matrix4d rotateXMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d(
										1,		0,			0,			0,
										0,		Math.cos(a),-Math.sin(a),0,
										0,		Math.sin(a),Math.cos(a),0,
										0, 		0, 			0,			1);
		return tMatrix;
	}
	public Matrix4d rotateYMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d(
										Math.cos(a),0,			Math.sin(a),0,
										0,			1,			0,			0,
										-Math.sin(a),0,			Math.cos(a),0,
										0, 			0, 			0,			1);
		return tMatrix;
	}
	public Matrix4d rotateZMatrix(double a) {
		Matrix4d tMatrix = new Matrix4d(
										Math.cos(a),-Math.sin(a),	0,		0,
										Math.sin(a),Math.cos(a),	0,		0,
										0,			0,				1,		0,
										0, 			0, 				0,		1);
		return tMatrix;
	}
}
