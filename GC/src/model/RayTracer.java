package model;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Vector3d;

import static util.Vectors.*;

public class RayTracer {
	
	private int AASamples = 16;
	private Vector3d u, v, w;
	private Scene scene;
	private boolean AAEnabled = true;
	private Sampler sampler;
	private double pixelSize = 0.001;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	public void setAA(boolean b) {
		AAEnabled = b;
	}
	public void setSamples(int samples) {
		this.AASamples = samples;
	}
	public void setUp() {
		if (AAEnabled) {
			this.sampler = new Multijittered(AASamples);
			sampler.generateSamples();
			sampler.genShuffledIndices();
		}
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
				Color3f resultColor = new Color3f();
				if (AAEnabled) {
					for (int k = 0; k < AASamples; k++) {
						Point2d sample = sampler.sampleUnitSquare();
						Ray ray = rayThroughPixel(i, j, sample);
						Supplier<Color3f> s = ()-> ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
						resultColor.add(s.get());
		//				CompletableFuture<Color3f> tracing = CompletableFuture.supplyAsync(s); 
		//				futures.add(tracing);
		//				futures.add(tracing.thenAccept(setColor(i, j, bi)));
					}
					resultColor.scale(1f/AASamples);
				} else {
					Ray ray = rayThroughPixel(i, j);
					Supplier<Color3f> s = ()-> ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
					resultColor = s.get();
				}
				setColor(i, j, bi).accept(resultColor);

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
		return rayThroughPixel(i, j, new Point2d(0,0));
	}
	private Ray rayThroughPixel(int i, int j, Point2d sample) {
		double x = pixelSize*(i - 0.5*scene.getCamera().getXRes() + sample.x);
		double y = pixelSize*(j - 0.5*scene.getCamera().getYRes() + sample.y);
		Vector3d direction = normalize(sub(add(scale(u, x), scale(v, y)), scale(w, scene.getCamera().getDistanceToCamera())));
		return new Ray(direction, scene.getCamera().getPosition());
	}
	private Consumer<Color3f> setColor(int i, int j, BufferedImage bi) {
		return (color) -> {
			bi.setRGB(i, j, color.get().getRGB());	
		};
	}
}