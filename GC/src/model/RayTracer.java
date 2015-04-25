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
	private double pixelSize;
	private long startTime;
	
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
		this.pixelSize = scene.getCamera().getPixelSize();
		w = scale(scene.getCamera().getDirection(), -1);
		u = normalize(cross(scene.getCamera().getUp(), w));
		v = cross(w, u);
	}
	
	public BufferedImage render() {
		List<CompletableFuture<?>> futures = new LinkedList<CompletableFuture<?>>();
		BufferedImage bi = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		setUp();
		startTime = System.currentTimeMillis();
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				CompletableFuture<Color3f> tracing = CompletableFuture.supplyAsync(packetTracer(i, j));
				futures.add(tracing.thenAccept(colorSetter(i, j, bi)));
				try {
					tracing.get(); //FIXME sin bloquear la concurrencia se producen glitches.
				} catch (Exception e) {
					;	
				}
			}
		}
		for (CompletableFuture<?> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
		System.out.println("Renderizado a: " + 1/((System.currentTimeMillis()-startTime)/1000f) + " FPS.");
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
	private Consumer<Color3f> colorSetter(int i, int j, BufferedImage bi) {
		return (color) -> {
			bi.setRGB(i, j, color.get().getRGB());
		};
	}
	private Supplier<Color3f> packetTracer(int i, int j) {
		return () -> {
			Color3f resultColor = new Color3f();
			if (AAEnabled) {
				for (int k = 0; k < AASamples; k++) {
					Point2d sample = sampler.sampleUnitSquare();
					Ray ray = rayThroughPixel(i, j, sample);
					Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
					resultColor.add(color);
				}
				resultColor.scale(1f/AASamples);
			} else {
				Ray ray = rayThroughPixel(i, j);
				resultColor = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects());
			}
			return resultColor;
		};
	}
}