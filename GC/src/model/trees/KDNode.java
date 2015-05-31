package model.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;

public class KDNode extends TraceableTree{

	public BoundingBox bbox;
	
	private final float[][] minMax = new float[2][];
	private float[][] vertices;
	private float[][] normals;
	private int[][] indices;
	public KDNode left;
	public KDNode right;
	
	public int depth;
	   
	public KDNode(final float[][] vertices, final float[][] normals, final int[][] indices, final float[][] minMax, final int axis, final int depth, float [][] UVs) {		
		this.depth = depth;
		this.normals = normals;
		this.vertices = vertices;
		this.indices = indices;
		
		System.out.println("KDNODE DEPTH:" + depth + " amount of indices "+ indices.length);
		
		if(indices.length <= 56){
			int j = 8;	
			j++;
		}
		
	     if (indices.length > 2000) {
	        float median;
	        final List<int[]> leftChildren = new ArrayList<int[]>();
	        final List<int[]> rightChildren = new ArrayList<int[]>();
	

	        this.minMax[0] = new float[] { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
	        this.minMax[1] = new float[] { -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE };

	        for (final float[] vertice : vertices) {
	           this.minMax[0][0] = Math.min(vertice[0], this.minMax[0][0]);
	           this.minMax[1][0] = Math.max(vertice[0], this.minMax[1][0]);

	           this.minMax[0][1] = Math.min(vertice[1], this.minMax[0][1]);
	           this.minMax[1][1] = Math.max(vertice[1], this.minMax[1][1]);

	           this.minMax[0][2] = Math.min(vertice[2], this.minMax[0][2]);
	           this.minMax[1][2] = Math.max(vertice[2], this.minMax[1][2]);
	        }
	          
	         if (axis == 0) {
	             median = (this.minMax[1][0] - this.minMax[0][0]) / 2f + this.minMax[0][0];
	          } else if (axis == 1) {
	             median = (this.minMax[1][1] - this.minMax[0][1]) / 2f + this.minMax[0][1];
	          } else {
	             median = (this.minMax[1][2] - this.minMax[0][2]) / 2f + this.minMax[0][2];
	          }
	
	        final float[][] leftMinMax = new float[][] { { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE }, { -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE } };
	        final float[][] rightMinMax = new float[][] { { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE }, { -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE } };
	
	        final float[][] childMinMax = new float[2][3];
	
	        for (final int[] face : indices) {
	           getMinMax(vertices, face, childMinMax);
	
	           if (childMinMax[1][axis] <= median) {
	              leftChildren.add(face);
	           } else if (childMinMax[0][axis] >= median) {
	              rightChildren.add(face);

	           } else {
	              leftChildren.add(face);
	              rightChildren.add(face);

	           }
	        }
	
	          if (leftChildren.size() > 0) {
	             final int[][] leftChildrenFaces = leftChildren.toArray(new int[leftChildren.size()][3]);
	             getMinMax(vertices, leftChildrenFaces, leftMinMax);
	             left = new KDNode(vertices, normals, leftChildrenFaces, leftMinMax, (axis +1) % 3, depth + 1, UVs);
	          } else {
	             left = null;
	          }

	          if (rightChildren.size() > 0) {
	             final int[][] rightChildrenFaces = rightChildren.toArray(new int[rightChildren.size()][3]);
	             getMinMax(vertices, rightChildrenFaces, rightMinMax);
	             right = new KDNode(vertices, normals, rightChildrenFaces, rightMinMax, (axis +1) % 3 , depth + 1, UVs);
	          } else {
	             right = null;
	          }
	          
	          
	     } else {
	        left = null;
	        right = null;
	     }
     
     
	}
	
   public static void getMinMax(final float[][] vertices, final int[][] indices, final float[][] output) {
	      output[0][0] = Float.MAX_VALUE;
	      output[0][1] = Float.MAX_VALUE;
	      output[0][2] = Float.MAX_VALUE;

	      output[1][0] = -Float.MAX_VALUE;
	      output[1][1] = -Float.MAX_VALUE;
	      output[1][2] = -Float.MAX_VALUE;

	      for (final int[] face : indices) {
	         for (final int index : face) {
	            output[0][0] = Math.min(output[0][0], vertices[index][0]);
	            output[0][1] = Math.min(output[0][1], vertices[index][1]);
	            output[0][2] = Math.min(output[0][2], vertices[index][2]);

	            output[1][0] = Math.max(output[1][0], vertices[index][0]);
	            output[1][1] = Math.max(output[1][1], vertices[index][1]);
	            output[1][2] = Math.max(output[1][2], vertices[index][2]);
	         }
	      }
	   }
   
   public static void getMinMax(final float[][] vertices, final int[] indices, final float[][] output) {
      output[0][0] = Float.MAX_VALUE;
      output[0][1] = Float.MAX_VALUE;
      output[0][2] = Float.MAX_VALUE;

      output[1][0] = -Float.MAX_VALUE;
      output[1][1] = -Float.MAX_VALUE;
      output[1][2] = -Float.MAX_VALUE;

      for (final int index : indices) {
         output[0][0] = Math.min(output[0][0], vertices[index][0]);
         output[0][1] = Math.min(output[0][1], vertices[index][1]);
         output[0][2] = Math.min(output[0][2], vertices[index][2]);

         output[1][0] = Math.max(output[1][0], vertices[index][0]);
         output[1][1] = Math.max(output[1][1], vertices[index][1]);
         output[1][2] = Math.max(output[1][2], vertices[index][2]);
      }
   }

	@Override
	public void add(Traceable object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IntersectionContext trace(Ray ray) {


		 if (left != null || right != null) {
	         IntersectionContext tempLeft = null, tempRight = null;

	         if (left != null && left.intersects(ray)) {
	            tempLeft = left.getChildIntersection(ray, depth);
	         }

	         if (right != null && right.intersects(ray)) {
	            tempRight = right.getChildIntersection(ray, depth);
	         }

	         return (tempLeft == IntersectionContext.noHit() ) ? tempRight : tempLeft;
	      } else {
	         float[] temp;
	         float[] closest = null;

	         for (final int[] face : indices) {
	            if (aabbIntersection(ray, minMax)) {
		               Vector3f aux = new Vector3f((float) ray.getOrigin().x,(float)ray.getOrigin().y,(float)ray.getOrigin().z);
		               Vector3f aux2 = new Vector3f((float) ray.getDirection().x,(float)ray.getDirection().y,(float)ray.getDirection().z);
		               temp = intersectsTriangle(aux,aux2, vertices, normals, face);
		               
	               if (temp != null && (closest == null || temp[6] < closest[6])) {
	                  closest = temp;
	               }
	            }
	         }

	         return closest == null ? IntersectionContext.noHit() : new IntersectionContext(0.0d,new Vector3d(closest[3], closest[4], closest[5]),ray,true, 0.3,0.3);

	      }
		 
	}
	
   public boolean intersects(final Ray ray) {
	      return aabbIntersection(ray, minMax);
   }
   
   public static boolean aabbIntersection(final Ray r, final float[][] minMax) {
	      // http://people.csail.mit.edu/amy/papers/box-jgt.pdf
	      float txmin, txmax, tymin, tymax, tzmin, tzmax;
	      final float divx = (float) (1.0f / r.getDirection().x);
	      final float divy = (float) (1.0f / r.getDirection().y);
	      final float divz = (float) (1.0f / r.getDirection().z);

	      if (divx >= 0) {
	         txmin = (float) ((minMax[0][0] - r.getOrigin().x) * divx);
	         txmax = (float) ((minMax[1][0] - r.getOrigin().x) * divx);
	      } else {
	         txmin = (float) ((minMax[1][0] - r.getOrigin().x) * divx);
	         txmax = (float) ((minMax[0][0] - r.getOrigin().x) * divx);
	      }

	      if (divy >= 0) {
	         tymin = (float) ((minMax[0][1] - r.getOrigin().y) * divy);
	         tymax = (float) ((minMax[1][1] - r.getOrigin().y) * divy);
	      } else {
	         tymin = (float) ((minMax[1][1] - r.getOrigin().y) * divy);
	         tymax = (float) ((minMax[0][1] - r.getOrigin().y) * divy);
	      }

	      if (txmin > tymax || tymin > txmax) {
	         return false;
	      }

	      if (tymin > txmin) {
	         txmin = tymin;
	      }

	      if (tymax < txmax) {
	         txmax = tymax;
	      }

	      if (divz >= 0) {
	         tzmin = (float) ((minMax[0][2] - r.getOrigin().z) * divz);
	         tzmax = (float) ((minMax[1][2] - r.getOrigin().z) * divz);
	      } else {
	         tzmin = (float) ((minMax[1][2] - r.getOrigin().z) * divz);
	         tzmax = (float) ((minMax[0][2] - r.getOrigin().z) * divz);
	      }

	      if (txmin > tzmax || tzmin > txmax) {
	         return false;
	      }

	      if (tzmin > txmin) {
	         txmin = tzmin;
	      }

	      if (tzmax < txmax) {
	         txmax = tzmax;
	      }

	      return true;
	   }
   
   public IntersectionContext getChildIntersection(final Ray ray, final int depth) {
	      if (left != null || right != null) {
	    	  IntersectionContext tempLeft = null, tempRight = null;

	         if (left != null && left.intersects(ray)) {
	            tempLeft = left.getChildIntersection(ray, depth);
	         }

	         if (right != null && right.intersects(ray)) {
	            tempRight = right.getChildIntersection(ray, depth);
	         }

	         return (tempLeft == IntersectionContext.noHit() ) ? tempRight : tempLeft;
	      } else {
	         float[] temp;
	         float[] closest = null;

	         for (final int[] face : indices) {
	            if (aabbIntersection(ray, minMax)) {
		               Vector3f aux = new Vector3f((float) ray.getOrigin().x,(float)ray.getOrigin().y,(float)ray.getOrigin().z);
		               Vector3f aux2 = new Vector3f((float) ray.getDirection().x,(float)ray.getDirection().y,(float)ray.getDirection().z);
		               temp = intersectsTriangle(aux,aux2, vertices, normals, face);

	               if (temp != null && (closest == null || temp[6] < closest[6])) {
	                  closest = temp;
	               }
	            }
	         }

	         return closest == null ? IntersectionContext.noHit() : new IntersectionContext(0.0d,new Vector3d(closest[3], closest[4], closest[5]),ray,true, 0.3,0.3);
	      }
	   }
   
   
   public static float[] intersectsTriangle(final Vector3f rO, final Vector3f rD, final float[][] vertices, final float[][] normals, final int[] indices) {
	      // final float[] normal = RTStatics.computeNormal(vertices, indices);
	      final Vector3f[] p0 = new Vector3f[] { new Vector3f(vertices[indices[0]]), new Vector3f(normals[indices[0]]) };
	      final Vector3f[] p1 = new Vector3f[] { new Vector3f(vertices[indices[1]]), new Vector3f(normals[indices[1]]) };
	      final Vector3f[] p2 = new Vector3f[] { new Vector3f(vertices[indices[2]]), new Vector3f(normals[indices[2]]) };

	      final Vector3f e1 = new Vector3f();
	      final Vector3f e2 = new Vector3f();
	      e1.sub(p1[0], p0[0]);
	      e2.sub(p2[0], p0[0]);

	      final Vector3f p = new Vector3f();
	      p.cross(rD, e2);
	      final float divisor = p.dot(e1);
	      /*
	       * Ray nearly parallel to triangle plane...
	       */
	      if ((divisor < 1e-15f) && (divisor > -1e-15f)) {
	         return null;
	      }

	      final Vector3f translatedOrigin = new Vector3f(rO);
	      translatedOrigin.sub(p0[0]);
	      final Vector3f q = new Vector3f();
	      q.cross(translatedOrigin, e1);
	      /*
	       * Barycentric coords also result from this formulation, which could be useful for interpolating attributes
	       * defined at the vertex locations:
	       */
	      final float u = p.dot(translatedOrigin) / divisor;
	      if ((u < 0) || (u > 1)) {
	         return null;
	      }

	      final float v = q.dot(rD) / divisor;
	      if ((v < 0) || (v + u > 1)) {
	         return null;
	      }

	      // return q.dot(e2) / divisor;

	      final float w = 1.0f - u - v;

	      final float[] returnValue = new float[] { w * p0[0].x + u * p1[0].x + v * p2[0].x, w * p0[0].y + u * p1[0].y + v * p2[0].y, w * p0[0].z + u * p1[0].z + v * p2[0].z,
	            w * p0[1].x + u * p1[1].x + v * p2[1].x, w * p0[1].y + u * p1[1].y + v * p2[1].y, w * p0[1].z + u * p1[1].z + v * p2[1].z, -1 };
	      returnValue[6] = getDistance(new float[] { rO.getX(), rO.getY(), rO.getZ() }, returnValue);

	      return returnValue;
	   }
   
   public static float getDistance(final float[] p1, final float[] p2) {
	      return (float) Math.sqrt(getDistanceSquared(p1, p2));
	   }
   
   public static float getDistanceSquared(final float[] p1, final float[] p2) {
	      return (p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]) + (p1[2] - p2[2]) * (p1[2] - p2[2]);
	   }
}