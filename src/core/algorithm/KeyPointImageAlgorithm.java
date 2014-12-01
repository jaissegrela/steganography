package core.algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_highgui;

import core.message.BasicImageMessage;
import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointImageAlgorithm implements ISteganographyAlgorithm {

	protected ICoverMessage coverMessage;
	protected double visibilityfactor;
	protected ICoverMessage original;
	protected int keyPointSize, quantity;
	protected ISteganographyAlgorithm steganoAlgorithm;

	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public KeyPointImageAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm,
			double visibilityfactor, int keyPointSize, int quantity) {
		this(coverMessage, steganoAlgorithm, visibilityfactor, keyPointSize, quantity, null);
	}

	public KeyPointImageAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm,
			double visibilityfactor, int keyPointSize, int quantity, ICoverMessage original) {
		this.coverMessage = coverMessage;
		this.steganoAlgorithm = steganoAlgorithm;
		this.keyPointSize = keyPointSize;
		this.quantity = quantity;
		this.visibilityfactor = visibilityfactor;
		this.original = original;
	}

	@Override
	public double getStegoObjectRate(IMessage embeddedData) {
		return 1;
	}

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {

		ICoverMessage result = coverMessage.duplicateMessage();

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(new KeyPointEnumeration(result.getMat(),
				keyPointSize), quantity);
		String messageID = getMessage(embeddedData);
		BasicImageMessage message = getMessage(keyPointSize >> 1, messageID);

		Mat source = result.getMat();
		source.convertTo(source, opencv_core.CV_64FC1);

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect.clone()));
			MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(message);
			stegoObject.getMat().copyTo(imagRect);
		}

		return result;
	}

	public Mat transform(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm, int keyPointSize,
			int quantity) throws IOException {

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(new KeyPointEnumeration(source, keyPointSize),
				quantity);
		BasicImageMessage message = getMessage(keyPointSize >> 1, "abc");

		Mat result = new Mat();
		source.convertTo(result, opencv_core.CV_64FC1);

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat imagRect = KeyPointOperation.getMatPoint(result, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect.clone()));
			MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(message);
			stegoObject.getMat().copyTo(imagRect);
		}

		opencv_highgui.imwrite(outImagePath, result);

		return result;
	}

	protected String getMessage(IMessage embeddedData) {
		byte[] temp = new byte[embeddedData.bytes()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = embeddedData.getByte(i);
		}
		return new String(temp);
	}

	@Override
	public boolean hasHiddenMessage() {
		return true;
	}

	@Override
	public byte[] getTypeMessage() {
		return null;
	}

	@Override
	public byte[] getEmbeddedData() {
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(new KeyPointEnumeration(original.getMat(),
				keyPointSize), quantity);

		ImageFactory factory = new ImageFactory();

		Mat source = coverMessage.getMat().clone();

		if (!source.size().equals(original.getMat().size())) {
			source = ImageFactory.resizeImage(source, original.getMat().size());
		}

		source.convertTo(source, opencv_core.CV_64FC1);

		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		BufferedImage image;

		int keyPointSize = this.keyPointSize >> 1;

		ArrayList<byte[]> messages = new ArrayList<byte[]>();

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			// it is no necessary to clone matrix because the algorithm will not modify it
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect)); 
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
			messages.add(embeddedData);
			image = factory.createImage(keyPointSize, keyPointSize, embeddedData);
			// image = ImageFactory.filter(image);
			images.add(image);
		}
		image = ImageFactory.createMergeImage(images);
		// image = ImageFactory.filter(image);
		return factory.getBytes(image);
	}

	public void inverse(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm, int keyPointSize,
			int quantity, Mat original) throws IOException {

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(new KeyPointEnumeration(original, keyPointSize),
				quantity);

		ImageFactory factory = new ImageFactory();
		source.convertTo(source, opencv_core.CV_64FC1);

		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		keyPointSize >>= 1;

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			// it is no necessary to clone matrix because the algorithm will not modify it
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect));
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
			BufferedImage image = factory.createImage(keyPointSize, keyPointSize, embeddedData);
			image = ImageFactory.filter(image);
			images.add(image);
		}

		BufferedImage image = ImageFactory.createMergeImage(images);
		if (image != null)
			ImageIO.write(image, "bmp", new File(outImagePath + ".bmp"));
	}

	@Override
	public ICoverMessage getCoverMessage() {
		return coverMessage;
	}

	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	@Override
	public int getMaxSizeMessageToHide() {
		return 0;
	}

	public void transform(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm) throws IOException {
		transform(source, outImagePath, steganoAlgorithm, 64, 12);
	}

	protected BasicImageMessage getMessage(int size, String id) {
		ImageFactory factory = new ImageFactory();
		byte[] identityImageData = factory.createIdentityImageInBytes(id, size, size);
		return new BasicImageMessage(identityImageData);
	}
}
