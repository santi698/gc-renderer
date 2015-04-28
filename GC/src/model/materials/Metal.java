package model.materials;

import javax.vecmath.Color3f;

public class Metal extends Phong {
	public Metal(Color3f color, double roughness) {
		super(color, roughness, 0.5, 1);
	}
}
