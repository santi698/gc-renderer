package scenes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;





import util.Vectors;
//import static util.Vectors.*;
import model.Body;
import model.Camera;
import model.Material;
import model.PinholeCamera;
import model.Scene;
import model.light.DirectionalLight;
import model.light.Light;
import model.light.PointLight;
import model.light.SpotLight;
//import model.light.SpotLight;
import model.shaders.Lambert;
import model.shaders.Phong;
import model.shapes.Plane;
import model.shapes.Sphere;
import model.shapes.Triangle;

@SuppressWarnings("unused")
public class SampleScene extends Scene {
	final static Point3d spherePos = new Point3d(0,1,10);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(-1,3,20), new Color3f(1,0.8f,0.8f), 0.25),
						new SpotLight(new Point3d(3,5,-1), new Color3f(1,0.5f,0.5f), 1.5, spherePos, Math.toRadians(30)),
						new SpotLight(new Point3d(-1,2,-1), new Color3f(0.5f,0.5f,1), 1.5, spherePos, Math.toRadians(30)),
//						new DirectionalLight(new Vector3d(0,-10,-1), new Color3f(1,1,1), 0.25)
						};
	
	//Bodies
	final static Sphere s2 = new Sphere(new Point3d(-2.5,0.5,15), 0.5); //Esfera chica
	final static Sphere s = new Sphere(spherePos, 1); // Esfera grande
	final static Plane p = new Plane(new Vector3d(0,1,0),new Point3d(0, 0, 0)); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0,0.1,1), new Point3d(-20,1,10), new Point3d(20,1,10)); //Triángulo en el piso

	final static Body[] bodies = new Body[] {
											new Body(s2, new Material(0, 0, 0, 1, new Color3f(0,1,1), new Lambert())), 
											new Body(s, new Material(0, 0, 0, 1, new Color3f(0.99f,0.99f,0.05f), new Phong(200))),
											new Body(p, new Material(0, 0, 0, 1, new Color3f(0.25f,0.40f,0.25f), new Lambert())),
											new Body(t, new Material(0, 0, 0, 1, new Color3f(1f,0f,0f), new Lambert()))
	};

	//Camera
	final static Camera camera = new PinholeCamera(new Point3d(0, 3, 0), spherePos, new Vector3d(0,-1,0), 0.5, 640, 480, Math.toRadians(49.13));
	
	public SampleScene() {
		super(lights,bodies,camera);
	}
}
