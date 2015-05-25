package model.trees;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;
import model.shapes.Triangle;

public class KDNode {

	public BoundingBox bbox;
	public KDNode left;
	public KDNode right;
	List<Triangle> triangles;

	public KDNode() {
		triangles = new ArrayList<Triangle>();
	}

	public KDNode build(List<Triangle> tris, int depth) {
		KDNode node = new KDNode();
		node.triangles = tris;
		node.left = null;
		node.right = null;
		node.bbox = new BoundingBox(0,0,0,0,0,0);
		if (tris.size() == 0)
			return node;
		if (tris.size() == 1) {
			node.bbox = tris.get(0).getBoundingBox();
			node.left = new KDNode();
			node.right = new KDNode();
			return node;
		}

		// get a bounding box surrounding all the triangles
		node.bbox = tris.get(0).getBoundingBox();
		for (int i = 1; i < tris.size(); i++) {
			node.bbox.expand(tris.get(i).getBoundingBox());
		}

		Point3d midpt = new Point3d(0, 0, 0);
		for (int i = 0; i < tris.size(); i++) {
			// find midpoint of all triangles
			Point3d aux = tris.get(i).getMidpoint();
			aux.scale(1.0 / tris.size());
			midpt.add(aux);
		}

		List<Triangle> left_tris = new ArrayList<Triangle>();
		List<Triangle> right_tris = new ArrayList<Triangle>();
		int axis = node.bbox.longestAxis();
		for (int i = 0; i < tris.size(); i++) {
			// split triangles based on their midpoints side of avg in longest axis
			
			Triangle auxTri = tris.get(i);
			switch (axis) {
			case 0:
				if(midpt.x >= auxTri.getMidpoint().x)
					right_tris.add(auxTri);
				else
					left_tris.add(auxTri);
				break;
			case 1:
				if(midpt.y >= auxTri.getMidpoint().y)
					right_tris.add(auxTri);
				else
					left_tris.add(auxTri);
				break;
			case 2:
				if(midpt.z >= auxTri.getMidpoint().z)
					right_tris.add(auxTri);
				else
					left_tris.add(auxTri);
				break;
			}
		}

		if (left_tris.size() == 0 && right_tris.size() > 0)
			left_tris = right_tris;
		if (right_tris.size() == 0 && left_tris.size() > 0)
			right_tris = left_tris;

		// if 50% of triangles match, don't subdivide any more
		int matches = 0;
		for (int i = 0; i < left_tris.size(); i++) {
			for (int j = 0; j < right_tris.size(); j++) {
				if (left_tris.get(i) == right_tris.get(j))
					matches++;
			}
		}

		if ((float) matches / left_tris.size() < 0.5
				&& (float) matches / right_tris.size() < 0.5) {
			// recurse down left and right sides
			node.left = build(left_tris, depth + 1);
			node.right = build(right_tris, depth + 1);
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
			if (node.left.triangles.size() > 0 || node.left.triangles.size() > 0){
				IntersectionContext hitleft = hit(node.left, ray);
				IntersectionContext hitright = hit(node.right, ray);
				if(hitleft != IntersectionContext.noHit())
					return hitleft;
				else
					return hitright;
			}else{
				//we have reached a leaf
				for (int i=0; i < node.triangles.size(); i++){
					//if there's a hit, return 
					IntersectionContext ic = node.triangles.get(i).trace(ray);
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