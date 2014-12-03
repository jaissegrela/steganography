package core.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SURF;

import core.algorithm.ISteganographyAlgorithm;
import core.message.BasicImageMessage;
import core.message.MatImage;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointImagesAlgorithm {

	public static Mat drawKeypoints(Mat source, int keyPointSize, int quantity) throws IOException {
		KeyPointEnumeration keyPoints = new KeyPointEnumeration(source, keyPointSize);
		Enumeration<KeyPoint> enumeration = new TopEnumeration<KeyPoint>(keyPoints, quantity);
		Mat result = source.clone();
		Scalar[] colors = new Scalar[] { new Scalar(256, 0, 0, 0), new Scalar(128, 0, 0, 0), new Scalar(0, 256, 0, 0),
				new Scalar(0, 128, 0, 0), new Scalar(0, 0, 256, 0), new Scalar(0, 0, 128, 0),
				new Scalar(256, 256, 0, 0), new Scalar(0, 256, 256, 0), new Scalar(256, 0, 256, 0),
				new Scalar(256, 256, 256, 0) };
		int i = 0;
		while (enumeration.hasMoreElements()) {
			KeyPoint kp = enumeration.nextElement();
			opencv_features2d.drawKeypoints(result, kp, result, colors[i++ % colors.length],
					DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
		}
		return result;
	}

	public static Mat drawAllKeypoints(Mat source) throws IOException {
		ListIterator<KeyPoint> it = KeyPointOperation.getListOfKeyPoints(source, new SURF(1000, 4, 4, true, true)).listIterator();
		Mat result = new Mat(source);
		Scalar[] colors = new Scalar[] { new Scalar(256, 0, 0, 0), new Scalar(128, 0, 0, 0), new Scalar(0, 256, 0, 0),
				new Scalar(0, 128, 0, 0), new Scalar(0, 0, 256, 0), new Scalar(0, 0, 128, 0),
				new Scalar(256, 256, 0, 0), new Scalar(0, 256, 256, 0), new Scalar(256, 0, 256, 0),
				new Scalar(256, 256, 256, 0) };
		int i = 0;
		while (it.hasNext()) {
			KeyPoint kp = it.next();
			opencv_features2d.drawKeypoints(result, kp, result, colors[i++ % colors.length],
					DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
		}
		return result;
	}
/*
	public void algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm) throws IOException {
		algorithm(source, outImagePath, steganoAlgorithm, 64);
	}

	public void algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm, int keyPointSize)
			throws IOException {

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(new KeyPointEnumeration(source, keyPointSize), 6);

		BasicImageMessage message = getMessage(new ImageFactory(), steganoAlgorithm.getMaxSizeMessageToHide());

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			imagRect = imagRect.clone();
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect));
			ICoverMessage stegoObject = steganoAlgorithm.getStegoObject(message);
			copyTo(source, keyPoint, imagRect, stegoObject);
		}

		opencv_highgui.imwrite(outImagePath, source);
	}

	protected void copyTo(Mat source, KeyPoint keyPoint, Mat imagRect, ICoverMessage stegoObject) {
		int keyPointSize = (int) keyPoint.size();
		byte[] data = new byte[keyPointSize * keyPointSize * source.channels()];
		for (int i = 0; i < data.length; i++) {
			data[i] = stegoObject.getByte(i);
		}
		keyPoint = KeyPointOperation.getLeftUpperConner(keyPoint);
		Mat aux = source.colRange((int) keyPoint.pt().x(), (int) (keyPoint.pt().x() + keyPointSize));
		aux = aux.rowRange((int) keyPoint.pt().y(), (int) (keyPoint.pt().y() + keyPointSize));
		imagRect.put(0, 0, data);
		imagRect.copyTo(aux);
	}
*/
	protected BasicImageMessage getMessage(ImageFactory factory, int size) throws IOException {
		size = (int) Math.sqrt(size);
		byte[] identityImageData = factory.createIdentityImageInBytes("abc", size, size);
		BasicImageMessage message = new BasicImageMessage(identityImageData);
		return message;
	}

	public void des_algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm,
			float keyPointSize, int quantity) throws IOException {

		List<KeyPoint> keyPointList = Collections.list(new TopEnumeration<KeyPoint>(new KeyPointEnumeration(source,
				keyPointSize), quantity));
		ImageFactory factory = new ImageFactory();

		int point = 1;

		for (KeyPoint keyPoint : keyPointList) {
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect));
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
			BufferedImage image = factory.createImage((int) keyPointSize / 2, (int) keyPointSize / 2, embeddedData);
			ImageIO.write(image, "bmp", new File(outImagePath + (point++) + ".bmp"));
		}
	}
}
