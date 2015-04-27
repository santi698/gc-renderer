package model.samplers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point2d;

public abstract class Sampler {
	protected final int numSamples;
	protected final int numSets = 83;
	protected Point2d[] samples;
	private Integer[] shuffledIndices;
	private long count;
	private int jump;
	private Random rnd = new Random();
	public abstract void generateSamples();
	public Sampler(int numSamples) {
		this.numSamples = numSamples;
		this.samples = new Point2d[numSamples*numSets];
		this.shuffledIndices = new Integer[numSamples*numSets];
	}
	public Point2d sampleUnitSquare() {
		if (count % numSamples == 0)
			jump = (rnd.nextInt(Integer.MAX_VALUE) % numSets) * numSamples;
		return samples[(int)(jump + shuffledIndices[(int)(jump + count++ % numSamples)])];
	}
	
	public void genShuffledIndices() {
		Integer[] indices = new Integer[numSamples];
		for (int i = 0; i < indices.length; i++) {
			indices[i] =  i;
		}
		for (int i = 0; i < numSets; i++) {
			List<Integer> list = Arrays.asList(indices);
			Collections.shuffle(list);
			indices = list.toArray(indices);
//			Collections.shuffle(indices); FIXME
			for (int j = 0; j < numSamples; j++) {
				shuffledIndices[i*numSamples+j]= indices[j];
			}
		}
	}
	public Point2d[] sampleUnitSquareSet() {
		Point2d[] set = new Point2d[numSamples];
		for (int i = 0; i < numSamples; i++) {
			set[i] = sampleUnitSquare();
		}
		return set;
	}
	public Point2d[] sampleUnitDiskSet() {
		Point2d[] set = new Point2d[numSamples];
		for (int i = 0; i < numSamples; i++) {
			set[i] = sampleUnitDisk();
		}
		return set;
	}
	public Point2d sampleUnitDisk() {
		Point2d ssp = sampleUnitSquare();
		Point2d sp = new Point2d();
		double r, phi;
		 // map sample point to [-1, 1] X [-1,1]
		sp.x = 2.0f * ssp.x - 1.0f;
		sp.y = 2.0f * ssp.y - 1.0f;
		if (sp.x > -sp.y) { // sectors 1 and 2
			if (sp.x > sp.y) { // sector 1
				r = sp.x;
				phi = sp.y / sp.x;
		}
		else { // sector 2
			r = sp.y;
			phi = 2 - sp.x / sp.y;
			}
		}
		else { // sectors 3 and 4
			if (sp.x < sp.y) { // sector 3
				r = -sp.x;
				phi = 4 + sp.y / sp.x;
			}
			else { // sector 4
				r = -sp.y;
				if (sp.y != 0.0) // avoid division by zero at origin
					phi = 6 - sp.x / sp.y;
				else
					phi = 0.0;
			}	
		}
		phi *= Math.PI / 4.0f;
		return new Point2d(r*Math.cos(phi), r*Math.sin(phi));
	}
}
