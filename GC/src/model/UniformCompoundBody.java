package model;

import model.materials.Material;
import model.shapes.BoundingBox;
import model.shapes.Shape;

public class UniformCompoundBody implements Body {
	Shape[] shapes;
	Material material;
	public UniformCompoundBody(Shape[] shapes, Material material) {
		this.shapes = shapes;
		this.material = material;
	}
	@Override
	public IntersectionContext trace(Ray ray) {
		IntersectionContext effectiveIc = IntersectionContext.noHit();
		double minT = Double.MAX_VALUE;
		for (Shape shape : shapes) {
			IntersectionContext ic = shape.trace(ray);
			if (ic.getT() < minT) {
				minT = ic.getT();
				effectiveIc = ic;
			}
		}
		effectiveIc.setMaterial(this.material);
		return effectiveIc;
	}

	public BoundingBox getBoundingBox() {
		BoundingBox bbox = new BoundingBox(0,0,0,0,0,0);
		
		for(Shape s: shapes)
			bbox.expand(s.getBoundingBox());
		
		return bbox;
	}
	@Override
	public boolean intersectsBox(BoundingBox box) {
		return getBoundingBox().intersectsBox(box);
	}
}