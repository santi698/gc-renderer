package model.texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Color3f;

public class ImageTexture implements Texture {
	
	private static final double GAMMA = 2.2;
	private BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
	private double scale = 1;
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
	@Override
	public Color3f get(double u, double v) {
		double unorm = u%scale;
		double vnorm = v%scale;
		if (unorm < 0)
			unorm = scale + unorm;
		if (vnorm < 0)
			vnorm = scale + vnorm;
		int x = (int)(unorm*image.getWidth()/(scale+1));
		int y = (int)(vnorm*image.getHeight()/(scale+1));
		if (x >= image.getWidth() || y >= image.getHeight() || x < 0 || y < 0) {
			System.out.println(unorm + " " + vnorm + " " + x + " " + y);
			return new Color3f();
		}
		Color3f color = new Color3f(new Color(image.getRGB(x, y)));
		color.x = (float) Math.pow(color.x, GAMMA);
		color.y = (float) Math.pow(color.y, GAMMA);
		color.z = (float) Math.pow(color.z, GAMMA);
		return color;
	}

}
