package model.trees;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import model.Body;
import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;
import model.shapes.Shape;
import model.shapes.Triangle;

public class KDNode {

	public BoundingBox bbox;
	public KDNode left;
	public KDNode right;
	List<Shape> bodies;

	public KDNode() {
		bodies = new ArrayList<Shape>();
	}

	public KDNode build(List<Shape> shae, int depth) {
		KDNode node = new KDNode();
		node.bodies = shae;
		node.left = null;
		node.right = null;
		node.bbox = new BoundingBox(0,0,0,0,0,0);
		if (shae.size() == 0)
			return node;
		if (shae.size() == 1) {
			node.bbox = shae.get(0).getBoundingBox();
			node.left = new KDNode();
			node.right = new KDNode();
			return node;
		}

		// get a bounding box surrounding all the bodies
		node.bbox = shae.get(0).getBoundingBox();
		for (int i = 1; i < shae.size(); i++) {
			node.bbox.expand(shae.get(i).getBoundingBox());
		}

		Point3d midpt = new Point3d(0, 0, 0);
		for (int i = 0; i < shae.size(); i++) {
			// find midpoint of all bodies
			Point3d aux = shae.get(i).getBoundingBox().getMidpoint();
			aux.scale(1.0 / shae.size());
			midpt.add(aux);
		}

		List<Shape> left_shae = new ArrayList<Shape>();
		List<Shape> right_shae = new ArrayList<Shape>();
		int axis = node.bbox.longestAxis();
		for (int i = 0; i < shae.size(); i++) {
			// split bodies based on their midpoints side of avg in longest axis
			
			Shape auxTri = shae.get(i);
			switch (axis) {
			case 0:
				if(midpt.x >= auxTri.getBoundingBox().getMidpoint().x)
					right_shae.add(auxTri);
				else
					left_shae.add(auxTri);
				break;
			case 1:
				if(midpt.y >= auxTri.getBoundingBox().getMidpoint().y)
					right_shae.add(auxTri);
				else
					left_shae.add(auxTri);
				break;
			case 2:
				if(midpt.z >= auxTri.getBoundingBox().getMidpoint().z)
					right_shae.add(auxTri);
				else
					left_shae.add(auxTri);
				break;
			}
		}

		if (left_shae.size() == 0 && right_shae.size() > 0)
			left_shae = right_shae;
		if (right_shae.size() == 0 && left_shae.size() > 0)
			right_shae = left_shae;

		// if 50% of triangles match, don't subdivide any more
		int matches = 0;
		for (int i = 0; i < left_shae.size(); i++) {
			for (int j = 0; j < right_shae.size(); j++) {
				if (left_shae.get(i) == right_shae.get(j))
					matches++;
			}
		}

		if ((float) matches / left_shae.size() < 0.5
				&& (float) matches / right_shae.size() < 0.5) {
			// recurse down left and right sides
			node.left = build(left_shae, depth + 1);
			node.right = build(right_shae, depth + 1);
		} else {
			node.left = new KDNode();
			node.right = new KDNode();
		}
		return node;
	}

	public IntersectionContext hit(KDNode node, Ray ray){
		//check if the ray intersects the bounding box of the given node
		if (node.bbox.trace(ray) != IntersectionContext.noHit()){
			//if either child still has triangles, recurse down both sides and check for intersections
			if (node.left.bodies.size() > 0 || node.left.bodies.size() > 0){
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
					if (ic.hit){
						return ic;
					}
				}
				return IntersectionContext.noHit();
			}
		}
		return IntersectionContext.noHit();
	}
}