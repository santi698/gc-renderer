package model.materials;

import model.brdfs.PerfectDiffuse;
import model.texture.Texture;

public class Matte extends PureDiffuse {
	public Matte(Texture texture, double albedo) {
		super(texture, new PerfectDiffuse(albedo/Math.PI));
	}
}
