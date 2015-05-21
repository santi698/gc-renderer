package model.materials;

import model.texture.Texture;

public class Glass extends Transparent {
	private double thickness = -1;
	public Glass(Texture texture) {
		this(texture, -1, 1.52);
	}
	public Glass(Texture texture, double thickness) {
		this(texture, thickness, 1.52);
	}
	public Glass(Texture texture, double thickness, double refractionIndex) {
		super(texture, refractionIndex);
		this.thickness = thickness;
	}
	public Glass(float kR, float kT, Texture texture, double thickness, double refractionIndex) {
		super(texture, refractionIndex, kR, kT);
		this.thickness = thickness;
	}
	public Glass(float kR, float kT, Texture texture, double refractionIndex) {
		this(kR, kT, texture, -1, refractionIndex);
	}
	@Override
	public boolean isThin() {
		return thickness >= 0;
	}
	@Override
	public double getThickness() {
		return thickness;
	}
}
