package model.trees;

import model.IntersectionContext;
import model.Ray;

public interface Traceable {
	public IntersectionContext trace(Ray ray);
}
