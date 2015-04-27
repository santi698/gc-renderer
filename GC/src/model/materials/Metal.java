package model.materials;

import javax.vecmath.Color3f;

import model.shaders.Phong;

public class Metal extends Material {
	public Metal(Color3f color, double roughness) {
		super(0.25, 0, 0, 0.75, color, new Phong(roughness));
	}
}
