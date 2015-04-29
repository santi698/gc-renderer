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
		return new Color3f(new Color(image.getRGB(x, y)));
	}

}
