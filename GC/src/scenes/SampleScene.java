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
	final static Plane p = new Plane(new Vector3d(1,1,1),new Point3d(0.2, 0.1, 1));
	final static Sphere s = new Sphere(new Point3d(0.002,0.002,0), 0.1);
	final static Plane p2 = new Plane(new Vector3d(3,0,-1),new Point3d(3, 1, 1));
	final static Body[] bodies = new Body[] {new Body(s, Color.yellow, 0, 0, 1) };
//	new Body[] {new Body(p, Color.cyan, 0, 0, 1), 
//			new Body(s, Color.yellow, 0, 0, 1),
//			new Body(p2, Color.MAGENTA, 0,0,1)};
	final static Camera camera = new PinholeCamera(new Point3d(0, -1e-1, 1), new Vector3d(-5, 1, 0.5), new Vector3d(0,0,1), 1, 60, 60, 1024, 1024);
	public SampleScene() {
		super(null,bodies,camera);
	}
}
