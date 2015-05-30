package model.shapes;

import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Triangle extends Shape {
	private Point3d p1;

	private Point2d uv;
	
	private Vector3d d1;
	private Vector3d d2;
	private Vector3d normal;
	
	public BoundingBox bbox;
	
	public Triangle(Point3d p1, Point3d p2, Point3d p3) {
		super(new Vector3d(), new Vector3d(), 1);
		this.p1 = new Point3d(p1);
		this.d1 = Vectors.sub(p2, p1);
		this.d2 = Vectors.sub(p3, p1);	
		this.normal = Vectors.normalize(Vectors.cross(d1, d2));
		
		double minX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double maxX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double minY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double maxY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double minZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		double maxZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		this.bbox = new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
	}
	
	

	public Triangle(int i, List<Integer> triindices, Point3d[] points, Vector3d[] normals, Point2d[] uv) {
		super(new Vector3d(), new Vector3d(), 1);
		
		p1 = points[triindices.get(3*i)];
		this.d1 = Vectors.sub(points[triindices.get(3*i+1)], p1);
		this.d2 = Vectors.sub(points[triindices.get(3*i+2)], p1);
		
		//TODO usa solo la normal de un vertice
		this.normal = normals[triindices.get(3*i)];
		
		//TODO usa solo un uv
		if(uv != null)
			this.uv = uv[triindices.get(2*i)];
		
		Point3d p2 = points[triindices.get(3*i+1)];
		Point3d p3 = points[triindices.get(3*i+2)];

		double minX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double maxX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double minY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double maxY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double minZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		double maxZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		this.bbox = new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
	}




	@Override
	public IntersectionContext trace(Ray ray) {
		Vector3d normal = new Vector3d(this.normal);
		Vector3d P = Vectors.cross(ray.getDirection(), d2);
		double det = P.dot(d1);
		if (det > -EPS && det < EPS) return IntersectionContext.noHit();
		double invDet = 1/det;
		Vector3d T = Vectors.sub(ray.getOrigin(), p1);
		double u = T.dot(P)*invDet;
		if (u < 0 || u > 1) 
			return IntersectionContext.noHit();
		Vector3d Q = Vectors.cross(T,d1);
		double v = ray.getDirection().dot(Q)*invDet;
		if (v < 0 || v > 1 || u + v > 1) 
			return IntersectionContext.noHit();
		double t = d2.dot(Q)*invDet;
		if (t > EPS) {
			if (normal.dot(ray.getDirection()) > EPS)
				normal.negate();
			
			return new IntersectionContext(t, normal, ray, true, u, v);
		}
		return IntersectionContext.noHit();
	}
	
	public BoundingBox getBoundingBox(){
		return bbox;
	}

}
