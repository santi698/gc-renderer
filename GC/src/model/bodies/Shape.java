package model.bodies;

import model.IntersectionContext;
import model.Ray;

public interface Shape {
	//Necesario para evitar self-occlusion.
	public static double EPS = 1e-4;
	public IntersectionContext intersect(Ray ray);
}
