package model.bodies;

import javax.vecmath.Color3f;

public class Material {
	private double reflectionIndex;
	private double transparency;
	private double refractionIndex;
	private double absorptionIndex;
	private Color3f color;
	
	public Material(double reflectionIndex, double transparency,
			double refractionIndex, double absorptionIndex, Color3f color) {
		super();
		this.reflectionIndex = reflectionIndex;
		this.transparency = transparency;
		this.refractionIndex = refractionIndex;
		this.absorptionIndex = absorptionIndex;
		this.color = color;
	}
	public double getReflectionIndex() {
		return reflectionIndex;
	}
	public double getTransparency() {
		return transparency;
	}
	public double getRefractionIndex() {
		return refractionIndex;
	}
	public double getAbsorptionIndex() {
		return absorptionIndex;
	}
	public Color3f getColor() {
		return color;
	}
}
