package test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.IntersectionContext;
import model.Ray;
import model.shapes.Sphere;

import org.junit.Before;
import org.junit.Test;

public class testSphere {
	private Sphere s1;
	private Sphere s2;
	private Ray r1;
	private Ray r2;
	private Ray r3;
	IntersectionContext ic11;
	IntersectionContext ic12;
	IntersectionContext ic13;
	IntersectionContext ic21;
	IntersectionContext ic22;
	IntersectionContext ic23;
	/* r1 tangente a s1
	 * r2 exterior a s1
	 * r3 secante a s1
	 * r1, r2 y r3 secantes a s2
	 * */
	@Before
	public void setUp() throws Exception {
		s1 = new Sphere(new Point3d(0,0,0), 1);
		s2 = new Sphere(new Point3d(0,0,0), 10);
		r1 = new Ray(new Vector3d(0,1,0), new Point3d(1,-1,0));
		r2 = new Ray(new Vector3d(0,1,0), new Point3d(0,5,0));
		r3 = new Ray(new Vector3d(0,1,0), new Point3d(0,0,0));
		ic11 = s1.intersect(r1);
		ic12 = s1.intersect(r2);
		ic13 = s1.intersect(r3);
		ic21 = s2.intersect(r1);
		ic22 = s2.intersect(r2);
		ic23 = s2.intersect(r3);
	}

	@Test
	public void testIntercept() {
		assertTrue(ic11.getHit());
		assertEquals(ic11.getIntersectionPoint(), new Point3d(1,0,0));
		assertFalse(ic12.getHit());
		assertTrue(ic13.getHit());
		assertTrue(ic21.getHit());
		assertTrue(ic22.getHit());
		assertTrue(ic23.getHit());
	}

}
