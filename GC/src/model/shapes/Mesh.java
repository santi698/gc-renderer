package model.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.trees.KDNode;

public class Mesh extends Shape {

	KDNode kdnode = null;
	
	public Mesh(Vector3d translation, Vector3d rotation, double scale, List<Integer> triindices, List<Double> P) {
		super(translation, rotation, scale);
		
		List<Triangle> triangles = new ArrayList<Triangle>();
		
		for(int i = 0; i < triindices.size(); i += 9){
			triangles.add(	new Triangle(new Point3d(P.get(triindices.get(i)),P.get(triindices.get(i+1)),P.get(triindices.get(i+2))),
							new Point3d(P.get(triindices.get(i+3)),P.get(triindices.get(i+4)),P.get(triindices.get(i+5))),
							new Point3d(P.get(triindices.get(i+6)),P.get(triindices.get(i+7)),P.get(triindices.get(i+8)))));
		}
		
		kdnode = new KDNode();
		kdnode.build(triangles,0);
	}

	@Override
	public IntersectionContext trace(Ray ray) {
		return kdnode.hit(kdnode, ray);
	}

}
