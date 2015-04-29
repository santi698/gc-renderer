package model.texture;

import javax.vecmath.Color3f;

public class CheckerBoardTexture extends FunctionTexture {
	public CheckerBoardTexture(Color3f color1, Color3f color2, double checkerSize) {
		super(
			(u,v)-> {
				if ((int)(Math.floor(u/checkerSize)+Math.floor(v/checkerSize)) % 2 == 0)
					return color1;
				else
					return color2;
			}
		);
	}
	public CheckerBoardTexture(double checkerSize) {
		this(new Color3f(0,0,0), new Color3f(1,1,1), checkerSize);
	}
}
