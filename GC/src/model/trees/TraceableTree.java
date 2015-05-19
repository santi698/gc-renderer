package model.trees;

import java.util.Collection;

import model.IntersectionContext;
import model.Ray;

public abstract class TraceableTree {
	public abstract void add(Traceable object);
	public void addAll(Collection<Traceable> objects) {
		objects.forEach((o)->add(o));
	}
	public void addAll(Traceable[] objects) {
		for (Traceable o : objects)
			add(o);
	}
	public abstract IntersectionContext trace(Ray ray);
}
