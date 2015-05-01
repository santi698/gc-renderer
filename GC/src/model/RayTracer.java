package model;

import static util.Vectors.add;
import static util.Vectors.cross;
import static util.Vectors.normalize;
import static util.Vectors.scale;
import static util.Vectors.sub;

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
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import model.materials.Material;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import util.Vectors;

public class RayTracer {
	public static final float invGamma = 1f/2.2f;
	private Vector3d u, v, w;
	private Scene scene;
	private boolean AAEnabled = true;
	private boolean showTime;
	private Sampler sampler;
	private double pixelSize;
	private long startTime;
	private DoubleProperty progress = new SimpleDoubleProperty();
	
	public DoubleProperty getProgressProperty() {
		return progress;
	}
	
	public RayTracer(Scene scene, int AASamples, int rayDepth, boolean showTime) {
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
			this.sampler = new Multijittered(scene.getAASamples());
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
			System.out.println("Renderizado en " + renderTime + " segundos ("+ 1/renderTime + ") FPS.");
		}
		return bi;
	}
	private Ray rayThroughPixel(int i, int j, Point2d lensSample) {
		return rayThroughPixel(i, j, new Point2d(0,0), lensSample);
	}
	private Ray rayThroughPixel(int i, int j, Point2d sample, Point2d lensSample) {
		double x = pixelSize*(i - 0.5*scene.getCamera().getXRes() + sample.x);
		double y = pixelSize*(j - 0.5*scene.getCamera().getYRes() + sample.y);
		Point3d lensPoint = new Point3d(scene.getCamera().getPosition());
		lensPoint.add(Vectors.scale(u, lensSample.x));
		lensPoint.add(Vectors.scale(v, lensSample.y));
		double ps = scene.getCamera().getFocalDistance()/scene.getCamera().getFocalLength();
		Vector3d direction = normalize(sub(add(scale(u, x*ps-lensSample.x), scale(v, y*ps-lensSample.y)), scale(w, scene.getCamera().getFocalDistance())));
		return new Ray(direction, lensPoint);
	}
	private Consumer<Color3f> colorSetter(int i, int j, BufferedImage bi) {
		return (color) -> {
			color.clamp(0, 1); //FIXME Guardar todo en una matriz y/o aplicar alguna compresión de rango no (o menos) destructiva.
			color.x = (float)Math.pow(color.x, invGamma);
			color.y = (float)Math.pow(color.y, invGamma);
			color.z = (float)Math.pow(color.z, invGamma);
			bi.setRGB(i, j, color.get().getRGB());
			int pixelsSet = ((i>0)?(i-1)*bi.getHeight():0) + j;
			progress.set(((double)pixelsSet/(bi.getWidth()*bi.getHeight())));
		};
	}
	private Supplier<Color3f> packetTracer(int i, int j) {
		return () -> {
			Color3f resultColor = new Color3f();
			Point2d[] lensSamples = scene.getCamera().sampleLens();
			if (AAEnabled) {
				for (int k = 0; k < scene.getAASamples(); k++) {
					Point2d sample = sampler.sampleUnitSquare();
					Ray ray;
					if (lensSamples.length == 1)
						ray = rayThroughPixel(i, j, sample, lensSamples[0]);
					else
						ray = rayThroughPixel(i, j, sample, lensSamples[k]);
					Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects(), 0, 0);
					resultColor.add(color);
				}
			} else {
				for (Point2d lensSample : lensSamples) {
					Ray ray = rayThroughPixel(i, j, lensSample);
					Color3f color = ray.trace(scene.getObjects()).shade(scene.getLights(), scene.getObjects(), 0, 0);
					resultColor.add(color);
				}
			}
			if (AAEnabled)
				resultColor.scale(1f/scene.getAASamples());
			else
				resultColor.scale(1f/lensSamples.length);
			return resultColor;
		};
	}
}