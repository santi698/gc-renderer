package model;

import java.util.Arrays;
import java.util.Collection;
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
}
