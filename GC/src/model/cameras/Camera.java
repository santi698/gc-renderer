package model.cameras;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Ray;
import model.WorldObject;
import util.Vectors;

public abstract class Camera extends WorldObject{
	private Point3d position;
	private Vector3d up;
	private Vector3d right;
	private Vector3d direction;
	private int xRes, yRes;
	private double pixelSize;
	private double focalLength;
	private double zoom = 1;
	public Camera(Point3d position, Point3d lookAt, Vector3d up, double focalLength, int xres, int yres) {
		super(new Vector3d(position), new Vector3d(0,0,0), 1);
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
		this.rotate(calculateRotation());
	}
	private Matrix4d calculateRotation() {
		Matrix4d rotation = new Matrix4d(
				right.x,	right.y,	right.z,	0,
				up.x,		up.y,		up.z,		0,
				direction.x,direction.y,direction.z,0,
				0,			0,			0,			1
		);
		rotation.transpose();
		return rotation;
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
	public abstract Point2d[] sampleLensSet();
	public Point2d sampleLens() {
		return new Point2d();
	}
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
	public Ray rayThroughPixel(double x, double y, Point2d lensSample) {
		Point3d screenPoint = screenPoint(new Point2d(x, y));
		Point3d lensPoint = new Point3d(lensSample.x, lensSample.y, 0);
		Vector3d direction = new Vector3d(screenPoint.x - lensPoint.x, screenPoint.y - lensPoint.y, screenPoint.z - lensPoint.z);
		return new Ray(toGlobal(direction), toGlobal(lensPoint));
	}
	//XXX con esta función es muy sencillo cambiar la proyección de la cámara.
	private Point3d screenPoint(Point2d position) {
		double ps = getFocalDistance()/getFocalLength();
		double x = ps*pixelSize*(position.x - 0.5*xRes);
		double y = -ps*pixelSize*(position.y - 0.5*yRes);
		
		return new Point3d(x, y, getFocalDistance());
	}
}
