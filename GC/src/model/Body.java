package model;

import model.trees.Traceable;

public interface Body extends Traceable {
	public IntersectionContext trace(Ray ray);
}
