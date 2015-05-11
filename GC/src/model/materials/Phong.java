package model.materials;

import model.brdfs.PerfectDiffuse;
import model.brdfs.PerfectSpecular;
import model.texture.Texture;

public class Phong extends DiffuseAndSpecular {
	public Phong(Texture bodyTexture, double alpha, double kS, double kD) {
		super(bodyTexture, 0.2, new PerfectDiffuse(kD), new PerfectSpecular(alpha, kS));
	}
	public Phong(Texture bodyTexture, double alpha, double kA, double kS, double kD) {
		super(bodyTexture, kA, new PerfectDiffuse(kD), new PerfectSpecular(alpha, kS));
	}
}
