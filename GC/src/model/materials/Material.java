package model.materials;

import javax.vecmath.Color3f;

import model.shaders.Shader;
import model.shapes.Shape;

public class Material {
	private double reflectionCoefficient;
	private double transmissionCoefficient;
	private double refractionIndex;
	private double absorptionCoefficient;
	private Color3f color;
	private Shader shader;
	
	public Material(double reflectionCoefficient, double transmissionCoefficient,
			double refractionIndex, double absorptionCoefficient, Color3f color, Shader shader) {
		super();
		assert(reflectionCoefficient + transmissionCoefficient + absorptionCoefficient - 1 < Shape.EPS);
		assert(reflectionCoefficient>0 && transmissionCoefficient>0 && absorptionCoefficient >0 && refractionIndex >= 1);
		this.reflectionCoefficient = reflectionCoefficient;
		this.transmissionCoefficient = transmissionCoefficient;
		this.refractionIndex = refractionIndex;
		this.absorptionCoefficient = absorptionCoefficient;
		this.color = color;
		this.shader = shader;
	}
	public double getReflectionCoefficient() {
		return reflectionCoefficient;
	}
	public double getTransmissionCoefficient() {
		return transmissionCoefficient;
	}
	public double getRefractionIndex() {
		return refractionIndex;
	}
	public double getAbsorptionCoefficient() {
		return absorptionCoefficient;
	}
	public Shader getShader() {
		return shader;
	}
	public Color3f getColor() {
		return color;
	}
}
