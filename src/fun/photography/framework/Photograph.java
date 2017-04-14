package fun.photography.framework;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.TileObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

/**
 * A Utility container class that holds a BufferedImage. It delegates all the functions of the buffered Image and also
 * creates some of its own.
 * 
 * @author Vikas
 */
public class Photograph implements Cloneable {
	private BufferedImage bufferedImage;

	/**
	 * Load from the given image file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public Photograph(File file) throws IOException {
		bufferedImage = ImageIO.read(file);
	}

	/**
	 * New object that holds this buffered image.
	 * 
	 * @param bufferedImage
	 */
	public Photograph(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	/**
	 * Create a new BufferedImage based on these parameters and hold it within
	 * 
	 * @param width
	 * @param height
	 * @param imageType
	 * @param cm
	 */
	public Photograph(int width, int height, int imageType, IndexColorModel cm) {
		this(new BufferedImage(width, height, imageType, cm));
	}

	/**
	 * Create a new BufferedImage based on these parameters and hold it within
	 * 
	 * @param width
	 * @param height
	 * @param imageType
	 */
	public Photograph(int width, int height, int imageType) {
		this(new BufferedImage(width, height, imageType));
	}

	/**
	 * Create a new BufferedImage based on these parameters and hold it within
	 * 
	 * @param cm
	 * @param raster
	 * @param isRasterPremultiplied
	 * @param properties
	 */
	public Photograph(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
		this(new BufferedImage(cm, raster, isRasterPremultiplied, properties));
	}

	/**
	 * Save the image to the given file - in JPG format.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public final void save(File file) throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(1f);
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

		writer.setOutput(new FileImageOutputStream(file));
		writer.write(null, new IIOImage(bufferedImage, null, null), jpegParams);
	}

	/**
	 * Create a deep copy of the object.
	 */
	public final Photograph clone() {
		ColorModel cm = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(null);
		return new Photograph(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Get the inner image
	 * 
	 * @return
	 */
	public final BufferedImage getImage() {
		return bufferedImage;
	}

	/**
	 * 
	 * @param op
	 */
	public final void filter(BufferedImageOp op) {
		this.bufferedImage = op.filter(bufferedImage, bufferedImage);
	}

	/**
	 * Scale the image to the new width and height
	 * 
	 * @param width
	 * @param height
	 */
	public final void scale(int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(this.bufferedImage, 0, 0, width, height, null);
		g.dispose();
		this.bufferedImage = resizedImage;
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return bufferedImage.hashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return bufferedImage.equals(obj);
	}

	/**
	 * @param width
	 * @param height
	 * @param hints
	 * @return
	 * @see java.awt.Image#getScaledInstance(int, int, int)
	 */
	public Image getScaledInstance(int width, int height, int hints) {
		return bufferedImage.getScaledInstance(width, height, hints);
	}

	/**
	 * 
	 * @see java.awt.Image#flush()
	 */
	public void flush() {
		bufferedImage.flush();
	}

	/**
	 * @param gc
	 * @return
	 * @see java.awt.Image#getCapabilities(java.awt.GraphicsConfiguration)
	 */
	public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
		return bufferedImage.getCapabilities(gc);
	}

	/**
	 * @param priority
	 * @see java.awt.Image#setAccelerationPriority(float)
	 */
	public void setAccelerationPriority(float priority) {
		bufferedImage.setAccelerationPriority(priority);
	}

	/**
	 * @return
	 * @see java.awt.Image#getAccelerationPriority()
	 */
	public float getAccelerationPriority() {
		return bufferedImage.getAccelerationPriority();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getType()
	 */
	public int getType() {
		return bufferedImage.getType();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getColorModel()
	 */
	public ColorModel getColorModel() {
		return bufferedImage.getColorModel();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getRaster()
	 */
	public WritableRaster getRaster() {
		return bufferedImage.getRaster();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getAlphaRaster()
	 */
	public WritableRaster getAlphaRaster() {
		return bufferedImage.getAlphaRaster();
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see java.awt.image.BufferedImage#getRGB(int, int)
	 */
	public int getRGB(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	/**
	 * @param startX
	 * @param startY
	 * @param w
	 * @param h
	 * @param rgbArray
	 * @param offset
	 * @param scansize
	 * @return
	 * @see java.awt.image.BufferedImage#getRGB(int, int, int, int, int[], int, int)
	 */
	public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
		return bufferedImage.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	/**
	 * @param x
	 * @param y
	 * @param rgb
	 * @see java.awt.image.BufferedImage#setRGB(int, int, int)
	 */
	public void setRGB(int x, int y, int rgb) {
		bufferedImage.setRGB(x, y, rgb);
	}

	/**
	 * @param startX
	 * @param startY
	 * @param w
	 * @param h
	 * @param rgbArray
	 * @param offset
	 * @param scansize
	 * @see java.awt.image.BufferedImage#setRGB(int, int, int, int, int[], int, int)
	 */
	public void setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
		bufferedImage.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getWidth()
	 */
	public int getWidth() {
		return bufferedImage.getWidth();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getHeight()
	 */
	public int getHeight() {
		return bufferedImage.getHeight();
	}

	/**
	 * @param observer
	 * @return
	 * @see java.awt.image.BufferedImage#getWidth(java.awt.image.ImageObserver)
	 */
	public int getWidth(ImageObserver observer) {
		return bufferedImage.getWidth(observer);
	}

	/**
	 * @param observer
	 * @return
	 * @see java.awt.image.BufferedImage#getHeight(java.awt.image.ImageObserver)
	 */
	public int getHeight(ImageObserver observer) {
		return bufferedImage.getHeight(observer);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getSource()
	 */
	public ImageProducer getSource() {
		return bufferedImage.getSource();
	}

	/**
	 * @param name
	 * @param observer
	 * @return
	 * @see java.awt.image.BufferedImage#getProperty(java.lang.String, java.awt.image.ImageObserver)
	 */
	public Object getProperty(String name, ImageObserver observer) {
		return bufferedImage.getProperty(name, observer);
	}

	/**
	 * @param name
	 * @return
	 * @see java.awt.image.BufferedImage#getProperty(java.lang.String)
	 */
	public Object getProperty(String name) {
		return bufferedImage.getProperty(name);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getGraphics()
	 */
	public Graphics getGraphics() {
		return bufferedImage.getGraphics();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#createGraphics()
	 */
	public Graphics2D createGraphics() {
		return bufferedImage.createGraphics();
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @see java.awt.image.BufferedImage#getSubimage(int, int, int, int)
	 */
	public Photograph getSubimage(int x, int y, int w, int h) {
		return new Photograph(bufferedImage.getSubimage(x, y, w, h));
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#isAlphaPremultiplied()
	 */
	public boolean isAlphaPremultiplied() {
		return bufferedImage.isAlphaPremultiplied();
	}

	/**
	 * @param isAlphaPremultiplied
	 * @see java.awt.image.BufferedImage#coerceData(boolean)
	 */
	public void coerceData(boolean isAlphaPremultiplied) {
		bufferedImage.coerceData(isAlphaPremultiplied);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#toString()
	 */
	public String toString() {
		return bufferedImage.toString();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getSources()
	 */
	public Vector<RenderedImage> getSources() {
		return bufferedImage.getSources();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getPropertyNames()
	 */
	public String[] getPropertyNames() {
		return bufferedImage.getPropertyNames();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getMinX()
	 */
	public int getMinX() {
		return bufferedImage.getMinX();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getMinY()
	 */
	public int getMinY() {
		return bufferedImage.getMinY();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getSampleModel()
	 */
	public SampleModel getSampleModel() {
		return bufferedImage.getSampleModel();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getNumXTiles()
	 */
	public int getNumXTiles() {
		return bufferedImage.getNumXTiles();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getNumYTiles()
	 */
	public int getNumYTiles() {
		return bufferedImage.getNumYTiles();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getMinTileX()
	 */
	public int getMinTileX() {
		return bufferedImage.getMinTileX();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getMinTileY()
	 */
	public int getMinTileY() {
		return bufferedImage.getMinTileY();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getTileWidth()
	 */
	public int getTileWidth() {
		return bufferedImage.getTileWidth();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getTileHeight()
	 */
	public int getTileHeight() {
		return bufferedImage.getTileHeight();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getTileGridXOffset()
	 */
	public int getTileGridXOffset() {
		return bufferedImage.getTileGridXOffset();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getTileGridYOffset()
	 */
	public int getTileGridYOffset() {
		return bufferedImage.getTileGridYOffset();
	}

	/**
	 * @param tileX
	 * @param tileY
	 * @return
	 * @see java.awt.image.BufferedImage#getTile(int, int)
	 */
	public Raster getTile(int tileX, int tileY) {
		return bufferedImage.getTile(tileX, tileY);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getData()
	 */
	public Raster getData() {
		return bufferedImage.getData();
	}

	/**
	 * @param rect
	 * @return
	 * @see java.awt.image.BufferedImage#getData(java.awt.Rectangle)
	 */
	public Raster getData(Rectangle rect) {
		return bufferedImage.getData(rect);
	}

	/**
	 * @param outRaster
	 * @return
	 * @see java.awt.image.BufferedImage#copyData(java.awt.image.WritableRaster)
	 */
	public WritableRaster copyData(WritableRaster outRaster) {
		return bufferedImage.copyData(outRaster);
	}

	/**
	 * @param r
	 * @see java.awt.image.BufferedImage#setData(java.awt.image.Raster)
	 */
	public void setData(Raster r) {
		bufferedImage.setData(r);
	}

	/**
	 * @param to
	 * @see java.awt.image.BufferedImage#addTileObserver(java.awt.image.TileObserver)
	 */
	public void addTileObserver(TileObserver to) {
		bufferedImage.addTileObserver(to);
	}

	/**
	 * @param to
	 * @see java.awt.image.BufferedImage#removeTileObserver(java.awt.image.TileObserver)
	 */
	public void removeTileObserver(TileObserver to) {
		bufferedImage.removeTileObserver(to);
	}

	/**
	 * @param tileX
	 * @param tileY
	 * @return
	 * @see java.awt.image.BufferedImage#isTileWritable(int, int)
	 */
	public boolean isTileWritable(int tileX, int tileY) {
		return bufferedImage.isTileWritable(tileX, tileY);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getWritableTileIndices()
	 */
	public Point[] getWritableTileIndices() {
		return bufferedImage.getWritableTileIndices();
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#hasTileWriters()
	 */
	public boolean hasTileWriters() {
		return bufferedImage.hasTileWriters();
	}

	/**
	 * @param tileX
	 * @param tileY
	 * @return
	 * @see java.awt.image.BufferedImage#getWritableTile(int, int)
	 */
	public WritableRaster getWritableTile(int tileX, int tileY) {
		return bufferedImage.getWritableTile(tileX, tileY);
	}

	/**
	 * @param tileX
	 * @param tileY
	 * @see java.awt.image.BufferedImage#releaseWritableTile(int, int)
	 */
	public void releaseWritableTile(int tileX, int tileY) {
		bufferedImage.releaseWritableTile(tileX, tileY);
	}

	/**
	 * @return
	 * @see java.awt.image.BufferedImage#getTransparency()
	 */
	public int getTransparency() {
		return bufferedImage.getTransparency();
	}
}
