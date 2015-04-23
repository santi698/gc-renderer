package model;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import static util.Vectors.*;

public class RayTracer {
	
	private Vector3d u, v, w;
	private Scene scene;
	private double pixelSize = 0.001;
	
	public RayTracer(Scene scene) {
//		this.executor = Executors.
		this.scene = scene;
	}
	
	public void setUp() {
		w = scale(scene.getCamera().getDirection(), -1);
		u = normalize(cross(scene.getCamera().getUp(), w));
		v = cross(w, u);
	}
	
	public BufferedImage render() {
//		List<CompletableFuture<?>> futures = new LinkedList<CompletableFuture<?>>();
		BufferedImage bi = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		setUp();
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				Ray ray = rayThroughPixel(i, j);
				Supplier<Color3f> s = ()-> ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
//				CompletableFuture<Color3f> tracing = CompletableFuture.supplyAsync(s); 
				setColor(i, j, bi).accept(s.get());
//				futures.add(tracing);
//				futures.add(tracing.thenAccept(setColor(i, j, bi)));
			}
		}
//		for (CompletableFuture<?> future : futures) {
//			try {
//				future.get();
//			} catch (Exception e) {
//				e.printStackTrace();
//			};
//		}
		return bi;
	}

	private Ray rayThroughPixel(int i, int j) {
		double x = pixelSize*(i - 0.5*scene.getCamera().getXRes());
		double y = pixelSize*(j - 0.5*scene.getCamera().getYRes());
		Vector3d direction = normalize(sub(add(scale(u, x), scale(v, y)), scale(w, scene.getCamera().getDistanceToCamera())));
		return new Ray(direction, scene.getCamera().getPosition());
	}
	private Consumer<Color3f> setColor(int i, int j, BufferedImage bi) {
		return (color) -> {
			bi.setRGB(i, j, color.get().getRGB());	
		};
	}
}