package model.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.trees.KDNode;

public class Mesh extends Shape {

	KDNode kdnode = null;
	BoundingBox bbox = new BoundingBox(0,0,0,0,0,0);
	
	public Mesh(Matrix4d transform, List<Integer> triindices, List<Double> P, List<Double> N , List<Float> UVs) {
		super(new Vector3d(), new Vector3d(), 1);
		this.transform(transform);
		
		List<Shape> triangles = new ArrayList<Shape>();

		Point3d[] points = new Point3d[P.size()/3];
		for(int i = 0; i < P.size()/3; i++)
			points[i] = new Point3d(P.get(3*i),P.get(3*i+1),P.get(3*i+2));
		
		Vector3d[] normals = new Vector3d[N.size()/3];
		for(int i = 0; i < N.size()/3; i++)
			normals[i] = new Vector3d(N.get(3*i),N.get(3*i+1),N.get(3*i+2));
		
		Point2d[] uv = null;
		
		if(UVs != null){
			 uv = new Point2d[UVs.size()/2];
			 for(int i = 0; i < UVs.size()/2; i++)
				uv[i] = new Point2d(P.get(2*i),P.get(2*i+1));
		}
		
		Triangle t = null;
		for(int i = 0; i < triindices.size()/3; i++){
			t = new Triangle(i,triindices,points,normals,uv);
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
