package fun.photography.processors;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SharpenerProc {

	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("input1.jpg"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		int width = img.getWidth();
		int height = img.getHeight();

		BufferedImage OutputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage OutputImg_sobel = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage OutputImg_mean = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage OutputImg_nor = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage OutputImg_final = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {

				if (w == 0 || w == width - 1 || h == 0 || h == height - 1) {
					Color Pass = new Color(img.getRGB(w, h));
					int rgb = Pass.getRGB();
					OutputImg.setRGB(w, h, rgb);
				} else {
					int[] r = new int[9];
					int[] g = new int[9];
					int[] b = new int[9];

					findNeighbors(img, w, h, r, g, b);

					Color result =
							new Color(
									Math.round(Math.max(0,
											Math.min(255,
													(float) r[0] - (float) (r[1] + r[2] + r[3] + r[4] + r[5] + r[6]
															+ r[7] + r[8]) / 8))),
									Math.round(Math.max(0,
											Math.min(255,
													(float) r[0] - (float) (g[1] + g[2] + g[3] + g[4] + g[5] + g[6]
															+ g[7] + g[8]) / 8))),
									Math.round(Math.max(0, Math.min(255, (float) r[0]
											- (float) (b[1] + b[2] + b[3] + b[4] + b[5] + b[6] + b[7] + b[8]) / 8))));
					OutputImg.setRGB(w, h, result.getRGB());

					Color result_sobel =
							new Color(
									Math.min(255,
											Math.max(0,
													Math.abs((r[6] + 2 * r[7] + r[8]) - (r[1] + 2 * r[2] + r[3])) + Math
															.abs((r[3] + 2 * r[5] + r[8]) - (r[1] + 2 * r[4] + r[6])))),
									Math.min(255,
											Math.max(0,
													Math.abs((g[6] + 2 * g[7] + g[8]) - (g[1] + 2 * g[2] + g[3])) + Math
															.abs((g[3] + 2 * g[5] + g[8]) - (g[1] + 2 * g[4] + g[6])))),
									Math.min(255,
											Math.max(0, Math.abs((b[6] + 2 * b[7] + b[8]) - (b[1] + 2 * b[2] + b[3]))
													+ Math.abs((b[3] + 2 * b[5] + b[8]) - (b[1] + 2 * b[4] + b[6])))));
					OutputImg_sobel.setRGB(w, h, result_sobel.getRGB());
				} // end of else
			} // end of h
		} // end of w

		float maxR = 0, minR = 255, maxG = 0, minG = 255, maxB = 0, minB = 255;
		// Mean Filter
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				if (w == 0 || w == width - 1 || h == 0 || h == height - 1) {
					OutputImg_mean.setRGB(w, h, new Color(OutputImg_sobel.getRGB(w, h)).getRGB());
				} else {
					int[] r = new int[9];
					int[] g = new int[9];
					int[] b = new int[9];

					float mR, mG, mB;

					findNeighbors(OutputImg_sobel, w, h, r, g, b);

					mR = Math.min(255,
							Math.max(0, (float) (r[1] + r[2] + r[3] + r[4] + r[5] + r[6] + r[7] + r[8] + r[0]) / 9));
					mG = Math.min(255,
							Math.max(0, (float) (g[1] + g[2] + g[3] + g[4] + g[5] + g[6] + g[7] + g[8] + g[0]) / 9));
					mB = Math.min(255,
							Math.max(0, (float) (b[1] + b[2] + b[3] + b[4] + b[5] + b[6] + b[7] + b[8] + b[0]) / 9));

					Color result_mean = new Color((int) mR, (int) mG, (int) mB);
					OutputImg_mean.setRGB(w, h, result_mean.getRGB());

					if (mR > maxR)
						maxR = mR;
					if (mR < minR)
						minR = mR;
					if (mG > maxG)
						maxG = mG;
					if (mG < minG)
						minG = mG;
					if (mB > maxB)
						maxB = mB;
					if (mB < minB)
						minB = mB;

					Color NorImage = new Color(OutputImg.getRGB(w, h));
					Color OriImage = new Color(img.getRGB(w, h));

					float NorRed = Math.min(255, Math.max(0, (float) NorImage.getRed() * mR / (maxR - minR)));
					float NorGreen = Math.min(255, Math.max(0, (float) NorImage.getGreen() * mG / (maxG - minG)));
					float NorBlue = Math.min(255, Math.max(0, (float) NorImage.getBlue() * mB / (maxB - minB)));

					OutputImg_nor.setRGB(w, h, new Color((int) NorRed, (int) NorGreen, (int) NorBlue).getRGB());
					Color result_final = new Color((int) Math.min(255, Math.max(0, NorRed + (float) OriImage.getRed())),
							(int) Math.min(255, Math.max(0, NorGreen + (float) OriImage.getGreen())),
							(int) Math.min(255, Math.max(0, NorBlue + (float) OriImage.getBlue())));
					OutputImg_final.setRGB(w, h, result_final.getRGB());
				}
			}
		} // end of for

		try {
			File outputfile = new File("result.jpg");
			ImageIO.write(OutputImg, "jpg", outputfile);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		try {
			File outputfile2 = new File("result_sobel.jpg");
			ImageIO.write(OutputImg_sobel, "jpg", outputfile2);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		try {
			File outputfile3 = new File("result_mean.jpg");
			ImageIO.write(OutputImg_mean, "jpg", outputfile3);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		try {
			File outputfile4 = new File("result_nor.jpg");
			ImageIO.write(OutputImg_nor, "jpg", outputfile4);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		try {
			File outputfile5 = new File("result_final.jpg");
			ImageIO.write(OutputImg_final, "jpg", outputfile5);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	private static void findNeighbors(BufferedImage OutputImg_sobel, int w, int h, int[] r, int[] g, int[] b) {
		findPointColor(OutputImg_sobel, w - 1, h - 1, r, g, b, 1);
		findPointColor(OutputImg_sobel, w, h - 1, r, g, b, 2);
		findPointColor(OutputImg_sobel, w + 1, h - 1, r, g, b, 3);
		findPointColor(OutputImg_sobel, w - 1, h, r, g, b, 4);
		findPointColor(OutputImg_sobel, w + 1, h, r, g, b, 5);
		findPointColor(OutputImg_sobel, w - 1, h + 1, r, g, b, 6);
		findPointColor(OutputImg_sobel, w, h + 1, r, g, b, 7);
		findPointColor(OutputImg_sobel, w + 1, h + 1, r, g, b, 8);
		findPointColor(OutputImg_sobel, w, h, r, g, b, 0);
	}

	private static void findPointColor(BufferedImage img, int w, int h, int[] r, int[] g, int[] b, int i) {
		Color c1 = new Color(img.getRGB(w, h));
		r[i] = c1.getRed();
		g[i] = c1.getGreen();
		b[i] = c1.getBlue();
	}
}
