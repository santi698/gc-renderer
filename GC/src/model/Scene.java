package model;

import model.light.Light;

public class Scene {
	private Light[] lights;
	private Body[] objects;
	private Camera camera;
	public Scene(Light[] lights, Body[] objects, Camera camera) {
		super();
		this.lights = lights;
		this.objects = objects;
		this.camera = camera;
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
}
