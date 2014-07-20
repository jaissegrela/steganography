package core.message;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.utils.Utils;

/**
 * Represents an image file message, it can be used for message and cover
 * message
 * 
 * @author Jaisse Grela
 */
public class BasicImageMessage extends CacheMessage implements ICoverMessage {

	protected String imageName;

	/**
	 * @param image
	 *            the image file
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if image is null
	 */
	public BasicImageMessage(File image) throws IOException {
		this(toArrayOfByte(image), image.getPath());
	}

	/**
	 * @param filename
	 *            the path of the image
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if image is null
	 */
	public BasicImageMessage(String filename) throws IOException {
		this(new File(filename));
	}

	protected BasicImageMessage(byte[] cache, String imageName) {
		super(cache, Utils.getExtension(imageName));
		this.imageName = imageName;
	}
	
	public BasicImageMessage(byte[] cache) {
		super(cache);
	}

	public static byte[] toArrayOfByte(File image) throws IOException {
		BufferedImage img = ImageIO.read(image);
		WritableRaster raster = img.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		return data.getData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.ICoverMessage#setByte(int, byte)
	 */
	@Override
	public void setByte(int index, byte value) {
		cache[index] = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.ICoverMessage#duplicateMessage()
	 */
	@Override
	public ICoverMessage duplicateMessage() {
		return new BasicImageMessage(cache.clone(), imageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.ICoverMessage#save(java.lang.String)
	 */
	@Override
	public void save(OutputStream stream) throws IOException {
		File imgPath = new File(imageName);
		BufferedImage img = ImageIO.read(imgPath);
		WritableRaster raster = img.getRaster();
		byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();
		System.arraycopy(cache, 0, data, 0, data.length);
		ImageIO.write(img, Utils.getExtension(imgPath), stream);
		stream.close();
	}
	
	public Mat getMat(int flags){
		return Highgui.imread(imageName, flags);
	}
}
