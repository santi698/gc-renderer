package model.materials;

import model.brdfs.OrenNayar;
import model.texture.Texture;

public class Matte2 extends PureDiffuse {

	public Matte2(Texture texture, double roughness, double albedo) {
		super(texture, new OrenNayar(roughness, albedo));
	}
	
}
