package model.cameras;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;

public abstract class Camera {
	private Point3d position;
	private Vector3d up;
	private Vector3d right;
	private Vector3d direction;
	private int xRes, yRes;
	private double pixelSize;
	private double focalLength;
	private double zoom = 1;
	public Camera(Point3d position, Point3d lookAt, Vector3d up, double focalLength, int xres, int yres) {
		double FOV = (0.035/focalLength)*Math.toRadians(49.13);
		this.position = position;
		this.direction = Vectors.normalize(Vectors.sub(lookAt, position));
		this.up = Vectors.normalize(up);
		this.right = Vectors.cross(direction, up);
		this.up = Vectors.normalize(Vectors.cross(right, direction));
		this.focalLength = focalLength;
		this.xRes = xres;
		this.yRes = yres;
		if (yres > xres)
			this.pixelSize = 2*focalLength*Math.sin(FOV/2)/yres;
		else
			this.pixelSize = 2*focalLength*Math.sin(FOV/2)/xres;
	}
	public Camera(Point3d position, Point3d lookAt, int xRes, int yRes) {
		this(position, lookAt, new Vector3d(0,-1,0), 0.035,  xRes, yRes);
	}
	public Camera(Point3d lookAt, int xRes, int yRes) {
		this(new Point3d(), lookAt, new Vector3d(0,-1,0), 0.035,  xRes, yRes);
	}
	public Point3d getPosition(){
		return position;
	};
	public abstract Point2d[] sampleLens();
	public Vector3d getUp(){
		return up;
	};
	public Vector3d getRight() {
		return right;
	}
	public Vector3d getDirection() {
		return direction;
	}
	public int getXRes(){
		return xRes;
	}
	public int getYRes() {
		return yRes;
	}
	public double getFocalLength() {
		return focalLength;
	}
	public double getPixelSize() {
		return pixelSize/zoom;
	}
	public double getLensRadius() {
		return 0;
	}
	public double getFocalDistance() {
		return getFocalLength();
	}
}
