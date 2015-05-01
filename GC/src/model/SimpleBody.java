package model;

import model.materials.Material;
import model.shapes.Shape;

public class SimpleBody implements Body {
	private Shape shape;
	private Material material;
	public SimpleBody(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}
	public Material getMaterial() {
		return material;
	}
	public IntersectionContext intersect(Ray ray) {
		IntersectionContext ic = shape.intersect(ray);
		ic.setMaterial(this.getMaterial());
		return ic;
	}
}
