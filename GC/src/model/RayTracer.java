package model;

import java.awt.image.BufferedImage;

import javax.vecmath.Vector3d;

import util.Vectors;

public class RayTracer {
	
	private Vector3d x, y, z;
	private Scene scene;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	
	public void setUp() {
		x = scene.getCamera().getRight();
		y = Vectors.scale(scene.getCamera().getUp(), 1);
		z = scene.getCamera().getDirection();
		
//		double width = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getHorizFOV()/2/360)*2*Math.PI);
//		double height = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getVertFOV()/2/360)*2*Math.PI);
	}
	
	public BufferedImage render() {
		int[] rgbArray = new int[scene.getCamera().getXRes()*scene.getCamera().getYRes()];
		setUp();
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				Ray ray = rayThroughPixel(i, j);
				rgbArray[i+j*scene.getCamera().getXRes()] = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects()).get().getRGB();
			}
		}
		BufferedImage image = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, scene.getCamera().getXRes(), scene.getCamera().getYRes(), rgbArray, 0, 0);
		return image;
	}

	private Ray rayThroughPixel(int x, int y) {
		//FIXME está mal!!!
		Vector3d xDir = (Vectors.scale(this.x, (x - scene.getCamera().getXRes()/2)));
		Vector3d yDir = (Vectors.scale(this.y, (y - scene.getCamera().getYRes()/2)));
		Vector3d zDir = Vectors.scale(z, scene.getCamera().getDistanceToCamera());
		Vector3d direction = Vectors.normalize(Vectors.add(xDir, Vectors.add(yDir, zDir)));
		return new Ray(direction, scene.getCamera().getPosition());
	}
}