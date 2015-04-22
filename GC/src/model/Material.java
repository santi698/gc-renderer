package model;

import javax.vecmath.Color3f;

public class Material {
	private double reflectionIndex;
	private double transparency;
	private double refractionIndex;
	private double absorptionIndex;
	private Color3f color;
	private Shader shader;
	
	public Material(double reflectionIndex, double transparency,
			double refractionIndex, double absorptionIndex, Color3f color, Shader shader) {
		super();
		this.reflectionIndex = reflectionIndex;
		this.transparency = transparency;
		this.refractionIndex = refractionIndex;
		this.absorptionIndex = absorptionIndex;
		this.color = color;
		this.shader = shader;
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
	public Shader getShader() {
		return shader;
	}
	public Color3f getColor() {
		return color;
	}
}
