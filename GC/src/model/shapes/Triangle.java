package model.shapes;

import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.trees.OctreeNode;
import util.Vectors;

public class Triangle extends Shape {
	private boolean isMeshTriangle = false;
	private Mesh mesh;
	private Vector3d[] normals = new Vector3d[3];
	private Point2d[] uvs = new Point2d[3];
	
	private Point3d p1;
	private Point3d p2;
	private Point3d p3;
	private Vector3d d1;
	private Vector3d d2;
	private Vector3d normal;
	
	public BoundingBox bbox;
	
	public Triangle(Point3d p1, Point3d p2, Point3d p3) {
		super(new Vector3d(), new Vector3d(), 1);
		this.p1 = new Point3d(p1);
		this.p2 = new Point3d(p2);
		this.p3 = new Point3d(p3);
		this.d1 = Vectors.sub(p2, p1);
		this.d2 = Vectors.sub(p3, p1);	
		this.normal = Vectors.normalize(Vectors.cross(d1, d2));
		
		double minX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double maxX = Math.max(Math.max(p1.x, p2.x),p3.x);
		double minY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double maxY = Math.max(Math.max(p1.y, p2.y),p3.y);
		double minZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		double maxZ = Math.max(Math.max(p1.z, p2.z),p3.z);
		this.bbox = new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
	}
	
	

