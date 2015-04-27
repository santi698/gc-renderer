package model.cameras;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.samplers.Multijittered;
import model.samplers.Sampler;

public class ThinLensCamera extends Camera {
	private double lensRadius;
	private double focalDistance;
	private int numSamples = 16;
	private Sampler sampler;
	public ThinLensCamera(Point3d position, Point3d lookAt, Vector3d up, double focalLength, int xres, int yres, double FOV, double fStop, Point3d focusAt) {
		super(position, lookAt, up, focalLength, xres, yres, FOV);
		this.focalDistance = position.distance(focusAt);
		this.lensRadius = getFocalLength()/(2*fStop);
		this.sampler = new Multijittered(numSamples);
		sampler.generateSamples();
		sampler.genShuffledIndices();
	}
	@Override
	public Point2d[] sampleLens() {
		Point2d[] set = new Point2d[numSamples];
		for (int i = 0; i < numSamples; i++) {
			Point2d lp = sampler.sampleUnitDisk();
			lp.scale(lensRadius);
			set[i] = lp;
		}
		return set;
	}
	public void setSampleNumber(int numSamples) {
		this.numSamples = numSamples;
		this.sampler = new Multijittered(numSamples);
		sampler.generateSamples();
		sampler.genShuffledIndices();
	}
	
	@Override
	public double getLensRadius() {
		return lensRadius;
	}
	
	@Override
	public double getFocalDistance() {
		return focalDistance;
	}
}
