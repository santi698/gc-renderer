package model;

import model.shapes.Shape;

public class Body {
	private Shape shape;
	private Material material;
	public Body(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}
	public Shape getShape() {
		return shape;
	}
	public Material getMaterial() {
		return material;
	}
	public IntersectionContext intersect(Ray ray) {
		IntersectionContext ic = shape.intersect(ray);
		ic.setBody(this);
		return ic;
	}
}
