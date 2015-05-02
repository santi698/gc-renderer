package scenes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import application.Main;
import static util.Vectors.*;
import model.SimpleBody;
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
import model.materials.PolishedMarble;
import model.shapes.Cylinder;
import model.shapes.Disc;
import model.shapes.Plane;
import model.shapes.Sphere;
import model.shapes.Triangle;
import model.texture.CheckerBoardTexture;
import model.texture.ImageTexture;
import model.texture.PlainTexture;

@SuppressWarnings("unused")
public class SampleScene extends Scene {
	final static int AASamples = Main.AASamples;
	
	final static Point3d spherePos = new Point3d(0, 0.05, 0.5);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(0, 0.5, 1), new Color3f(0.8f,0.8f,1f), 0.002),
						new SpotLight(new Point3d(0,2,0.3), new Color3f(1,1f,1f), 0.001, spherePos, Math.toRadians(45)),
						new SpotLight(new Point3d(-1,2,-1), new Color3f(0.9f,0.9f,1), 0.05, spherePos, Math.toRadians(30)),
						new DirectionalLight(new Vector3d(0,-10,-1), new Color3f(1,1,1), 0.25)
						};
	
	//Bodies
	final static Sphere s2 = new Sphere(new Point3d(0.2, 0.05, 0.8), 0.05); 
	final static Sphere s = new Sphere(spherePos, 0.05);
	final static Plane p = new Plane(new Vector3d(0,1,0), new Point3d(0, 0, 0.5), 0.4); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0.05,0.15,0.75), new Point3d(-0.15,0,0.5), new Point3d(0.15,0,0.8)); //Triángulo en el piso
	final static Triangle t2 = new Triangle(new Point3d(0,0.15,0.4), new Point3d(0.1,0,0.4), new Point3d(-0.1,0,0.4)); //Triángulo en el piso
	final static Cylinder c = new Cylinder(new Point3d(-0.05,0,0.55), new Vector3d(0, 1, 0), 0.02, 0.04); 

	final static SimpleBody[] bodies = new SimpleBody[] {
//											new SimpleBody(s2, new PolishedMarble(new CheckerBoardTexture(0.1))),
											new SimpleBody(s2, new Matte(new ImageTexture("textures/seamlesspaper2.jpg"))),
//											new SimpleBody(s, new Metal(new Color3f(0.99f,0.99f,0.05f), 200)),
//											new SimpleBody(s, new PolishedMarble(new Color3f(0.95f,0.95f,0.95f))),
											new SimpleBody(s, new Glass(new PlainTexture(new Color3f(0.9f, 0.1f, 0.1f)))),
//											new SimpleBody(s, new Glass(new CheckerBoardTexture())),
//											new SimpleBody(s, new Mirror(new Color3f(0.95f, 0.05f, 0.05f), 0.1)),
//											new SimpleBody(p, new Metal(new PlainTexture(new Color3f(0.25f, 0.40f, 0.25f)), 0.5)),
//											new SimpleBody(p, new Metal(new CheckerBoardTexture(0.5), 0.5)),
											new SimpleBody(p, new Matte(new ImageTexture("textures/1024/Wood 2.png", 1))),
//											new SimpleBody(p, new Mirror(new Color3f(0.25f, 0.40f, 0.25f), 0.15)),
											new SimpleBody(t, new PolishedMarble(new PlainTexture(new Color3f(0.85f,0.01f,0.01f)))),
//											new SimpleBody(t, new PolishedMarble(new CheckerBoardTexture(0.1))),
//											new SimpleBody(t2, new Glass(new PlainTexture(new Color3f(0.95f,0.01f,0.01f)), 0.02))
//											new SimpleBody(c, new PolishedMarble(new PlainTexture(new Color3f(0.2f, 0.1f, 0.1f))))

	};

	//Camera
//	final static Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480, 5.6, spherePos, AASamples);
	final static Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 1366, 768, 5.6, spherePos, AASamples);
//	final static Camera camera = new PinholeCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480);
	
	public SampleScene() {
		super(lights,bodies,camera, AASamples);
	}
}
