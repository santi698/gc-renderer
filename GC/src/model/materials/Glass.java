package model.materials;

import javax.vecmath.Color3f;

import model.shaders.Phong;

public class Glass extends Material{
	public Glass(Color3f color) {
		super(0.05, 0.9, 1.52, 0.05, color, new Phong(100));
	}
}
