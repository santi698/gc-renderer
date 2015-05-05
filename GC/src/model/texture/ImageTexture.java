package model.texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.vecmath.Color3f;

public class ImageTexture implements Texture {
	
	private static final double GAMMA = 2.2;
	private BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
	private double scale = 1;
	public ImageTexture(URL url) {
		try {
			image = ImageIO.read(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ImageTexture(File file) {
		try {
			image = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ImageTexture(String imagePath) {
		File file = new File(imagePath);
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ImageTexture(String imagePath, double scale) {
		this(imagePath);
		this.scale = 1d/scale;
	}
	public ImageTexture(URL url, double scale) {
		this(url);
		this.scale = 1d/scale;
	}
	@Override
	public Color3f get(double u, double v) {
		double unorm = (u*scale)%1;
		double vnorm = ((1-v)*scale)%1;
		if (unorm < 0)
			unorm = 1 + unorm;
		if (vnorm < 0)
			vnorm = 1 + vnorm;
		int x = (int)(unorm*image.getWidth());
		int y = (int)((1-vnorm)*image.getHeight());
		if (x >= image.getWidth() || y >= image.getHeight() || x < 0 || y < 0) {
			System.out.println("ERROR: " + unorm + " " + vnorm + " " + x + " " + y);
			return new Color3f();
		}
		Color3f color = new Color3f(new Color(image.getRGB(x, y)));
		color.x = (float) Math.pow(color.x, GAMMA);
		color.y = (float) Math.pow(color.y, GAMMA);
		color.z = (float) Math.pow(color.z, GAMMA);
		return color;
	}

}
