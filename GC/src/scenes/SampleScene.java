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
import model.materials.PolishedMarble;
import model.shapes.Plane;
import model.shapes.Sphere;
import model.shapes.Triangle;
import model.texture.CheckerBoardTexture;
import model.texture.ImageTexture;
import model.texture.PlainTexture;

@SuppressWarnings("unused")
public class SampleScene extends Scene {
	final static int AASamples = 16;
	final static Point3d spherePos = new Point3d(0, 0.05, 0.5);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(0, 25, 2), new Color3f(1,0.8f,0.8f), 1),
						new SpotLight(new Point3d(0,2,0.3), new Color3f(1,1f,0.5f), 0.01, spherePos, Math.toRadians(45)),
						new SpotLight(new Point3d(-1,2,-1), new Color3f(0.5f,0.5f,1), 0.05, spherePos, Math.toRadians(45)),
//						new DirectionalLight(new Vector3d(0,-10,-1), new Color3f(1,1,1), 0.25)
						};
	
	//Bodies
	final static Sphere s2 = new Sphere(new Point3d(0.2, 0.05, 0.8), 0.05); 
	final static Sphere s = new Sphere(spherePos, 0.05);
	final static Plane p = new Plane(new Vector3d(0,1,0),new Point3d(0, 0, 0)); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0.05,0.15,0.75), new Point3d(-0.15,0,0.5), new Point3d(0.15,0,0.8)); //Triángulo en el piso

	final static Body[] bodies = new Body[] {
//											new Body(s2, new PolishedMarble(new PlainTexture(new Color3f(0.2f,0.9f,0.9f)))),
											new Body(s2, new PolishedMarble(new CheckerBoardTexture(0.1))),
//											new Body(s, new Metal(new Color3f(0.99f,0.99f,0.05f), 200)),
//											new Body(s, new PolishedMarble(new Color3f(0.95f,0.95f,0.95f))),
											new Body(s, new Glass(new PlainTexture(new Color3f(0.9f, 0.1f, 0.1f)))),
//											new Body(s, new Glass(new CheckerBoardTexture())),
//											new Body(s, new Mirror(new Color3f(0.95f, 0.05f, 0.05f), 0.1)),
//											new Body(p, new Metal(new PlainTexture(new Color3f(0.25f, 0.40f, 0.25f)), 0.5)),
//											new Body(p, new Metal(new CheckerBoardTexture(0.1), 0.5)),
											new Body(p, new Matte(new CheckerBoardTexture(0.05))),
//											new Body(p, new Mirror(new Color3f(0.25f, 0.40f, 0.25f), 0.15)),
											new Body(t, new PolishedMarble(new PlainTexture(new Color3f(0.95f,0.01f,0.01f))))
	};

	//Camera
	final static Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,-1,0), 0.035, 720, 480, 5.6, spherePos, AASamples);
//	final static Camera camera = new PinholeCamera(new Point3d(0, 0.75, -0.1), spherePos, new Vector3d(0,-1,0), 0.035, 720, 480, Math.toRadians(49.13));
	
	public SampleScene() {
		super(lights,bodies,camera, AASamples);
	}
}
