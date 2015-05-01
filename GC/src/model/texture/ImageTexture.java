package model.texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Color3f;

public class ImageTexture implements Texture {
	
	private BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
	
	public ImageTexture(String imagePath) {
		File file = new File(imagePath);
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public Color3f get(double u, double v) {
		int x = (int)(u*image.getWidth());
		int y = (int)(v*image.getHeight());
		Color3f color = new Color3f(new Color(image.getRGB(x, y)));
		color.x = (float) Math.pow(color.x, 2.2);
		color.y = (float) Math.pow(color.y, 2.2);
		color.z = (float) Math.pow(color.z, 2.2);
		return color;
	}

}
