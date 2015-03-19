package scenes;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Body;
import model.Camera;
import model.PinholeCamera;
import model.Plane;
import model.Scene;
import model.Sphere;

public class SampleScene extends Scene {
	final static Plane p = new Plane(new Vector3d(0,1,0),new Point3d(0, 1, 0));
	final static Sphere s = new Sphere(new Point3d(0,2,0), 0.1);
	final static Body[] bodies = new Body[] {new Body(p, Color.red, 0, 0, 1), new Body(s, Color.green, 0, 0, 1)};
	final static Camera camera = new PinholeCamera(new Point3d(0, 0, 0), new Vector3d(1, 0, 0), new Vector3d(0,0,1), 1, 60, 60, 1024, 1024);
	public SampleScene() {
		super(null,bodies,camera);
	}
}
