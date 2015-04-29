package model.texture;

import javax.vecmath.Color3f;

public interface Texture {
	public Color3f get(double u, double v);
}
