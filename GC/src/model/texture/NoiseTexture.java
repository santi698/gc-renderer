package model.texture;

import java.util.Random;
import java.util.function.Function;

import javax.vecmath.Color3f;

public class NoiseTexture {
	public Random noiseGenerator;
	public Function<Float, Float> f;
	public NoiseTexture(Function<Float,Float> f) {
		noiseGenerator = new Random();
		this.f = f;
	}
	public Color3f get(double u, double v) {
		Color3f color = new Color3f();
		color.x = f.apply(noiseGenerator.nextFloat());
		color.y = f.apply(noiseGenerator.nextFloat());
		color.z = f.apply(noiseGenerator.nextFloat());
		return color;
	}
}
