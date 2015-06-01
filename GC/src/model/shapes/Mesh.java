package model.shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.trees.KDNode;
import model.trees.Traceable;

public class Mesh extends Shape {

	KDNode kdnode = null;
	
	BoundingBox bbox = new BoundingBox(0,0,0,0,0,0);
	
	public Mesh(Matrix4d transform, List<Integer> triindices, List<Double> P, List<Double> N , List<Float> UVs) {
		super(new Vector3d(), new Vector3d(), 1);
		this.transform(transform);
		
		
		Traceable[] triangles = new Traceable[triindices.size()/3];
		BoundingBox[] boxes = new BoundingBox[triindices.size()/3];

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
				uv[i] = new Point2d(UVs.get(2*i),UVs.get(2*i+1));
		}
		
		Triangle t = null;
		for(int i = 0; i < triindices.size()/3; i++){
			t = new Triangle(i,triindices,points,normals,uv);
			bbox.expand(t.getBoundingBox());
			triangles[i] = t;
			boxes[i] = t.bbox;
		}
		
				
		kdnode = new KDNode(bbox,triangles,boxes);
		
		//this.kdnode = new DummyTree();
		//kdnode.addAll(triangles);
	}

	@Override
	public IntersectionContext trace(Ray ray) {
		
		Ray localRay = new Ray(toLocal(ray.getDirection()), toLocal(ray.getOrigin()));
		
		KDNode node = kdnode.findFirstNode(localRay);
		while (node != null) {
			Collection<Traceable> objects = Arrays.asList(node.getObjects());
			Traceable obj = getFirstIntersectedObject(localRay, objects);
			if (obj != null)
				return obj.trace(localRay);
			node = node.findNextNode(localRay);
		}
		return IntersectionContext.noHit();
		
	}

	@Override
	public BoundingBox getBoundingBox() {
		return bbox;
	}

	@Override
	public boolean intersectsBox(BoundingBox box) {
		return box.intersects(bbox);
	}
	
	
	public Traceable getFirstIntersectedObject(Ray ray,Collection<Traceable> objs) {
		Traceable nearest = null;
		IntersectionContext currentIntersection, nearestIntersection = null;
		double nearestDistance = Double.MAX_VALUE;

		for (Traceable o : objs) {
			if ((currentIntersection = o.trace(ray)) == IntersectionContext.noHit())
				continue;

			Vector3d aux = new Vector3d(currentIntersection.intersectionPoint);
			double currentDistance;
			aux.sub(ray.getOrigin());
			currentDistance = aux.length();

			if (nearestIntersection == null
					|| currentDistance < nearestDistance) {
				nearest = o;
				nearestIntersection = currentIntersection;
				nearestDistance = currentDistance;
			}
		}
		if (nearestIntersection == null)
			return null;

		return nearest;
	}

}
