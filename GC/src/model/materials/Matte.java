package model.materials;

import javax.vecmath.Color3f;

import model.shaders.Lambert;

public class Matte extends Material {
	public Matte(Color3f color) {
		super(0, 0, 0, 1, color, new Lambert());
	}
}
