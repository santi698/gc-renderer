package model.trees;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;

public class KDNode extends TraceableTree{

	public BoundingBox bbox;
	public KDNode left;
	public KDNode right;
	List<Traceable> bodies = new LinkedList<Traceable>();

	public KDNode() {
		bodies = new LinkedList<Traceable>();
	}

	public KDNode build(List<? extends Traceable> shapes, int depth) {
		this.bodies = new LinkedList<Traceable>(shapes);
		this.left = null;
		this.right = null;
		this.bbox = new BoundingBox(0,0,0,0,0,0);
		if (shapes.size() == 0)
			return this;
		if (shapes.size() == 1) {
			this.bbox = new BoundingBox(shapes.get(0).getBoundingBox());
			this.left = new KDNode();
			this.right = new KDNode();
			this.left.bodies = new LinkedList<Traceable>();
			this.right.bodies = new LinkedList<Traceable>();
			return this;
		}

		// get a bounding box surrounding all the bodies
		this.bbox = new BoundingBox(shapes.get(0).getBoundingBox());
		for (int i = 1; i < shapes.size(); i++) {
			this.bbox.expand(shapes.get(i).getBoundingBox());
		}

		if(depth < 3)
		System.out.println("N Depth:" + depth + " #Shape:" + shapes.size() + " bbox [("+this.bbox.x0+ ","+this.bbox.x1 + "),("+this.bbox.y0 + ","+this.bbox.y1+"),("+this.bbox.z0+","+this.bbox.z1+")]") ;
		
		Point3d midpt = new Point3d(0, 0, 0);
		for (int i = 0; i < shapes.size(); i++) {
			// find midpoint of all bodies
			Point3d aux = shapes.get(i).getBoundingBox().getMidpoint();
			aux.scale(1.0 / shapes.size());
			midpt.add(aux);
		}

		List<Traceable> left_shapes = new LinkedList<Traceable>();
		List<Traceable> right_shapes = new LinkedList<Traceable>();
		int axis = this.bbox.longestAxis();
		
		
		for (int i = 0; i < shapes.size(); i++) {
			// split bodies based on their midpoints side of avg in longest axis
			
			Traceable auxTri = shapes.get(i);

			switch (axis) {
			case 0:
				if(midpt.x >= auxTri.getBoundingBox().getMidpoint().x){
					right_shapes.add(auxTri);
				}else{
					left_shapes.add(auxTri);
					}
				break;
			case 1:
				if(midpt.y >= auxTri.getBoundingBox().getMidpoint().y){
					right_shapes.add(auxTri);
				}else{
					left_shapes.add(auxTri);
				}
				break;
			case 2:
				if(midpt.z >= auxTri.getBoundingBox().getMidpoint().z){
					right_shapes.add(auxTri);
				}else{
					left_shapes.add(auxTri);
				}
				break;
			}
		}

		if (left_shapes.size() == 0 && right_shapes.size() > 0)
			left_shapes = right_shapes;
		if (right_shapes.size() == 0 && left_shapes.size() > 0)
			right_shapes = left_shapes;

		// if 50% of triangles match, don't subdivide any more
		int matches = 0;
		for (int i = 0; i < left_shapes.size(); i++) {
			for (int j = 0; j < right_shapes.size(); j++) {
				if (left_shapes.get(i) == right_shapes.get(j))
					matches++;
			}
		}

		if ((float) matches / left_shapes.size() < 0.5
				&& (float) matches / right_shapes.size() < 0.5) {
			// recurse down left and right sides
			this.left = build(left_shapes, depth + 1);
			this.right = build(right_shapes, depth + 1);
		} else {
			this.left = new KDNode();
			this.right = new KDNode();
			this.left.bodies = new LinkedList<Traceable>();
			this.right.bodies = new LinkedList<Traceable>();
		}
		return this;
	}

	public IntersectionContext hit(KDNode node, Ray ray){

		//check if the ray intersects the bounding box of the given node
		if (node.bbox.trace(ray) != IntersectionContext.noHit()){
			
			//if either child still has triangles, recurse down both sides and check for intersections
			if (!node.left.bodies.isEmpty() || !node.right.bodies.isEmpty()){
				IntersectionContext hitleft = hit(node.left, ray);
				IntersectionContext hitright = hit(node.right, ray);
				if(hitleft != IntersectionContext.noHit())
					return hitleft;
				else
					return hitright;
			}else{
				//we have reached a leaf
				for (int i=0; i < node.bodies.size(); i++){
					//if there's a hit, return 
					IntersectionContext ic = node.bodies.get(i).trace(ray);
					if (ic.getHit()){
						return ic;
					}
				}
				return IntersectionContext.noHit();
			}
		}
		return IntersectionContext.noHit();
	}
	public IntersectionContext trace(Ray ray) {
		return this.hit(this, ray);
	}

	@Override
	public void add(Traceable object) {
		bodies.add(object);
		build(bodies, 0);
	}
	@Override
	public void addAll(List<? extends Traceable> objects) {
		bodies = (List<Traceable>)objects;
		build(objects, 0);
	}
}