	public Triangle(int i, List<Integer> triindices, Point3d[] points, Vector3d[] normals, Point2d[] uv, Mesh mesh) {
		super(new Vector3d(), new Vector3d(), 1);
		isMeshTriangle = true;
		this.mesh = mesh;
		this.normals = new Vector3d[]{normals[triindices.get(3*i)], normals[triindices.get(3*i+1)], normals[triindices.get(3*i+2)]};
		this.uvs = new Point2d[] {uv[triindices.get(3*i)], uv[triindices.get(3*i+1)], uv[triindices.get(3*i+2)]};
		
		p1 = points[triindices.get(3*i)];
		p2 = points[triindices.get(3*i+1)];
		p3 = points[triindices.get(3*i+2)];
		this.d1 = Vectors.sub(points[triindices.get(3*i+1)], p1);
		this.d2 = Vectors.sub(points[triindices.get(3*i+2)], p1);
		this.normal = Vectors.normalize(Vectors.cross(d1, d2));

		double minX = Math.min(Math.min(p1.x, p2.x),p3.x);
		double maxX = Math.max(Math.max(p1.x, p2.x),p3.x);
		double minY = Math.min(Math.min(p1.y, p2.y),p3.y);
		double maxY = Math.max(Math.max(p1.y, p2.y),p3.y);
		double minZ = Math.min(Math.min(p1.z, p2.z),p3.z);
		double maxZ = Math.max(Math.max(p1.z, p2.z),p3.z);
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
			return mesh.normalToGlobal(Vectors.add(Vectors.add(uv1, u2), v2));
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
	@Override
	public boolean intersectsBox(BoundingBox bb) {
		return getBoundingBox().intersectsBox(bb);
//		// Si algún vértice está contenido en la caja, intersecta
//		if (bb.contains(p1) || bb.contains(p2) || bb.contains(p3))
//			return true;
//
//		// Si algún eje intersecta la caja, intersecta
//		if (edgeIntersectsBox(p1, p2, bb) || edgeIntersectsBox(p2, p3, bb)
//				|| edgeIntersectsBox(p3, p1, bb))
//			return true;
//
//		// Todavía puede ser que el triángulo seccione la caja sin que sus
//		// ejes la intesecten (una esquina, todo un plano de la caja, etc.)
//		// Para eso, chequear si alguna de las 4 diagonales de la caja
//		// intersectan el triángulo.
//		Ray r = new Ray(new Vector3d(), new Point3d());
//		Point3d orig = new Point3d();
//		Vector3d dir = new Vector3d();
//		double len;
//
//		orig.set(bb.x0, bb.y0, bb.z0);
//		dir.set(bb.x1 - bb.x0, bb.y1 - bb.y0, bb.z1 - bb.z0);
//		len = dir.length();
//		dir.scale(1.0 / len);
//		if (checkIntersection(r, len))
//			return true;
//		orig.set(bb.x1, bb.y0, bb.z0);
//		dir.set(bb.x0 - bb.x1, bb.y1 - bb.y0, bb.z1 - bb.z0);
//		len = dir.length();
//		dir.scale(1.0 / len);
//		if (checkIntersection(r, len))
//			return true;
//		orig.set(bb.x0, bb.y1, bb.z0);
//		dir.set(bb.x1 - bb.x0, bb.y0 - bb.y1, bb.z1 - bb.z0);
//		len = dir.length();
//		dir.scale(1.0 / len);
//		if (checkIntersection(r, len))
//			return true;
//		orig.set(bb.x0, bb.y0, bb.z1);
//		dir.set(bb.x1 - bb.x0, bb.y1 - bb.y0, bb.z0 - bb.z1);
//		len = dir.length();
//		dir.scale(1.0 / len);
//		if (checkIntersection(r, len))
//			return true;
//		return false;
	}
	private boolean checkIntersection(Ray r, double withinDistance) {
		Vector3d normal = new Vector3d(this.normal);
		
		// Si no pertenece al plano del triángulo, no hay intersección
		double auxDot = normal.dot(r.getDirection());
		if (auxDot < 0) {
			normal.scale(-1);
		}
		if (normal.dot(r.getDirection()) < OctreeNode.TOLERANCE) {
			return false;
		}
		
		// Sino, calcular el punto de intersección en el plano
		Vector3d a = new Vector3d(p1);
		a.sub(r.getOrigin());
		double t = a.dot(normal) / r.getDirection().dot(normal);
		if (!(t >= 0 && t < withinDistance)) {
			return false;
		}
		Vector3d pointOfIntersection = new Vector3d(r.getDirection());
		pointOfIntersection.scale(t);
		pointOfIntersection.add(r.getOrigin());
		
		// Determinar si el punto de intersección pertenece al triángulo
		if (pointBelongs(pointOfIntersection)) {
			return true;
		}
		return false;
	}
	public boolean pointBelongs(Vector3d point) {
		Vector3d aux = new Vector3d(point);
		aux.sub(p1);
		
		// Si no pertenece al plano, return false
		if (Math.abs(aux.dot(normal)) > OctreeNode.TOLERANCE) {
			return false;
		}
		
		Vector3d aux2 = new Vector3d(point);
		aux2.sub(p1);
		aux2.cross(d1, aux2);
		if (aux2.dot(normal) < 0) {
			return false;
		}
		
		aux2 = new Vector3d(point);
		aux2.sub(p2);
		aux2.cross(Vectors.sub(p3,p2), aux2);
		if (aux2.dot(normal) < 0) {
			return false;
		}
		
		aux2 = new Vector3d(point);
		aux2.sub(p3);
		aux2.cross(Vectors.sub(p1,p3), aux2);
		if (aux2.dot(normal) < 0) {
			return false;
		}
		
		return true;
	}
	private boolean edgeIntersectsBox(Point3d p1, Point3d p2, BoundingBox bb) {
		double t1, t2, tmin = -Double.MAX_VALUE, tmax = Double.MAX_VALUE;
		double dirx = p2.x - p1.x, diry = p2.y - p1.y, dirz = p2.z - p1.z;
		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
		if (dirx == 0.0) {
			if (p1.x < bb.x0 || p1.x > bb.x1)
				return false;
		} else {
			t1 = (bb.x0 - p1.x) * len / dirx;
			t2 = (bb.x1 - p1.x) * len / dirx;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmin > len || tmax < 0.0)
				return false;
		}
		if (diry == 0.0) {
			if (p1.y < bb.y0 || p1.y > bb.y1)
				return false;
		} else {
			t1 = (bb.y0 - p1.y) * len / diry;
			t2 = (bb.y1 - p1.y) * len / diry;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmin > len || tmax < 0.0)
				return false;
		}
		if (dirz == 0.0) {
			if (p1.z < bb.z0 || p1.z > bb.z1)
				return false;
		} else {
			t1 = (bb.z0 - p1.z) * len / dirz;
			t2 = (bb.z1 - p1.z) * len / dirz;
			if (t1 < t2) {
				if (t1 > tmin)
					tmin = t1;
				if (t2 < tmax)
					tmax = t2;
			} else {
				if (t2 > tmin)
					tmin = t2;
				if (t1 < tmax)
					tmax = t1;
			}
			if (tmin > tmax || tmin > len || tmax < 0.0)
				return false;
		}
		return true;
	}
}
