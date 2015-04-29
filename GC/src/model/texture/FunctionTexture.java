package model.texture;

import java.util.function.BiFunction;

import javax.vecmath.Color3f;

public class FunctionTexture implements Texture {
	BiFunction<Double, Double, Color3f> f;
	public FunctionTexture(BiFunction<Double, Double, Color3f> f) {
		this.f = f;
	}
	@Override
	public Color3f get(double u, double v) {
		return f.apply(u,v);
	}

}
