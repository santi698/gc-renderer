package model.trees;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;

public interface Traceable {
	public IntersectionContext trace(Ray ray);
	public BoundingBox getBoundingBox();
	public boolean intersectsBox(BoundingBox b8);
}
