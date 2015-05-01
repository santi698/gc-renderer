package model.texture;

import javax.vecmath.Color3f;

public class PlainTexture implements Texture{
	public Color3f color;
	public PlainTexture(Color3f color) {
		this.color = color;
	}
	@Override
	public Color3f get(double u, double v) {
		return color;
	}
}