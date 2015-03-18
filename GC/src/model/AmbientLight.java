package model;

import java.awt.Color;

import javafx.geometry.Point3D;

public class AmbientLight implements Light {
	private Color color;
	
	public AmbientLight(Color color) {
		super();
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public double isVisible(Point3D point) {
		return 1;
	}

}
