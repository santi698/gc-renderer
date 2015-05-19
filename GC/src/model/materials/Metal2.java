package model.materials;

import model.texture.Texture;

public class Metal2 extends GeneralRealistic {
	public Metal2(Texture texture, double roughness) {
		super(texture, 1d/(Double.MIN_VALUE+roughness), 0, 64, 1, 0.75);
	}
}
