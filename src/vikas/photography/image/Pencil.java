package vikas.photography.image;

import java.awt.image.BufferedImage;

public class Pencil extends MatrixProcessor {

	@Override
	public BufferedImage process(BufferedImage image) {
		setImage(image);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				loadNeighbors(x, y);
				bi.setRGB(x, y, change.getRGB());
			}
		}
		return bi;
	}

}
