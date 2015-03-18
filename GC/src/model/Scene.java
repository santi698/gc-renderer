package model;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javafx.geometry.Point3D;

public class Scene {
	private Light[] lights;
	private Body[] objects;
	private Camera camera;
	public Scene(Light[] lights, Body[] objects, Camera camera) {
		super();
		this.lights = lights;
		this.objects = objects;
		this.camera = camera;
	}
	public Light[] getLights() {
		return lights;
	}
	public Body[] getObjects() {
		return objects;
	}
	public Camera getCamera() {
		return camera;
	}
	public Image render() {
		int[] rgbArray = new int[getCamera().getXRes()*getCamera().getYRes()];
		Point3D x = getCamera().getRight();
		Point3D y = getCamera().getUp().multiply(-1);
		Point3D center = getCamera().getDirection().multiply(getCamera().getDistanceToCamera());
		
		double width = 2*getCamera().getDistanceToCamera()*Math.tan(getCamera().getHorizFOV());
		double height = 2*getCamera().getDistanceToCamera()*Math.tan(getCamera().getHorizFOV());
		
		Point3D topLeft = center.subtract(x.multiply(width/2)).subtract(y.multiply(height/2));
		
		double xStep = width/getCamera().getXRes();
		double yStep = height/getCamera().getYRes();
		
		for (int i = 0; i < getCamera().getXRes(); i++) {
			for (int j = 0; j < getCamera().getYRes(); j++) {
				
				Point3D location = topLeft.add(x.multiply(i*xStep)).add(y.multiply(j*yStep));
				Ray ray = new Ray(location.subtract(getCamera().getPosition()).normalize(), getCamera().getPosition());
				for (Body body : getObjects()) {
					IntersectionContext ic = body.getShape().intersect(ray);
					//Calcular color rgb y ponerlo en el array en la posicion i+j*width
					rgbArray[i+j*getCamera().getXRes()] = body.getColor().getRGB(); 
					
				}
			}
		}
		BufferedImage img = new BufferedImage(getCamera().getXRes(), getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, getCamera().getXRes(), getCamera().getYRes(), rgbArray, 0, 0);
		return img;
	}
}
