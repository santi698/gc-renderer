package model;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;

import model.materials.Material;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import application.Main;

public class RayTracer {
	public static final float invGamma = 1f/2.2f;
	private static final boolean DEBUG = false;
	private Scene scene;
	private boolean AAEnabled = true;
	private boolean showTime;
	private Sampler sampler;
	private long startTime;
	private DoubleProperty progress = new SimpleDoubleProperty();
	int pixelsSet = 0;	
	
	public DoubleProperty getProgressProperty() {
		return progress;
	}
	
	public RayTracer(Scene scene, int AASamples, int rayDepth, boolean showTime) {
		if (DEBUG)
			System.out.println("Debug mode");
		if (rayDepth >= 0) {
			Material.REFLECTIONDEPTH = rayDepth;
			Material.REFRACTIONDEPTH = rayDepth;
		}
		//TODO Traer el control de las muestras de AA hasta acá.
		this.scene = scene;
		this.showTime = showTime;
	}
	public void setAA(boolean b) {
		AAEnabled = b;
	}
	public void setUp() {
		if (AAEnabled) {
			this.sampler = new Multijittered(Main.AASamples);
			sampler.generateSamples();
			sampler.genShuffledIndices();
		}
	}
	
	public BufferedImage render() {
		List<CompletableFuture<?>> futures = new LinkedList<CompletableFuture<?>>();
		BufferedImage image = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		setUp();
		startTime = System.currentTimeMillis();
		for (int i = 0; i < scene.getCamera().getXRes(); i++) {
			for (int j = 0; j < scene.getCamera().getYRes(); j++) {
				CompletableFuture<Color3f> tracing = CompletableFuture.supplyAsync(packetTracer(i, j));
				futures.add(tracing.thenAccept(colorSetter(i, j, image)));
				try {
					tracing.get();
				} catch (Exception e) {
					e.printStackTrace();
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
		if (showTime) {
			double renderTime = ((System.currentTimeMillis()-startTime)/1000f);
			System.out.printf("Renderizado en %f segundos (%f FPS).\n", renderTime ,1/renderTime );
		}
		return image;
	}
	private Consumer<Color3f> colorSetter(int i, int j, BufferedImage bi) {
		return (color) -> {
			float max = (Math.max(color.x, Math.max(color.y, color.z)));
			if (max>1)
				if (DEBUG)
					color.set(1,0,1);
				else
					color.scale(1f/max);
			color.x = (float)Math.pow(color.x, invGamma);
			color.y = (float)Math.pow(color.y, invGamma);
			color.z = (float)Math.pow(color.z, invGamma);
			bi.setRGB(i, j, color.get().getRGB());
			pixelsSet++;
			progress.set(((double)pixelsSet/(bi.getWidth()*bi.getHeight())));
		};
	}
	private Supplier<Color3f> packetTracer(int i, int j) {
		return () -> {
			Color3f resultColor = new Color3f();
			Point2d[] lensSamples = scene.getCamera().sampleLens();
			if (AAEnabled) {
				for (int k = 0; k < Main.AASamples; k++) {
					Point2d sample = sampler.sampleUnitSquare();
					Ray ray;
					if (lensSamples.length == 1)
						ray = scene.getCamera().rayThroughPixel(i + sample.x, j + sample.y, lensSamples[0]);
					else {
						ray = scene.getCamera().rayThroughPixel(i + sample.x, j + sample.y, lensSamples[k]);
					}
					Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects(), 0, 0);
					resultColor.add(color);
				}
			} else {
				for (Point2d lensSample : lensSamples) {
					Ray ray = scene.getCamera().rayThroughPixel(i, j, lensSample);
					Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects(), 0, 0);
					resultColor.add(color);
				}
			}
			if (AAEnabled)
				resultColor.scale(1f/Main.AASamples);
			else
				resultColor.scale(1f/lensSamples.length);
			return resultColor;
		};
	}
}