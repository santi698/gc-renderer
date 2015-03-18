package model;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

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
		Vector3d x = getCamera().getRight();
		Vector3d y = new Vector3d(getCamera().getUp());
		y.scale(-1);
		Point3d center = new Point3d(getCamera().getDirection());
		center.scale(getCamera().getDistanceToCamera());
		
		double width = 2*getCamera().getDistanceToCamera()*Math.tan(getCamera().getHorizFOV());
		double height = 2*getCamera().getDistanceToCamera()*Math.tan(getCamera().getHorizFOV());
		Vector3d hwX = new Vector3d(x);
		hwX.scale(width/2);
		Vector3d hwY = new Vector3d(x);
		hwY.scale(height/2);
		Point3d location = new Point3d(center);
		location.sub(hwX);
		location.sub(hwY);
		
		double xStep = width/getCamera().getXRes();
		double yStep = height/getCamera().getYRes();
		
		for (int i = 0; i < getCamera().getXRes(); i++) {
			for (int j = 0; j < getCamera().getYRes(); j++) {
				Vector3d disX = new Vector3d(x); disX.scale(i*xStep);
				Vector3d disY = new Vector3d(y); disY.scale(j*yStep);
				location.add(disX);
				location.add(disY);
				Vector3d direction = new Vector3d(location);
				direction.sub(getCamera().getPosition());
				direction.normalize(); 
				Ray ray = new Ray(direction, getCamera().getPosition());
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
