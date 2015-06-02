package model.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;

public class OctreeNode extends BoundingBox {

	public static final double TOLERANCE = 1e-12;
	
	private static final int MAX_OBJECTS_PER_NODE = 8;
	private static final int MAX_OCTREE_DEPTH = 8;

	private List<Traceable> shapes = new LinkedList<Traceable>();

	public OctreeNode(List<Traceable> shapes,BoundingBox bbox, int depth){
		super(bbox);
		if(shapes.size() > MAX_OBJECTS_PER_NODE || depth >= MAX_OCTREE_DEPTH){
			this.shapes = shapes;
			return;
		}

		List<Traceable> L1 = new ArrayList<Traceable>();
		List<Traceable> L2 = new ArrayList<Traceable>();
		List<Traceable> L3 = new ArrayList<Traceable>();
		List<Traceable> L4 = new ArrayList<Traceable>();
		List<Traceable> L5 = new ArrayList<Traceable>();
		List<Traceable> L6 = new ArrayList<Traceable>();
		List<Traceable> L7 = new ArrayList<Traceable>();
		List<Traceable> L8 = new ArrayList<Traceable>();
		
		double xavg = (bbox.x1 + bbox.x0) /2;
		double yavg = (bbox.y1 + bbox.y0) /2;
		double zavg = (bbox.z1 + bbox.z0) /2;
		
		BoundingBox B1 = new BoundingBox(bbox.x0, bbox.y0, bbox.z0, xavg, yavg, zavg);
		BoundingBox B2 = new BoundingBox(xavg, bbox.y0, bbox.z0, bbox.x1, yavg, zavg);
		BoundingBox B3 = new BoundingBox(xavg, yavg, bbox.z0, bbox.x1, bbox.y1, zavg);
		BoundingBox B4 = new BoundingBox(xavg, yavg, zavg, bbox.x1, bbox.y1, bbox.z1);
		BoundingBox B5 = new BoundingBox(bbox.x0, yavg, bbox.z0, xavg, bbox.y1, zavg);
		BoundingBox B6 = new BoundingBox(bbox.x0, yavg, zavg, xavg, bbox.y1, bbox.z1);
		BoundingBox B7 = new BoundingBox(bbox.x0, bbox.y0, zavg, xavg, yavg, bbox.z1);
		BoundingBox B8 = new BoundingBox(xavg, bbox.y0, zavg, bbox.x1, yavg, bbox.z1);

		for(Traceable t: shapes){
			if(t.intersectsBox(B1))
				L1.add(t);
			if(t.intersectsBox(B2))
				L2.add(t);
			if(t.intersectsBox(B3))
				L3.add(t);
			if(t.intersectsBox(B4))
				L4.add(t);
			if(t.intersectsBox(B5))
				L5.add(t);
			if(t.intersectsBox(B6))
				L6.add(t);
			if(t.intersectsBox(B7))
				L7.add(t);
			if(t.intersectsBox(B8))
				L8.add(t);
		}

		if(L1.size() != 0)
			shapes.add(new OctreeNode(L1,B1, depth + 1));
		if(L2.size() != 0)
			shapes.add(new OctreeNode(L2,B2, depth + 1));
		if(L3.size() != 0)
			shapes.add(new OctreeNode(L3,B3, depth + 1));
		if(L4.size() != 0)
			shapes.add(new OctreeNode(L4,B4, depth + 1));
		if(L5.size() != 0)
			shapes.add(new OctreeNode(L5,B5, depth + 1));
		if(L6.size() != 0)
			shapes.add(new OctreeNode(L6,B6, depth + 1));
		if(L7.size() != 0)
			shapes.add(new OctreeNode(L7,B7, depth + 1));
		if(L8.size() != 0)
			shapes.add(new OctreeNode(L8,B8, depth + 1));
	}

	public OctreeNode(List<Traceable> objects) {
		this(objects, computeBoundingBox(objects), 0);
	}

	private static BoundingBox computeBoundingBox(List<Traceable> objects) {
		BoundingBox box = new BoundingBox(0, 0, 0, 0, 0, 0);
		for (Traceable traceable : objects) {
			box.expand(traceable.getBoundingBox());
		}
		return box;
	}

	public IntersectionContext trace(Ray ray){
		double tMin = Double.MAX_VALUE;
		IntersectionContext res = IntersectionContext.noHit();
		for(Traceable t: shapes){
			IntersectionContext aux = t.trace(ray);
			if(aux != IntersectionContext.noHit()){
				if(aux.getT() < tMin) {
					tMin = aux.getT();
					res = aux;
				}
			}
		}
		return res;
	}

}