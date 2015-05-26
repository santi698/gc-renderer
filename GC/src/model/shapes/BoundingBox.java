package model.shapes;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Vectors;
import model.IntersectionContext;
import model.Ray;

public class BoundingBox extends Shape {

	double x0,y0,z0;
	double x1,y1,z1;

	public BoundingBox(double x0, double y0, double z0, double x1, double y1, double z1) {
		super(new Vector3d(), new Vector3d(), 1);
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		this.z0 = z0;
		this.z1 = z1;

	}
	@Override
	public IntersectionContext trace(Ray ray) {
		
		double ox = ray.getOrigin().x;
		double oy = ray.getOrigin().y;
		double oz = ray.getOrigin().z;

		double dx = ray.getDirection().x;
		double dy = ray.getDirection().y;
		double dz = ray.getDirection().z;
		
		double tx_min, ty_min, tz_min;
		double tx_max, ty_max, tz_max;
		
		double a = 1.0f / dx;
		if(a >= 0){
			tx_min = (x0 - ox) * a;
			tx_max = (x1 - ox) * a;
		}else{
			tx_min = (x1 - ox) * a;
			tx_max = (x0 - ox) * a;
		}
		
		double b = 1.0f / dy;
		if(a >= 0){
			ty_min = (y0 - oy) * b;
			ty_max = (y1 - oy) * b;
		}else{
			ty_min = (y1 - oy) * b;
			ty_max = (y0 - oy) * b;
		}
		
		double c = 1.0f / dz;
		if(a >= 0){
			tz_min = (z0 - oz) * c;
			tz_max = (z1 - oz) * c;
		}else{
			tz_min = (z1 - oz) * c;
			tz_max = (z0 - oz) * c;
		}
		
		double t0, t1;

		if(tx_min > ty_min)
			t0 = tx_min;
		else
			t0 = ty_min;
		
		if(tz_min > t0)
			t0 = tz_min;
		
		if(tx_max < ty_max)
			t1 = tx_max;
		else
			t1 = ty_max;
		
		if(tz_max < t1)
			t1 = tz_max;
		

		if ( t1 > EPS) {
			
			boolean isOutside = false;
			
			if(t0 < t1)
				isOutside = true;
			
			Point3d rayOrigin = ray.getOrigin();
			Point3d hitPoint = new Point3d(rayOrigin);
			Vector3d displacement = new Vector3d(ray.getDirection());
			displacement.scale(t1);
			hitPoint.add(displacement);
			Point2d uv = getUVCoordinates(hitPoint);
			
			Point3d localHitPoint = new Point3d(Vectors.add(rayOrigin,displacement));
			Vector3d normal = normalToGlobal(getNormalAt(localHitPoint, isOutside));
			normal.normalize();
			
			return new IntersectionContext(t1, normal , ray, true, uv.x, uv.y);
		}
		else return IntersectionContext.noHit();
	}
	
	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point3d texturePoint = localToTexture(localHitPoint);
		Point2d uv = new Point2d();
		uv.x = texturePoint.x;
		uv.y = texturePoint.z;
		return uv;
	}
	
	protected Vector3d getNormalAt(Point3d localHitPoint, boolean isOutside) {
		Vector3d normal = new Vector3d(localHitPoint);
		if(!isOutside)
			normal.negate();
		
		return normal;
	}
	
	public int longestAxis(){
		double longX = Math.abs(x0 - x1);
		double longY = Math.abs(y0 - y1);
		double longZ = Math.abs(z0 - z1);
		
		if(longX > longY && longX > longZ)
			return 0;
		else if(longY > longX && longY > longZ)
			return 1;
		else
			return 2;
	}
	
	public void expand(BoundingBox bbox){
		if(x0 > bbox.x0)
			x0 = bbox.x0;
		if(bbox.x1 > x1)
			x1 = bbox.x1;
		
		if(y0 > bbox.y0)
			y0 = bbox.y0;
		if(bbox.y1 > y1)
			y1 = bbox.y1;
		
		if(z0 > bbox.z0)
			z0 = bbox.z0;
		if(bbox.z1 > z1)
			z1 = bbox.z1;
	}
	
	
	public Point3d getMidpoint(){
		return new Point3d((x0+x1)/2,(y0+y1)/2,(y0+y1)/2);
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return this;
	}
}
