package scenes;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

//import static util.Vectors.*;
import model.Body;
import model.Camera;
import model.Material;
import model.PinholeCamera;
import model.Scene;
import model.light.Light;
import model.light.PointLight;
//import model.light.SpotLight;
import model.shaders.Lambert;
import model.shaders.Phong;
import model.shapes.Plane;
import model.shapes.Sphere;
import model.shapes.Triangle;

public class SampleScene extends Scene {
	final static Point3d spherePos = new Point3d(5,5,5);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(-2,1,10), new Color3f(1,1,1), 1),
						new PointLight(new Point3d(1,-1,-1), new Color3f(1,0,0), 0.4)
						};
	
	//Bodies
	final static Triangle p = new Triangle(new Point3d(0,0,10), new Point3d(0,1,0), new Point3d(10,0,0)); //FIXME no se ve el triángulo
	final static Sphere s = new Sphere(spherePos, 0.5);
	final static Plane p2 = new Plane(new Vector3d(5,0,-1),new Point3d(10, 10, 10));
	final static Body[] bodies = new Body[] {
											new Body(p, new Material(0, 0, 0, 1, new Color3f(Color.cyan), new Lambert())), 
											new Body(s, new Material(0, 0, 0, 1, new Color3f(Color.yellow), new Phong(100))),
											new Body(p2, new Material(0, 0, 0, 1, new Color3f(Color.GRAY), new Lambert()))
	};
	
	//Camera
	final static Camera camera = new PinholeCamera(new Point3d(0, 0, 0), spherePos, new Vector3d(0,0,1), 3, 640, 640);
	
	public SampleScene() {
		super(lights,bodies,camera);
	}
}
