package scenes;

import java.util.List;

import model.Body;
import model.cameras.Camera;
import model.light.Light;
import model.trees.Traceable;

public interface Scene {
	public List<Light> getLights();
	public List<Body> getObjects();
	public Camera getCamera();
}
