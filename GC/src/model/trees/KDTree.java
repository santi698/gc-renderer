package model.trees;

import model.IntersectionContext;
import model.Ray;
import model.shapes.BoundingBox;

public class KDTree{

   private final float[][] minMax = new float[2][];
   private KDNode rootNode = null;

   public KDTree(final float[][] vertices, final float[][] normals, final int[][] indices, final float[][] UVs) {

	   
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
	      
	      
      System.out.println("creating KD Tree...");
      final long startTime = System.nanoTime();
      
      this.rootNode = new KDNode(vertices, normals, indices,minMax, 0,0, UVs);
      final long endTime = System.nanoTime();

      System.out.println("KD Tree computation duration: " + (endTime - startTime) / 1000000000. + " seconds");
   }

	public IntersectionContext trace(Ray ray) {
		return rootNode.trace(ray);
	}

	public BoundingBox getBoundingBox() {
		return rootNode.bbox;
	}
}
