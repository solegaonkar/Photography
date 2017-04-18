package vikas.photography.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import vikas.photography.framework.CommonUtils;
import vikas.photography.image.Photograph.Processor;

/**
 * Several image processing algorithms will follow the matrix. Hence this generic base class with utility for all of
 * them.
 * 
 * @author vs0016025
 *
 */
public abstract class MatrixProcessor implements Processor {
	protected int[][]	nbhRgb		= new int[9][3];
	protected float[][]	nbhHsb		= new float[9][3];
	protected int[]		meanRgb		= new int[3];
	protected float[]	meanHsb		= new float[3];
	protected Color		change		= Color.black;

	protected int[]		rgb			= null;
	protected float[][]	hsb			= null;
	protected int		width		= 0;
	protected int		height		= 0;

	/**
	 * Load all possible information out of the image.
	 * 
	 * @param image
	 */
	protected void setImage(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		rgb = image.getRGB(0, 0, width, height, null, 0, width);
		CommonUtils.log("Image Loaded: " + width + "x" + height + ":" + rgb.length);

		hsb = new float[rgb.length][3];
		for (int i = 0; i < rgb.length; i++) {
			Color c = new Color(rgb[i]);
			Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb[i]);
		}
	}

	/**
	 * Load the neighbors of the given point into the nbh array.
	 * 
	 * @param x
	 * @param y
	 */
	protected void loadNeighbors(int x, int y) {
		int n = 0;
		meanRgb[0] = 0;
		meanRgb[1] = 0;
		meanRgb[2] = 0;
		meanHsb[0] = 0;
		meanHsb[1] = 0;
		meanHsb[2] = 0;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				Color c = new Color(
						(i >= 0 && j >= 0 && i < width && j < height) ? rgb[j * width + i] : rgb[y * width + x]);
				meanRgb[0] += (nbhRgb[n][0] = c.getRed());
				meanRgb[1] += (nbhRgb[n][1] = c.getGreen());
				meanRgb[2] += (nbhRgb[n][2] = c.getBlue());

				for (int v = 0; v < 3; v++) {
					meanHsb[v] += (nbhHsb[n][v] = (i >= 0 && j >= 0 && i < width && j < height) ? hsb[j * width + i][v]
							: hsb[y * width + x][v]);
				}

				change = new Color(
						Math.min(255,
								Math.abs((nbhRgb[6][0] + 2 * nbhRgb[7][0] + nbhRgb[8][0])
										- (nbhRgb[0][0] + 2 * nbhRgb[1][0] + nbhRgb[2][0]))
										+ Math.abs((nbhRgb[2][0] + 2 * nbhRgb[5][0] + nbhRgb[8][0])
												- (nbhRgb[0][0] + 2 * nbhRgb[3][0] + nbhRgb[6][0]))),
						Math.min(255,
								Math.abs((nbhRgb[6][1] + 2 * nbhRgb[7][1] + nbhRgb[8][1])
										- (nbhRgb[0][1] + 2 * nbhRgb[1][1] + nbhRgb[2][1]))
										+ Math.abs((nbhRgb[2][1] + 2 * nbhRgb[5][1] + nbhRgb[8][1])
												- (nbhRgb[0][1] + 2 * nbhRgb[3][1] + nbhRgb[6][1]))),
						Math.min(255,
								Math.abs((nbhRgb[6][2] + 2 * nbhRgb[7][2] + nbhRgb[8][2])
										- (nbhRgb[0][2] + 2 * nbhRgb[1][2] + nbhRgb[2][2]))
										+ Math.abs((nbhRgb[2][2] + 2 * nbhRgb[5][2] + nbhRgb[8][2])
												- (nbhRgb[0][2] + 2 * nbhRgb[3][2] + nbhRgb[6][2]))));
			}
		}
		for (int v = 0; v < 3; v++) {
			meanRgb[v] /= 9;
			meanHsb[v] /= 9;
		}
	}

}
