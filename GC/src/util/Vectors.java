package util;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Vectors {
	public static Vector3d add(Vector3d v1, Vector3d v2) {
		Vector3d v = new Vector3d(v1);
		v.add(v2);
		return v;
	}
	public static Vector3d add(Vector3d v1, Point3d v2) {
		Vector3d v = new Vector3d(v2);
		v.add(v1);
		return v;
	}
	public static Point3d add(Point3d v1, Vector3d v2) {
		return new Point3d(add(v2,v1));
	}
	public static Vector3d scale(Vector3d v1, double scale) {
		Vector3d v = new Vector3d(v1);
		v.scale(scale);
		return v;
	}
	public static Vector3d cross(Vector3d v1, Vector3d v2) {
		Vector3d v = new Vector3d();
		v.cross(v1, v2);
		return v;
	}
	public static Vector3d normalize(Vector3d v1) {
		Vector3d v = new Vector3d(v1);
		v.normalize();
		return v;
	}
	public static Vector3d sub(Vector3d v1, Vector3d v2) {
		Vector3d v = new Vector3d(v1);
		v.sub(v2);
		return v;
	}
	public static Vector3d sub(Vector3d v1, Point3d v2) {
		Vector3d v = new Vector3d(v1);
		v.sub(v2);
		return v;
	}
	public static Vector3d sub(Point3d v1, Vector3d v2) {
		Vector3d v = new Vector3d(v1);
		v.sub(v2);
		return v;
	}
	public static Vector3d sub(Point3d v1, Point3d v2) {
		Vector3d v = new Vector3d(v1);
		v.sub(v2);
		return v;
	}
	public static Vector3d negate(Vector3d v) {
		Vector3d v1 = new Vector3d(v);
		v1.negate();
		return v1;
	}
}
