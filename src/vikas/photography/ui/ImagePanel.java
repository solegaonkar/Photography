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
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Constants;
import vikas.photography.framework.Database;
import vikas.photography.framework.Database.ImageType;
import vikas.photography.framework.Database.Record;
import vikas.photography.image.Photograph;

/**
 * @author vs0016025
 *
 */
public class ImagePanel extends JPanel {
	private static final long	serialVersionUID	= -3230373971732277427L;
	private BufferedImage		image				= null;

	public ImagePanel(BufferedImage image) {
		super();
		this.image = image;
	}

	public ImagePanel(Record record) throws Exception {
		super();
		if (record != null) {
			CommonUtils.log(Constants.BASE_DIRECTORY + File.separator + record.getRelativeFilePath());
			this.image =
					ImageIO.read(new File(Constants.BASE_DIRECTORY + File.separator + record.getRelativeFilePath()));
		} else {
			image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		}
	}

	public void update(Record record) throws Exception {
		if (record != null)
			this.image =
					ImageIO.read(new File(Constants.BASE_DIRECTORY + File.separator + record.getRelativeFilePath()));
		repaint();
	}

	public void createMouseListener(Photograph photograph, String parentFolder, ImageType type, Record record) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (record.getAlbums().isEmpty() || record.getInfo() == null || record.getInfo().length() == 0
						|| record.getTitle() == null || record.getTitle().length() == 0) {
					new DetailsDialog(record).setVisible(true);
				}
				if (!record.getAlbums().isEmpty() && record.getInfo() != null && record.getInfo().length() != 0
						&& record.getTitle() != null && record.getTitle().length() != 0) {
					try {
						File f = photograph.save(image, parentFolder, Constants.SaveQuality);
						record.setRelativePath(f.getAbsolutePath().substring(Constants.BASE_DIRECTORY.length() + 1));
						Database.addRecord(type, record);
						JOptionPane.showMessageDialog(null, String.format("Saved to path: %s%n Albums: %s",
								f.getAbsolutePath(), record.getAlbumString()), "Saved",
								JOptionPane.INFORMATION_MESSAGE);
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
}
