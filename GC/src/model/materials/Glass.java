package model.materials;

import javax.vecmath.Color3f;

public class Glass extends Transparent {
	private double thickness = 0;
	public Glass(Color3f color) {
		super(color, 1.52);
	}
	public Glass(Color3f color, double thickness) {
		super(color, 1.52);
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
