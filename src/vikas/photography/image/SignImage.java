package vikas.photography.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import vikas.photography.framework.CommonUtils;
import vikas.photography.image.Photograph.Processor;

public class SignImage implements Processor {
	private String signature;

	/**
	 * @param signature
	 */
	public SignImage(String signature) {
		this.signature = signature;
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		int ih, iw, sh, sw;
		int[] irgb;
		int[] srgb;
		ih = image.getHeight();
		iw = image.getWidth();

		// Did not dig too much into this. But it works. Most probably the font size has a 1:2 relation with the pixels.
		int fs = 2 * iw / ((signature.length() + 8));
		BufferedImage signatureImage = new BufferedImage(iw / 1, fs * 2, BufferedImage.TYPE_INT_RGB);
		sh = signatureImage.getHeight();
		sw = signatureImage.getWidth();
		CommonUtils.info("Signature Image Dimensions: " + sw + ":" + sh);

		Graphics2D g2d = signatureImage.createGraphics();
		g2d.setFont(new Font("Consolas", Font.ITALIC, fs));
		g2d.setColor(Color.WHITE);
		g2d.drawString("  " + signature, 0, 2 * sh / 3);
		g2d.dispose();

		srgb = signatureImage.getRGB(0, 0, sw, sh, null, 0, sw);
		irgb = image.getRGB(iw - sw, ih - sh, sw, sh, null, 0, sw);

		for (int i = 0; i < srgb.length; i++) {
			if ((srgb[i] & 0xffffff) > 0)
				irgb[i] = irgb[i] | (irgb[i] + 0x333333) & 0xffffff;
		}
		image.setRGB(iw - sw, ih - sh, sw, sh, irgb, 0, sw);
		return image;
	}

}
