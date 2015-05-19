package model.trees;

import java.util.LinkedList;
import java.util.List;

import model.Body;
import model.IntersectionContext;
import model.Ray;

public class DummyTree extends TraceableTree {
	List<Traceable> objects;
	public DummyTree() {
		objects = new LinkedList<Traceable>();
	}
	public DummyTree(Body[] objects) {
		this();
		addAll(objects);
	}
	@Override
	public void add(Traceable object) {
		objects.add(object);
	}

	@Override
	public IntersectionContext trace(Ray ray) {
		return ray.trace(objects);
	}

}
