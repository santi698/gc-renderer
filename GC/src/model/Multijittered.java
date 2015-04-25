package model;

import javax.vecmath.Point2d;

public class Multijittered extends Sampler {
	public Multijittered(int numSamples) {
		super(numSamples);
	}
	@Override
	public void generateSamples() {
		// numSamples needs to be a perfect square
		int n = (int)Math.sqrt(this.numSamples);
		float subcell_width = 1.0f / ((float) numSamples);
		// fill the samples array with dummy points to allow us to use the [ ] notation when we set the
		// initial patterns
		for (int j = 0; j < numSamples * numSets; j++)
			samples[j] = new Point2d();
		// distribute points in the initial patterns
		for (int p = 0; p < numSets; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
				samples[i * n + j + p * numSamples].x = (i * n + j) * subcell_width + Math.random()*subcell_width;
				samples[i * n + j + p * numSamples].y = (j * n + i) * subcell_width + Math.random()*subcell_width;
				}
			}
		}
		
		// shuffle x coordinates
		for (int p = 0; p < numSets; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int k = (int) Math.round(Math.random()*((n-1)-j) + j);
					double t = samples[i * n + j + p * numSamples].x;
					samples[i * n + j + p * numSamples].x = samples[i * n + k + p * numSamples].x;
					samples[i * n + k + p * numSamples].x = t;
				}
			}
		}
		// shuffle y coordinates
		for (int p = 0; p < numSets; p++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int k = (int) Math.round(Math.random()*((n-1)-j) + j);
					double t = samples[j * n + i + p * numSamples].y;
					samples[j * n + i + p * numSamples].y = samples[k * n + i + p * numSamples].y;
					samples[k * n + i + p * numSamples].y = t;
				}
			}
		}
	}

}
