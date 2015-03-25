package model.bodies;

import java.awt.Color;

public class Body {
	private Shape shape;
	private Color color;
	private double refrIndex;
	private double reflIndex;
	private double absorIndex;
	public Body(Shape shape, Color color, double refrIndex, double reflIndex,
			double absorIndex) {
		super();
		this.shape = shape;
		this.color = color;
		this.refrIndex = refrIndex;
		this.reflIndex = reflIndex;
		this.absorIndex = absorIndex;
	}
	public Shape getShape() {
		return shape;
	}
	public Color getColor() {
		return color;
	}
	public double getRefrIndex() {
		return refrIndex;
	}
	public double getReflIndex() {
		return reflIndex;
	}
	public double getAbsorIndex() {
		return absorIndex;
	}
	
}
