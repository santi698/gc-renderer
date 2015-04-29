package model;

import model.cameras.Camera;
import model.light.Light;

public class Scene {
	private Light[] lights;
	private Body[] objects;
	private Camera camera;
	private int AASamples;
	public Scene(Light[] lights, Body[] objects, Camera camera, int AASamples) {
		super();
		this.lights = lights;
		this.objects = objects;
		this.camera = camera;
		this.AASamples = AASamples;
	}
	public Light[] getLights() {
		return lights;
	}
	public Body[] getObjects() {
		return objects;
	}
	public Camera getCamera() {
		return camera;
	}
	public int getAASamples() {
		return AASamples;
	}
}
