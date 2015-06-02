package model.shapes;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;

public class BoundingBox extends Shape {

	public double x0,y0,z0;
	public double x1,y1,z1;

	public BoundingBox(double x0, double y0, double z0, double x1, double y1, double z1) {
		super(new Vector3d(), new Vector3d(), 1);
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		this.z0 = z0;
		this.z1 = z1;
		fixOrder();

	}
	public BoundingBox(BoundingBox box) {
		super(new Vector3d(), new Vector3d(), 1);
		this.x0 = box.x0;
		this.x1 = box.x1;
		this.y0 = box.y0;
		this.y1 = box.y1;
		this.z0 = box.z0;
		this.z1 = box.z1;
		fixOrder();
	}
	private void fixOrder() {
		if (x1 < x0){
			double aux = x0;
			x0 = x1;
			x1 = aux;
		}
		if (y1 < y0){
			double aux = y0;
			y0 = y1;
			y1 = aux;
		}
		if (z1 < z0){
			double aux = z0;
			z0 = z1;
			z1 = aux;
		}
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
		if(b >= 0){
			ty_min = (y0 - oy) * b;
			ty_max = (y1 - oy) * b;
		}else{
			ty_min = (y1 - oy) * b;
			ty_max = (y0 - oy) * b;
		}
		
		double c = 1.0f / dz;
		if(c >= 0){
			tz_min = (z0 - oz) * c;
			tz_max = (z1 - oz) * c;
		}else{
			tz_min = (z1 - oz) * c;
			tz_max = (z0 - oz) * c;
		}
		
		
		double t0, t1;
		
		int face_in, face_out;
		
		if(tx_min > ty_min){
			t0 = tx_min;
			face_in = (a >= 0.0d ) ? 0 : 3 ;
		}else{
			t0 = ty_min;
			face_in = (b >= 0.0d ) ? 1 : 4 ;
		}
		
		if( tz_min > t0){
			t0 = tz_min;
			face_in = (c >= 0.0d ) ? 2 : 5 ;
		}
		
		if(tx_max < ty_max){
			t1 = tx_max;
			face_out = (a >= 0.0d ) ? 3 : 0 ;
		}else{
			t1 = ty_max;
			face_out = (b >= 0.0d ) ? 4 : 1 ;
		}
		
		if( tz_max < t1){
			t1 = tz_max;
			face_out = (c >= 0.0d ) ? 5 : 2 ;
		}
		
		double tmin = 0.0d;
		Vector3d normal = null;
		if(t0 < t1 && t1 > EPS){
			if(t0 > EPS){
				tmin = t0;
				normal = getNormal(face_in);
			}else{
				tmin = t1;
				normal = getNormal(face_out);
			}
			
			Vector3d dir = new Vector3d(ray.getDirection());
			dir.scale(tmin);
			Point3d ori = new Point3d(ray.getOrigin());
			ori.add(dir);
			Point3d localHitPoint = ori;
			
			Point2d uv = getUVCoordinates(localHitPoint);
			return new IntersectionContext(tmin,normal,ray,true,uv.x,uv.y);
		}	
		
		return IntersectionContext.noHit();
		
	}
	
	private Point2d getUVCoordinates(Point3d localHitPoint) {
		Point3d texturePoint = localToTexture(localHitPoint);
		Point2d uv = new Point2d();
		uv.x = texturePoint.x;
		uv.y = texturePoint.z;
		return uv;
	}
	public final boolean intersectsBox(BoundingBox b) {
		if (x0 > b.x1 || x1 < b.x0 || y0 > b.y1 || y1 < b.y0
				|| z0 > b.z1 || z1 < b.z0)
			return false;
		return true;
	}
	
	protected Vector3d getNormal(int face_hit) {

		switch(face_hit){
		case 0:
			return new Vector3d(-1,0,0);
		case 1:
			return new Vector3d(0,-1,0);
		case 2:
			return new Vector3d(0,0,-1);
		case 3:
			return new Vector3d(1,0,0);
		case 4:
			return new Vector3d(0,1,0);
		case 5:
			return new Vector3d(0,0,1);
		}
		
		return null;
	}
	
	public int longestAxis(){
		double x = Math.abs(x1 - x0);
		double y = Math.abs(y1 - y0);
		double z = Math.abs(z1 - z0);
		if (x >= y) {
			if (x >= z)
				return 0;
			else
				return 2;
		} else if (y > z) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public void expand(BoundingBox bbox){
		if (bbox == null)
			return;
		this.x0 = Math.min(this.x0, bbox.x0);
		this.x1 = Math.max(this.x1, bbox.x1);

		this.y0 = Math.min(this.y0, bbox.y0);
		this.y1 = Math.max(this.y1, bbox.y1);

		this.z0 = Math.min(this.z0, bbox.z0);
		this.z1 = Math.max(this.z1, bbox.z1);
	}
	
	
	public Point3d getMidpoint(){
		return new Point3d((x0+x1)/2,(y0+y1)/2,(y0+y1)/2);
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return this;
	}
	public final boolean contains(Point3d p) {
		if (p.x < x0 || p.x > x1 || p.y < y0 || p.y > y1 || p.z < z0
				|| p.z > z1)
			return false;
		return true;
	}}
