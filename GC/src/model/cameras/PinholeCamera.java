package model.cameras;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class PinholeCamera extends Camera {	
	public PinholeCamera(Point3d position, Point3d lookAt, Vector3d up, double zoomFactor, int xres, int yres, double FOV) {
		super(position, lookAt, up, zoomFactor, xres, yres, FOV);
	}
	@Override
	public Point2d[] sampleLens() {
		return new Point2d[]{new Point2d(0,0)};
	}
}
