package test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.shapes.Plane;

import org.junit.Before;
import org.junit.Test;

public class testPlane {
	private Plane p1;
	private Plane p2;
	private Ray r1;
	private Ray r2;
	private Ray r3;
	IntersectionContext ic11;
	IntersectionContext ic12;
	IntersectionContext ic13;
	IntersectionContext ic21;
	IntersectionContext ic22;
	IntersectionContext ic23;

	@Before
	public void setUp() throws Exception {
		p1 = new Plane(new Vector3d(1,0,0), new Point3d(0,-1,-1));
		p2 = new Plane(new Vector3d(0,1,0), new Point3d(-2,0,-2));
		r1 = new Ray(new Vector3d(0,1,1), new Point3d(0,-1,0));
		r2 = new Ray(new Vector3d(1,0,0), new Point3d(-1,10,-1));
		r3 = new Ray(new Vector3d(1,0,1), new Point3d(-1,1,-2));
		ic11 = p1.trace(r1);
		ic12 = p1.trace(r2);
		ic13 = p1.trace(r3);
		ic21 = p2.trace(r1);
		ic22 = p2.trace(r2);
		ic23 = p2.trace(r3);
	}

	@Test
	public void testIntersection() {
		assertFalse(ic11.getHit());
		assertTrue(ic12.getHit());
		assertTrue(ic13.getHit());
		assertTrue(ic21.getHit());
		assertFalse(ic22.getHit());
		assertFalse(ic23.getHit());
	}

}
