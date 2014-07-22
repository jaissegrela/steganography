package core.algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import core.message.BasicImageMessage;
import core.message.ICoverMessage;
import core.message.MatImage;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.TopEnumeration;

public class KeyPointImagesAlgorithm {

	public Mat drawKeypoints(Mat source, int keyPointSize, int quantity) throws IOException{
		KeyPointEnumeration keyPoints = new KeyPointEnumeration(source, keyPointSize);	    
	    List<KeyPoint> keyPointslist = Collections.list(new TopEnumeration<KeyPoint>(keyPoints, quantity));
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    matOfKeyPoints.fromList(keyPointslist);
	    Mat result = new Mat();
	    Features2d.drawKeypoints(source, matOfKeyPoints, result, Scalar.all(500), 4);
	    return result;	
		
	}
	
	public void algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm) throws IOException{
		algorithm(source, outImagePath, steganoAlgorithm, 64);
	}

	
	public void algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm,
			int keyPointSize) throws IOException{

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), 6);

		BasicImageMessage message = getMessage(
				new ImageFactory(), 
				steganoAlgorithm.getMaxSizeMessageToHide());
		
	    while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect));
			ICoverMessage stegoObject = steganoAlgorithm.getStegoObject(message);
			copyTo(source, keyPoint, imagRect, stegoObject);
		}
	    
	    Highgui.imwrite(outImagePath, source);
	}

	protected void copyTo(Mat source, KeyPoint keyPoint,
			Mat imagRect, ICoverMessage stegoObject) {
		int keyPointSize = (int)keyPoint.size;
		byte[] data = new byte[keyPointSize * keyPointSize * source.channels()];
		for (int i = 0; i < data.length; i++) {
			data[i] = stegoObject.getByte(i);
		}
		keyPoint = KeyPointOperation.getLeftUpperConner(keyPoint);
		Mat aux = source.colRange((int)keyPoint.pt.x, (int)(keyPoint.pt.x + keyPointSize));
		aux = aux.rowRange((int)keyPoint.pt.y, (int)(keyPoint.pt.y + keyPointSize));
		imagRect.put(0, 0, data);
		imagRect.copyTo(aux);
	}

	protected BasicImageMessage getMessage(ImageFactory factory, int size)
			throws IOException {
		size = (int) Math.sqrt(size);
		byte[] identityImageData = factory.createIdentityImageInBytes("abc", size, size);
		BasicImageMessage message = new BasicImageMessage(identityImageData);
		return message;
	}
	
	public void des_algorithm(Mat source, String outImagePath, ISteganographyAlgorithm steganoAlgorithm,
			float keyPointSize, int quantity) throws IOException{

		List<KeyPoint> keyPointList = Collections.list(new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), quantity));
	    ImageFactory factory = new ImageFactory();
	    
	    int point = 1;
		
	    for (KeyPoint keyPoint : keyPointList) {
	    	
			Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect));
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
			BufferedImage image = factory.createImage((int)keyPointSize / 2, (int)keyPointSize / 2, embeddedData);
			ImageIO.write(image, "bmp", new File(outImagePath + (point++) + ".bmp"));
		}
	}

	public void saveKeyPointsImage(Mat source, float keyPointSize, int quantity, String outImagePath) {
		Mat outImage = new Mat();
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    List<KeyPoint> keyPointList = Collections.list(new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), quantity));
	    matOfKeyPoints.fromList(keyPointList);
	    Features2d.drawKeypoints(source, matOfKeyPoints, outImage, Scalar.all(500), 4);
	    Highgui.imwrite(outImagePath + ".bmp", outImage);
	}
}
