package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import model.bodies.Body;
import util.Vectors;

public class RayTracer {
	//TODO Modularizar este monstruo
	public BufferedImage render(Scene scene) {
		int[] rgbArray = new int[scene.getCamera().getXRes()*scene.getCamera().getYRes()];
		Vector3d x = scene.getCamera().getRight();
		Vector3d y = Vectors.scale(scene.getCamera().getUp(), -1);
		Vector3d center = Vectors.scale(scene.getCamera().getDirection(), scene.getCamera().getDistanceToCamera());
		
		double width = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getHorizFOV()/2/360)*2*Math.PI);
		double height = 2*scene.getCamera().getDistanceToCamera()*Math.tan((scene.getCamera().getVertFOV()/2/360)*2*Math.PI);
		Vector3d hwX = Vectors.scale(x, width/2);
		Vector3d hwY = Vectors.scale(y, height/2);
		Vector3d position = Vectors.sub(Vectors.sub(center, hwX), hwY);
		
		double xStep = width/scene.getCamera().getXRes();
		double yStep = height/scene.getCamera().getYRes();
		
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				Vector3d disX = Vectors.scale(x, i*xStep);
				Vector3d disY = Vectors.scale(y, j*yStep);
				position.add(disX);
				position.add(disY);
				Vector3d direction = Vectors.normalize(Vectors.sub(position, scene.getCamera().getPosition()));
				Ray ray = new Ray(direction, scene.getCamera().getPosition());
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
				//Calcular color rgb y ponerlo en el array en la posicion i+j*width
				if (closestBody != null) {
					Color c = closestBody.getColor();
					// FIXME Luz puntual hardcodeada
					double factor = Math.abs(effectiveIC.getNormal().dot(Vectors.normalize(Vectors.sub(effectiveIC.getIntersectionPoint(),new Vector3d(1e-3,0,0)))));
					Vector3f d = new Vector3f(c.getRed()*(float)factor/255, c.getGreen()*(float)factor/255,c.getBlue()*(float)factor/255);
					d.clampMax(1);
					rgbArray[i+j*scene.getCamera().getXRes()] = new Color3f(d).get().getRGB();
				}
				else
					rgbArray[i+j*scene.getCamera().getXRes()] = Color.BLACK.getRGB();
			}
		}
		BufferedImage image = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, scene.getCamera().getXRes(), scene.getCamera().getYRes(), rgbArray, 0, 0);
		return image;
	}
}
