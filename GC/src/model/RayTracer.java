package model;

import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import static util.Vectors.*;

public class RayTracer {
	
	private Vector3d u, v, w;
	private Scene scene;
	private double pixelSize = 0.001;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	
	public void setUp() {
		w = scale(scene.getCamera().getDirection(), -1);
		u = normalize(cross(scene.getCamera().getUp(), w));
		v = cross(w, u);
	}
	
	public BufferedImage render() {
		BufferedImage bi = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		setUp();
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				Ray ray = rayThroughPixel(i, j);
				Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
				bi.setRGB(i, j, color.get().getRGB());
			}
		}
		return bi;
	}

	private Ray rayThroughPixel(int i, int j) {
		double x = pixelSize*(i - 0.5*scene.getCamera().getXRes());
		double y = pixelSize*(j - 0.5*scene.getCamera().getYRes());
		Vector3d direction = normalize(sub(add(scale(u, x), scale(v, y)), scale(w, scene.getCamera().getDistanceToCamera())));
		return new Ray(direction, scene.getCamera().getPosition());
	}
}