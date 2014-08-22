package core.algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import core.message.BasicImageMessage;
import core.message.MatImage;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointImagesAlgorithm1 {

	public Mat drawKeypoints(Mat source, int keyPointSize, int quantity) throws IOException{
		Enumeration<KeyPoint> keyPoints = new KeyPointEnumeration(source, keyPointSize);	    
	    List<KeyPoint> keyPointslist = Collections.list(new TopEnumeration<KeyPoint>(keyPoints, quantity));
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    matOfKeyPoints.fromList(keyPointslist);
	    Mat result = new Mat();
	    Features2d.drawKeypoints(source, matOfKeyPoints, result, Scalar.all(500), 4);
	    return result;	
		
	}
	
	public void transform(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm) throws IOException{
		transform(source, outImagePath, steganoAlgorithm, 64, 12);
	}

	
	public Mat transform(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm,
			int keyPointSize, int quantity) throws IOException{

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), quantity);
		BasicImageMessage message = getMessage(new ImageFactory(), keyPointSize >> 1);
		
		Mat result = new Mat();
		source.convertTo(result, CvType.CV_64FC1);
	    
		while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat imagRect = KeyPointOperation.getMatPoint(result, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(imagRect.clone()));
			MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(message);
			stegoObject.getMat().copyTo(imagRect);
		}	
	    
	    Highgui.imwrite(outImagePath, result);
	    
	    return result;
	}


	protected BasicImageMessage getMessage(ImageFactory factory, int size)
			throws IOException {
		byte[] identityImageData = factory.createIdentityImageInBytes("SJG", size, size);
		BasicImageMessage message = new BasicImageMessage(identityImageData);
		return message;
	}
	
	public void inverse(Mat source, String outImagePath, DWT2D_Algorithm steganoAlgorithm,
			int keyPointSize, int quantity, Mat original) throws IOException{

		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(original, keyPointSize), quantity);
		
	    ImageFactory factory = new ImageFactory();
	    source.convertTo(source, CvType.CV_64FC1);
	    
	    ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	    keyPointSize >>= 1;
		
		while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat imagRect = KeyPointOperation.getMatPoint(source, keyPoint);
	    	steganoAlgorithm.setCoverMessage(new MatImage(imagRect)); //it is no necessary to clone matrix because the algorithm will not modify it
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
			BufferedImage image = factory.createImage(keyPointSize, keyPointSize, embeddedData);
			image = ImageFactory.filter(image);
			images.add(image);
		}

	    BufferedImage image = ImageFactory.createMergeImage(images);
	    if(image != null)
	    	ImageIO.write(image, "bmp", new File(outImagePath + ".bmp"));
	}

	public void saveKeyPointsImage(Mat source, float keyPointSize, int quantity, String outImagePath) {
		Mat outImage = new Mat();
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    List<KeyPoint> keyPointList = Collections.list(new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), quantity));
	    matOfKeyPoints.fromList(keyPointList);
	    Features2d.drawKeypoints(source, matOfKeyPoints, outImage, Scalar.all(500), 1);
	    Highgui.imwrite(outImagePath + ".bmp", outImage);
	}
	
	public List<KeyPoint> getKeyPoints(Mat source, int detectorType, float size){
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    FeatureDetector blobDetector = FeatureDetector.create(detectorType);
	    blobDetector.detect(source, matOfKeyPoints);
	    List<KeyPoint> list = matOfKeyPoints.toList();
	    ArrayList<KeyPoint> result = new ArrayList<KeyPoint>(list.size());
	    for (KeyPoint keyPoint : list) {
	    	if(!isOutPoint(keyPoint, size, source.height())){
	    		keyPoint.size = size;
	    		result.add(keyPoint);
	    	}
		}
		return result;	
	}
	
	public List<KeyPoint> getKeyPoints(Mat source, int size){
		ArrayList<KeyPoint> result = new ArrayList<KeyPoint>(source.height() * source.width());
		for (int row = size; row < source.height() - size; row++) {
			for (int col = size; col < source.width() - size; col++) {
				result.add(new KeyPoint(row, col, size));
			}	
		}
		return result;	
	}

	private boolean isOutPoint(KeyPoint keyPoint, float size, int dimention) {
		return keyPoint.pt.x < size || keyPoint.pt.y < size ||
				keyPoint.pt.x + size >= dimention || keyPoint.pt.y + size >= dimention;
	}
}
