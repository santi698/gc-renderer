package model.materials;

import model.texture.Texture;

public class Metal extends Phong {
	public Metal(Texture texture, double roughness) {
		super(texture, roughness, 0.5, 1);
	}
}
