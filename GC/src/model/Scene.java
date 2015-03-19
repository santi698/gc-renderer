package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import util.Vectors;

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
	public BufferedImage render() {
		int[] rgbArray = new int[getCamera().getXRes()*getCamera().getYRes()];
		Vector3d x = getCamera().getRight();
		Vector3d y = Vectors.scale(getCamera().getUp(), -1);
		Vector3d center = Vectors.scale(getCamera().getDirection(), getCamera().getDistanceToCamera());
		
		double width = 2*getCamera().getDistanceToCamera()*Math.tan((getCamera().getHorizFOV()/360)*2*Math.PI);
		double height = 2*getCamera().getDistanceToCamera()*Math.tan((getCamera().getVertFOV()/360)*2*Math.PI);
		Vector3d hwX = Vectors.scale(x, width/2);
		Vector3d hwY = Vectors.scale(y, height/2);
		Vector3d position = Vectors.sub(Vectors.sub(center, hwX), hwY);
		
		double xStep = width/getCamera().getXRes();
		double yStep = height/getCamera().getYRes();
		
		for (int i = 0; i < getCamera().getXRes(); i++) {
			for (int j = 0; j < getCamera().getYRes(); j++) {
				Vector3d disX = Vectors.scale(x, i*xStep);
				Vector3d disY = Vectors.scale(y, j*yStep);
				position.add(disX);
				position.add(disY);
				Vector3d direction = Vectors.normalize(Vectors.sub(position, getCamera().getPosition()));
				Ray ray = new Ray(direction, getCamera().getPosition());
				Body closestBody = null;
				IntersectionContext effectiveIC = null;
				double minDistance = Double.MAX_VALUE;
				for (Body body : getObjects()) {
					IntersectionContext ic = body.getShape().intersect(ray);
					if (ic.getHit()) {
						double distance = getCamera().getPosition().distance(ic.getIntersectionPoint());
						if (distance < minDistance) {
							minDistance = distance;
							closestBody = body;
							effectiveIC = ic;
						}
					}
				}
				//Calcular color rgb y ponerlo en el array en la posicion i+j*width
				if (closestBody != null) {
					Color c = closestBody.getColor();
					// FIXME Luz puntual hardcodeada
					double factor = Math.abs(effectiveIC.getNormal().dot(Vectors.normalize(Vectors.sub(effectiveIC.getIntersectionPoint(),new Vector3d(1e-3,0,0)))));
					Vector3f d = new Vector3f(c.getRed()*(float)factor/255, c.getGreen()*(float)factor/255,c.getBlue()*(float)factor/255);
					d.clampMax(1);
					rgbArray[i+j*getCamera().getXRes()] = new Color3f(d).get().getRGB();
				}
				else
					rgbArray[i+j*getCamera().getXRes()] = Color.BLACK.getRGB();
			}
		}
		BufferedImage img = new BufferedImage(getCamera().getXRes(), getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, getCamera().getXRes(), getCamera().getYRes(), rgbArray, 0, 0);
		return img;
	}
}
