/**
 * 
 */
package vikas.photography.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import vikas.photography.framework.CommonUtils;
import vikas.photography.image.Photograph;

/**
 * @author vs0016025
 *
 */
public class ImagePanel extends JPanel {
	private static final long	serialVersionUID	= -3230373971732277427L;
	private BufferedImage		image				= null;

	public ImagePanel(Photograph photograph, String parentFolder) {
		super();
		this.image = photograph.getWip();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
						"If you like this image, I will save it in the appropriate folder...", "Save?",
						JOptionPane.OK_CANCEL_OPTION)) {
					try {
						photograph.save(image, parentFolder, 1f);
					} catch (Exception e) {
						CommonUtils.exception(e);
					}
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int h = 0, w = 0, x = 0, y = 0;

		if (image != null) {
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

	/**
	 * @param image
	 * the image to set
	 */
	public final void setImageInfo(Photograph photograph, String parentFolder) {
		CommonUtils.log("Create Image Panel Tab: " + photograph.getSourceFile());
	}
}
