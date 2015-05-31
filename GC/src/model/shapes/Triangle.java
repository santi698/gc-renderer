package model.shapes;

import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import util.Vectors;

public class Triangle extends Shape {
	private boolean isMeshTriangle = false;
	private Vector3d[] normals = new Vector3d[3];
	private Point2d[] uvs = new Point2d[3];
	
	private Point3d p1;	
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
		isMeshTriangle = true;
		this.normals = new Vector3d[]{normals[triindices.get(3*i)], normals[triindices.get(3*i+1)], normals[triindices.get(3*i+2)]};
		this.uvs = new Point2d[] {uv[triindices.get(3*i)], uv[triindices.get(3*i+1)], uv[triindices.get(3*i+2)]};
		
		p1 = points[triindices.get(3*i)];
		this.d1 = Vectors.sub(points[triindices.get(3*i+1)], p1);
		this.d2 = Vectors.sub(points[triindices.get(3*i+2)], p1);
		this.normal = Vectors.normalize(Vectors.cross(d1, d2));
		
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
		System.out.println("SOMEONE IS TRYING TO HIT ME");
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
			Point2d uv = getGlobalUV(new Point2d(u,v));
			return new IntersectionContext(t, getNormal(new Point2d(u,v)), ray, true, uv.x, uv.y);
		}
		return IntersectionContext.noHit();
	}
	
	public BoundingBox getBoundingBox(){
		return bbox;
	}
	public Vector3d getNormal(Point2d uv) {
		if (isMeshTriangle) {
			Vector3d uv1 = Vectors.scale(normals[0], 1-uv.x-uv.y);
			Vector3d u2 = Vectors.scale(normals[1], uv.x);
			Vector3d v2 = Vectors.scale(normals[2], uv.y);
			return (Vectors.add(Vectors.add(uv1, u2), v2));
		}
		return normal;
	}
	public Point2d getGlobalUV(Point2d localUv) {
		if (isMeshTriangle) {
			Point2d uv1 = new Point2d(uvs[0]);
			Point2d u2 = new Point2d(uvs[1]);
			Point2d v2 = new Point2d(uvs[2]);
			uv1.scale(1-localUv.x-localUv.y);
			u2.scale(localUv.x);
			v2.scale(localUv.y);
			Point2d result = uv1;
			result.add(u2);
			result.add(v2);
			return result;
		}
		return localUv;
	}
}
