package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import model.bodies.Body;
import model.light.Light;
import util.Vectors;

public class RayTracer {
	
	private Vector3d position;
	private Vector3d x, y;
	private double xStep, yStep;
	private Scene scene;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	
	public void setUp() {
		x = scene.getCamera().getRight();
		y = Vectors.scale(scene.getCamera().getUp(), -1);
		Vector3d center = Vectors.add(scene.getCamera().getPosition(), Vectors.scale(scene.getCamera().getDirection(), scene.getCamera().getDistanceToCamera()));
		
		double width = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getHorizFOV()/2/360)*2*Math.PI);
		double height = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getVertFOV()/2/360)*2*Math.PI);
		Vector3d hwX = Vectors.scale(x, width/2);
		Vector3d hwY = Vectors.scale(y, height/2);
		position = Vectors.sub(Vectors.sub(center, hwX), hwY);
		xStep = width/scene.getCamera().getXRes();
		yStep = height/scene.getCamera().getYRes();
	}
	
	public BufferedImage render() {
		int[] rgbArray = new int[scene.getCamera().getXRes()*scene.getCamera().getYRes()];
		
		setUp();
		Vector3d disY = Vectors.scale(y, yStep);
		Vector3d disX = Vectors.scale(x, xStep);
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				Vector3d direction = Vectors.normalize(Vectors.sub(position, scene.getCamera().getPosition()));
				if (i+j*scene.getCamera().getXRes() == 260000)
					System.out.println("");
				Ray ray = new Ray(direction, scene.getCamera().getPosition());
				rgbArray[i+j*scene.getCamera().getXRes()] = traceRay(ray);
				position.add(disY);
			}
			position.add(disX);
		}
		BufferedImage image = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, scene.getCamera().getXRes(), scene.getCamera().getYRes(), rgbArray, 0, 0);
		return image;
	}
	
	public int traceRay(Ray ray) {
		Body closestBody = null;
		IntersectionContext effectiveIC = null;
		double minDistance = Double.MAX_VALUE;
		for (Body body : scene.getObjects()) {
			
			IntersectionContext ic = body.getShape().intersect(ray);
			if (ic.getHit()) {
				double distance = scene.getCamera().getPosition().distance(ic.getIntersectionPoint());
				if (distance < minDistance) {
					minDistance = distance;
					closestBody = body;
					effectiveIC = ic;
				}
			}
		}
		if (closestBody != null) {
			Color3f c = closestBody.getMaterial().getColor();
			return calculateColor(effectiveIC, c).get().getRGB();
		}
		else
			return Color.BLACK.getRGB();
	}
	
	private double calculateLightFactor(IntersectionContext ic) {
		double factor = 1;
		for (Light light : scene.getLights()) {
			if (light.isVisible(ic.getIntersectionPoint(), scene)>0)
				factor *= light.getIntensity(ic);
		}
//		return factor;
		return 1;
	}
	
	private Color3f calculateColor(IntersectionContext ic, Color3f bodyColor) {
		//FIXME esto esta mal
		double factor = calculateLightFactor(ic);
		Color3f d = new Color3f(bodyColor);
		d.scale((float)factor);
		
		return d;
	}
}