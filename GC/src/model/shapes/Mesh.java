package model.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.trees.KDNode;

public class Mesh extends Shape {

	KDNode kdnode = null;
	BoundingBox bbox = new BoundingBox(0,0,0,0,0,0);
	
	public Mesh(Matrix4d transform, List<Integer> triindices, List<Double> P, List<Float> UVs) {
		super(new Vector3d(), new Vector3d(), 1);
		this.transform(transform);
		
		List<Shape> triangles = new ArrayList<Shape>();
		
		
		for(int i = 0; i < triindices.size() / 9; i++){
			
			//Utilizar los UVs!
			Vector2d uv = new Vector2d(UVs.get(2*i), UVs.get(2*i+1));
			Triangle t = new Triangle(	new Point3d(P.get(triindices.get(9*i)),P.get(triindices.get(9*i+1)),P.get(triindices.get(9*i+2))),
										new Point3d(P.get(triindices.get(9*i+3)),P.get(triindices.get(9*i+4)),P.get(triindices.get(9*i+5))),
										new Point3d(P.get(triindices.get(9*i+6)),P.get(triindices.get(9*i+7)),P.get(triindices.get(9*i+8))));
			
			bbox.expand(t.getBoundingBox());
			
			triangles.add(t);
		}
		
		kdnode = new KDNode();
		kdnode.build(triangles,0);
	}

	@Override
	public IntersectionContext trace(Ray ray) {
		return kdnode.hit(kdnode, ray);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return bbox;
	}

}
