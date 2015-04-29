package model.materials;

import model.texture.Texture;

public class Glass extends Transparent {
	private double thickness = 0;
	public Glass(Texture texture) {
		super(texture, 1.52);
	}
	public Glass(Texture texture, double thickness) {
		super(texture, 1.52);
		this.thickness = thickness;
	}
	@Override
	public boolean isThin() {
		return thickness != 0;
	}
	@Override
	public double getThickness() {
		return thickness;
	}
}
