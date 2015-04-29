package model.materials;

import javax.vecmath.Color3f;

public class Glass extends Transparent {
	public Glass(Color3f color) {
		super(color, 1.52);
	}
}
