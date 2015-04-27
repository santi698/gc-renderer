package model.materials;

import javax.vecmath.Color3f;

import model.shaders.Phong;

public class Mirror extends Material {
	public Mirror() {
		super(0.9, 0, 0, 0.1, new Color3f(), new Phong(100));
	}
	public Mirror(Color3f color) {
		super(0.9, 0, 0, 0.1, color, new Phong(100));
	}
}
