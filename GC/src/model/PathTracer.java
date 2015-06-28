package model;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;

import model.materials.Material;
import model.samplers.Multijittered;
import model.samplers.Sampler;
import model.trees.DummyTree;
import scenes.Scene;
import application.Main;

public class PathTracer {
	private DummyTree sceneTree;
	public static final float invGamma = 1f/2.2f;
	private static final boolean DEBUG = false;
	private static final boolean THREADINGDISABLED = false;
	private int samplesPerPixel = Main.AASamples;
	private int packetSize = 4;
	private Scene scene;
	private boolean showTime;
	private Sampler sampler;
	private long startTime;
	private DoubleProperty progress = new SimpleDoubleProperty();
	private int samplesSet = 0;
	
	public DoubleProperty getProgressProperty() {
		return progress;
	}
	
	public PathTracer(Scene scene, int AASamples, int rayDepth, boolean showTime) {
		if (DEBUG)
			System.out.println("Debug mode");
		if (rayDepth >= 0) {
			Material.REFLECTIONDEPTH = rayDepth;
			Material.REFRACTIONDEPTH = rayDepth;
		}
		//TODO Traer el control de las muestras de AA hasta acá.
		this.scene = scene;
		this.showTime = showTime;
		
		this.sceneTree = new DummyTree();
		sceneTree.addAll(scene.getObjects());		

	}
	public void setUp() {
		this.sampler = new Multijittered(samplesPerPixel);
		sampler.generateSamples();
		sampler.genShuffledIndices();
	}
	
	public BufferedImage render() {
		List<CompletableFuture<?>> futures = new LinkedList<CompletableFuture<?>>();
		BufferedImage image = new BufferedImage(scene.getCamera().getXRes(), scene.getCamera().getYRes(), BufferedImage.TYPE_INT_RGB);
		setUp();
		startTime = System.currentTimeMillis();
		for (int i = 0; i < scene.getCamera().getXRes()-(packetSize-1); i+=packetSize) {
			for (int j = 0; j < scene.getCamera().getYRes()-(packetSize-1); j+=packetSize) {
				CompletableFuture<Void> tracing = CompletableFuture.runAsync(packetTracer(i, j, image));
				futures.add(tracing);
				if (THREADINGDISABLED) {
					try {
						tracing.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
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
			samplesSet++;
			progress.set(((double)samplesSet/(bi.getWidth()*bi.getHeight()*samplesPerPixel)));
		};
	}
	private Runnable packetTracer(int i, int j, BufferedImage bi) {
		return () -> {
			int packetSizeX = this.packetSize;
			int packetSizeY = this.packetSize;
			if (i > scene.getCamera().getXRes()-2*packetSize) //Si estoy en la ultima columna
				packetSizeX += scene.getCamera().getXRes()%packetSize;
			if (j > scene.getCamera().getYRes()-2*packetSize) //Si estoy en la ultima fila
				packetSizeY += scene.getCamera().getYRes()%packetSize;
			
			for (int x = 0; x < packetSizeX; x++) {
				for (int y = 0; y < packetSizeY; y++) {
					Color3f resultColor = new Color3f();
					Point2d lensSample = scene.getCamera().sampleLens();
					for (int k = 0; k < samplesPerPixel; k++) {
						Point2d sample = sampler.sampleUnitSquare();
						Ray ray = scene.getCamera().rayThroughPixel(i + x + sample.x, j + y + sample.y, lensSample);
						Color3f directColor = sceneTree.trace(ray).directShade(scene.getLights(), scene.getObjects(), 0, 0);
						resultColor.add(directColor);
						
//						Color3f indirectColor = sceneTree.trace(ray).indirectShade(scene.getLights(), scene.getObjects(), 0, 0);
//						resultColor.add(indirectColor);
					}
					resultColor.scale(1f/samplesPerPixel);
					colorSetter(i+x, j+y, bi).accept(resultColor);
				}
			}
		};
	}
}