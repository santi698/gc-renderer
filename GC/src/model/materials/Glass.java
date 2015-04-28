package model.materials;

import javax.vecmath.Color3f;

public class Glass extends Transparent {
	public Glass(Color3f color) {
		super(color, 1.52);
	}
	public Glass(Color3f color, double reflectivity) {
		super(color, 1.52, reflectivity);
	}
}
