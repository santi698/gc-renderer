package scenes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import static util.Vectors.*;
import model.Body;
import model.Scene;
import model.cameras.Camera;
import model.cameras.PinholeCamera;
import model.cameras.ThinLensCamera;
import model.light.DirectionalLight;
import model.light.Light;
import model.light.PointLight;
import model.light.SpotLight;
import model.materials.Glass;
import model.materials.Lambert;
import model.materials.Material;
import model.materials.Matte;
import model.materials.Metal;
import model.materials.Mirror;
//import model.light.SpotLight;
import model.materials.Phong;
import model.shapes.Plane;
import model.shapes.Sphere;
import model.shapes.Triangle;

@SuppressWarnings("unused")
public class SampleScene extends Scene {
	final static Point3d spherePos = new Point3d(0, 0.25, 0.3);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(0, 20, 0.25), new Color3f(1,0.8f,0.8f), 10),
						new SpotLight(new Point3d(9,5,-1), new Color3f(1,0.5f,0.5f), 0.35, spherePos, Math.toRadians(10)),
						new SpotLight(new Point3d(-1,5,-1), new Color3f(0.5f,0.5f,1), 0.15, spherePos, Math.toRadians(30)),
//						new DirectionalLight(new Vector3d(0,-10,-1), new Color3f(1,1,1), 0.25)
						};
	
	//Bodies
	final static Sphere s2 = new Sphere(new Point3d(0, 1, 1.5), 1); 
	final static Sphere s = new Sphere(spherePos, 0.25);
	final static Plane p = new Plane(new Vector3d(0,1,0),new Point3d(0, 0, 0)); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0,0.01,1), new Point3d(-0.25,0.01,0.35), new Point3d(0.25,0.01,0.35)); //Triángulo en el piso

	final static Body[] bodies = new Body[] {
//											new Body(s2, new Matte(new Color3f(0,1,1))), 
//											new Body(s, new Metal(new Color3f(0.99f,0.99f,0.05f), 200)),
//											new Body(s, new Glass(new Color3f(1f, 1f, 1f), 0.1)),
											new Body(s, new Mirror(new Color3f(0.95f, 0.05f, 0.05f), 0.1)),
											new Body(p, new Metal(new Color3f(0.25f,0.40f,0.25f), 0.5)),
//											new Body(p, new Mirror(new Color3f(0.25f, 0.40f, 0.25f), 0.15)),
//											new Body(t, new Matte(new Color3f(0.95f,0.01f,0.01f)))
	};

	//Camera
	final static Camera camera = new ThinLensCamera(new Point3d(0, 0.5, -0.1), spherePos, new Vector3d(0,-1,0), 0.035, 720, 480, Math.toRadians(49.13), 2.8, spherePos);
	
	public SampleScene() {
		super(lights,bodies,camera);
	}
}
