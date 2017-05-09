/**
 * 
 */
package org.github.solegaonkar.photography.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author vs0016025
 *
 */
public class ImagePanel extends JPanel {
	private static final long	serialVersionUID	= -3230373971732277427L;
	private BufferedImage		image				= null;

	public ImagePanel() {
	}
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			int h = 0, w = 0, x = 0, y = 0;
			if ((image.getHeight() * this.getWidth()) > (this.getHeight() * image.getWidth())) {
				h = this.getHeight() - 20;
				w = h * image.getWidth() / image.getHeight();
				x = (getWidth() - w) / 2;
				y = 10;
			} else {
				w = this.getWidth() - 20;
				h = w * image.getHeight() / image.getWidth();
				x = 10;
				y = (getHeight() - h) / 2;
			}
			g.setColor(Color.black);
			g.fillRect(x - 10, y - 10, w + 20, h + 20);
			g.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
		}
	}
}
