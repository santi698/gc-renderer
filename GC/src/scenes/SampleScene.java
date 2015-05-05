package scenes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import application.Main;
import static util.Vectors.*;
import model.Body;
import model.SimpleBody;
import model.Scene;
import model.UniformCompoundBody;
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
import model.shapes.Shape;
import model.shapes.Square;
import model.shapes.Triangle;
import model.shapes.sphere.ConcaveSphere;
import model.shapes.sphere.ConvexSphere;
import model.shapes.sphere.SolidSphere;
import model.shapes.sphere.ThinSphere;
import model.texture.CheckerBoardTexture;
import model.texture.ImageTexture;
import model.texture.PlainTexture;

@SuppressWarnings("unused")
public class SampleScene extends Scene {
	final static int AASamples = Main.AASamples;
	final static Color3f sunsetColor = new Color3f(1,0.85f,0.66f);
	final static Point3d spherePos = new Point3d(0, 0.05, 0.5);
	//Lights
	final static Light[] lights = new Light[] {
						new PointLight(new Point3d(0, 0.5, 1), new Color3f(0.8f,0.8f,1f), 0.001),
						new SpotLight(new Point3d(0,2,0.3), new Color3f(1,1f,1f), 0.005, spherePos, Math.toRadians(45)),
						new SpotLight(new Point3d(-1,2,-1), new Color3f(0.9f,0.9f,1), 0.04, spherePos, Math.toRadians(30)),
						new DirectionalLight(new Vector3d(0,-0.05,1), sunsetColor, 2)
						};
	
	//Bodies
	final static SolidSphere s2 = new SolidSphere(new Point3d(0.2, 0.05, 0.8), 0.05);
	final static SolidSphere s = new SolidSphere(spherePos, 0.05);
	final static double fraction = 0.1;	
	final static ConvexSphere scn = new ConvexSphere(new Point3d(Vectors.sub(spherePos,new Point3d(-0.03, 0, -0.03))), 0.04, -Math.PI*fraction, Math.PI*fraction, -Math.PI*2*fraction, Math.PI*2*fraction);
	final static ConvexSphere scv = new ConvexSphere(new Point3d(Vectors.sub(spherePos,new Point3d(0.03, 0, 0.03))), 0.05, -Math.PI*fraction, Math.PI*fraction, -Math.PI*2*fraction, Math.PI*2*fraction);
	final static Plane p = new Plane(new Vector3d(0,1,0), new Point3d(0, 0, 0.5)); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0.05,0.15,0.75), new Point3d(-0.15,0,0.5), new Point3d(0.15,0,0.8)); //Triángulo en el piso
	final static Triangle t2 = new Triangle(new Point3d(0,0.05,0.4), new Point3d(0.1,0,0.4), new Point3d(-0.1,0,0.4)); //Triángulo en el piso
//	final static Cylinder c = new Cylinder(new Point3d(-0.05,0,0.55), new Vector3d(0, 1, 0), 0.01, 2*Math.PI*0.01);
//	final static Disc d1 = new Disc(new Vector3d(0, 1, 0), new Point3d(-0.05, 2*Math.PI*0.01, 0.55), 0.01);
//	final static Disc d2 = new Disc(new Vector3d(0, -1, 0), new Point3d(-0.05, 0, 0.55), 0.01);
//	final static Square sq = new Square(new Vector3d(0,1,0), new Point3d(-0.05, Math.PI*0.01, 0.55), 0.04);
	
	static {
		double yRotation = Math.PI/4;
		scv.rotate(new Vector3d(0, yRotation, 0));
	}

	final static Body[] bodies = new Body[] {
//											new SimpleBody(s2, new PolishedMarble(new CheckerBoardTexture(0.1))),
											new SimpleBody(s2, new Matte(new ImageTexture(Main.class.getResource("/textures/seamlesspaper2.jpg")))),
//											new SimpleBody(s, new Metal(new Color3f(0.99f,0.99f,0.05f), 200)),
//											new SimpleBody(s, new Metal(new ImageTexture("textures/2400/earth_night.jpg"), 0.4)),
//											new SimpleBody(s, new Matte(new CheckerBoardTexture(0.1))),
											new SimpleBody(s, new Glass(new PlainTexture(new Color3f(0.9f, 0.1f, 0.1f)))),
//											new SimpleBody(s, new Glass(new CheckerBoardTexture())),
//											new SimpleBody(s, new Mirror(new PlainTexture (new Color3f(0.65f, 0.65f, 0.65f)))),
//											new SimpleBody(s, new Phong(new PlainTexture (new Color3f(0.95f, 0.95f, 0.95f)), 1e3, 0.5, 0.5)),
//											new SimpleBody(p, new Metal(new PlainTexture(new Color3f(0.25f, 0.40f, 0.25f)), 0.5)),
//											new SimpleBody(p, new Metal(new CheckerBoardTexture(0.5), 0.5)),
											new SimpleBody(p, new Matte(new ImageTexture(Main.class.getResource("/textures/1024/Wood 2.png"), 0.2))),
//											new SimpleBody(p, new Mirror(new Color3f(0.25f, 0.40f, 0.25f), 0.15)),
											new SimpleBody(t, new PolishedMarble(new PlainTexture(new Color3f(0.85f,0.01f,0.01f)))),
//											new SimpleBody(t, new PolishedMarble(new CheckerBoardTexture(0.1))),
											new SimpleBody(t2, new Mirror(new PlainTexture(new Color3f(0.85f,0.85f,0.85f)))),
//											new SimpleBody(scn, new PolishedMarble(new PlainTexture(new Color3f(0.9f, 0.1f, 0.1f)))),
//											new UniformCompoundBody(new Shape[]{scn, scv}, new Glass(new PlainTexture(new Color3f(0.95f, 0.05f, 0.05f))))

	};

	//Camera
	final static Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480, 5.6, spherePos, AASamples);
//	final static Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 1366, 768, 5.6, spherePos, AASamples);
//	final static Camera camera = new PinholeCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480);
	
	public SampleScene() {
		super(lights,bodies,camera, AASamples);
	}
}
