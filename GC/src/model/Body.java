package model;

import model.shapes.BoundingBox;
import model.trees.Traceable;

public interface Body extends Traceable {
	public IntersectionContext trace(Ray ray);
	
	public BoundingBox getBoundingBox();
}
