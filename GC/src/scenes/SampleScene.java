package scenes;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.Camera;
import model.PinholeCamera;
import model.Scene;
import model.bodies.Body;
import model.bodies.Material;
import model.bodies.Plane;
import model.bodies.Sphere;
import model.light.Light;
import model.light.PointLight;
import model.light.SpotLight;

public class SampleScene extends Scene {
	//Lights
	final static Light[] lights = new Light[] {new PointLight(new Point3d(5,1,1), Color.white, 1.5),
												new SpotLight(new Point3d(0, -1e-1, 1), Color.yellow, 1.1, new Vector3d(-5, 1, 0.5), 10)};
	
	//Bodies
	final static Plane p = new Plane(new Vector3d(1,1,1),new Point3d(0.2, 0.1, 1));
	final static Sphere s = new Sphere(new Point3d(0.02,0.02,0.9), 0.005);
	final static Plane p2 = new Plane(new Vector3d(3,0,-1),new Point3d(3, 1, 1));
	final static Body[] bodies = new Body[] {new Body(p, new Material(0, 0, 0, 1, new Color3f(Color.cyan))), 
											new Body(s, new Material(0, 0, 0, 1, new Color3f(Color.yellow))),
											new Body(p2, new Material(0, 0, 0, 1, new Color3f(Color.magenta)))};
	
	//Camera
	final static Camera camera = new PinholeCamera(new Point3d(0, -1e-1, 1), new Vector3d(-5, 1, 0.5), new Vector3d(0,0,1), 1, 60, 60, 256, 256);
	
	public SampleScene() {
		super(lights,bodies,camera);
	}
}
