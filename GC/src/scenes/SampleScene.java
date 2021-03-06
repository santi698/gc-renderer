package scenes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import application.Main;
import static util.Vectors.*;
import model.Body;
import model.SimpleBody;
import model.UniformCompoundBody;
import model.cameras.Camera;
import model.cameras.PinholeCamera;
import model.cameras.ThinLensCamera;
import model.light.AmbientLight;
import model.light.DirectionalLight;
import model.light.Light;
import model.light.PointLight;
import model.light.SpotLight;
import model.materials.CookTorrance;
import model.materials.Glass;
import model.materials.Matte2;
import model.materials.PureDiffuse;
import model.materials.Material;
import model.materials.Matte;
import model.materials.Metal;
import model.materials.Mirror;
import model.light.SpotLight;
import model.materials.DiffuseAndSpecular;
import model.materials.PolishedMarble;
import model.shapes.BoundingBox;
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
import model.trees.Traceable;

@SuppressWarnings("unused")
public class SampleScene implements Scene {
	final static int AASamples = Main.AASamples;
	final static Color3f sunsetColor = new Color3f(1,0.85f,0.66f);
	final static Point3d spherePos = new Point3d(0, 0.05, 0.5);
	//Lights
	List<Light> lights = Arrays.asList(new Light[] {
						new PointLight(new Point3d(-1, 0.5, 1), new Color3f(0.8f,0.8f,1f), 0.001),
						new PointLight(new Point3d(3, 1, 8), new Color3f(0.8f,0.8f,1f), 1),
						new SpotLight(new Point3d(0,2,0.3), new Color3f(1,1f,1f), 0.05, spherePos, Math.toRadians(45)),
						new SpotLight(new Point3d(0.5,2,-1), new Color3f(0.9f,0.9f,1), 0.04, spherePos, Math.toRadians(30)),
						new DirectionalLight(new Vector3d(0,-0.1,1), sunsetColor, 0.5	)
						});
	
	//Bodies
	final static SolidSphere s2 = new SolidSphere(new Point3d(0.175, 0.04, 0.5), 0.04);
	final static SolidSphere s = new SolidSphere(spherePos, 0.05);
	final static SolidSphere s3 = new SolidSphere(new Point3d(), 10);
	final static Plane p = new Plane(new Vector3d(0,1,0), new Point3d(0, 0, 0.5)); //Plano horizontal (piso)
	final static Triangle t = new Triangle(new Point3d(0.05,0.15,0.75), new Point3d(-0.15,0,0.5), new Point3d(0.15,0,0.8)); //Triángulo en el piso
	final static BoundingBox box = new BoundingBox(0,0,0,0.1,0.1,0.1);

	List<Body> bodies = Arrays.asList(new Body[] {
//											new SimpleBody(s2, new PolishedMarble(new CheckerBoardTexture(0.1))),
											new SimpleBody(s2, new CookTorrance(new PlainTexture(0.99f,0.5f,0.2f), 0, 0, 0.01, 1.5)),
//											new SimpleBody(s, new Metal(new Color3f(0.99f,0.99f,0.05f), 200)),
//											new SimpleBody(s, new Metal(new ImageTexture("textures/2400/earth_night.jpg"), 0.4)),
//											new SimpleBody(s, new Matte2(new CheckerBoardTexture(0.1), 0.1, 0.85)),
//											new SimpleBody(s, new Glass(new PlainTexture(0.9f, 0.1f, 0.1f))),
											new SimpleBody(s, new CookTorrance(new PlainTexture(0.99f,0f,0.1f), 0.5, 0, 0.01, 1.5)),
//											new SimpleBody(s, new Mirror(new PlainTexture (new Color3f(0.65f, 0.65f, 0.65f)))),
//											new SimpleBody(s, new Phong(new PlainTexture (new Color3f(0.95f, 0.95f, 0.95f)), 1e3, 0.5, 0.5)),
//											new SimpleBody(p, new Metal(new PlainTexture(new Color3f(0.25f, 0.40f, 0.25f)), 0.5)),
//											new SimpleBody(p, new Metal(new CheckerBoardTexture(0.5), 0.5)),
											new SimpleBody(p, new Matte2(new ImageTexture(Main.class.getResource("/textures/1024/Wood 2.png"), 0.2), 0, 0.75)),
//											new SimpleBody(p, new Mirror(new Color3f(0.25f, 0.40f, 0.25f), 0.15)),
											new SimpleBody(t, new Mirror(new PlainTexture(0.85f,0.01f,0.01f), 0.85f)),
//											new SimpleBody(t, new Metal2(new PlainTexture(new Color3f(173f/255f, 178f/255f, 189f/255f)),1e-100)),
//											new SimpleBody(t2, new Mirror(new PlainTexture(new Color3f(0.85f,0.85f,0.85f)))),
//											new SimpleBody(scn, new PolishedMarble(new PlainTexture(new Color3f(0.9f, 0.1f, 0.1f)))),
	});

	//Camera
//	Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480, 5.6, spherePos, 1);
//	Camera camera = new ThinLensCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 1376, 768, 5.6, spherePos, AASamples);
	Camera camera = new PinholeCamera(new Point3d(-0.2, 0.1, 0.3), spherePos, new Vector3d(0,1,0), 0.018, 640, 480);
	public SampleScene() {}
	@Override
	public Camera getCamera() {
		return camera;
	}
	@Override
	public List<Light> getLights() {
		return lights;
	}
	@Override
	public List<Body> getObjects() {
		return bodies;
	}
}
