package model;

public interface Shape {
	//Necesario para evitar self-occlusion.
	public static double EPS = 0.000001;
	public IntersectionContext intersect(Ray ray);
}
