package model.trees;

import java.util.List;

import model.IntersectionContext;
import model.Ray;

public abstract class TraceableTree {
	public abstract void add(Traceable object);
	public void addAll(List<? extends Traceable> objects) {
		objects.forEach((o)->add(o));
	}
	public abstract IntersectionContext trace(Ray ray);
}