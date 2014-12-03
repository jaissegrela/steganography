package core.utils;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

public class KeyPointOperation {
	
	public static List<KeyPoint> getListOfKeyPoints(Mat source, opencv_features2d.FeatureDetector detector) {
		List<KeyPoint> result = new ArrayList<KeyPoint>();
		KeyPoint keypoints = getKeyPoints(source, detector);
		for (int i = 0; i < keypoints.capacity(); i++) {
			KeyPoint kp = keypoints.position(i);
			kp = new KeyPoint(kp.pt(), kp.size(), kp.angle(), kp.response(), kp.octave(), kp.class_id());
			result.add(kp);
		}
		return result;
	}
	
	public static KeyPoint getKeyPoints(Mat source, opencv_features2d.FeatureDetector detector) {
		Mat kps;
		if(source.type() != opencv_core.CV_8U){
			kps = new Mat();
			source.convertTo(kps, opencv_core.CV_8U);
		}else{
			kps = source;
		}
		KeyPoint result = new KeyPoint();
		detector.detect(kps, result);
		return result;
	}
	
	public static double distance(KeyPoint a, KeyPoint b){
		return Math.sqrt(Math.pow((a.pt().x() - b.pt().x()), 2) + Math.pow((a.pt().y() - b.pt().y()), 2));
	}
	
	public static boolean hasInstersection(KeyPoint a, KeyPoint b){
		return distance(a, b) < ((a.size() + b.size()) / 2);
	}
	
	public static boolean hasManhattanInstersection(KeyPoint a, KeyPoint b){
		float size = (a.size() + b.size()) / 2;
		return Math.abs(a.pt().x() - b.pt().x()) < size && Math.abs(a.pt().y() - b.pt().y()) < size;
	}
	
	public static KeyPoint getLeftUpperConner(KeyPoint keyPoint){
		float width = keyPoint.size() / 2;
		return new KeyPoint((float)keyPoint.pt().x() - width, (float)keyPoint.pt().y() - width, keyPoint.size());
	}
	
	public static Mat getMatPoint(Mat mat, KeyPoint keyPoint){
		KeyPoint leftUpperConner = getLeftUpperConner(keyPoint);
		Rect rect = new Rect((int)leftUpperConner.pt().x(), (int)leftUpperConner.pt().y(), (int)keyPoint.size(), (int)keyPoint.size());
		return new Mat(mat, rect);
	}

	/*
	public static void setCircleImage(Mat result) {
		float radio = result.rows() / 2;
		KeyPoint center = new KeyPoint(radio, radio, 0);
		for (int i = 0; i < result.cols(); i++) {
			for (int j = 0; j < result.rows(); j++) {
				KeyPoint point = new KeyPoint(i, j, 0);
				if(distance(center, point) > radio)
				{
					result.put(i, j, 0);
				}
			}
		}
	}
	
	public static double getProbability(Mat result) {
		float radio = result.height() / 2;
		KeyPoint center = new KeyPoint(radio, radio, 0);
		int outside = 0;
		int white = 0;
		for (int i = 0; i < result.height(); i++) {
			for (int j = 0; j < result.width(); j++) {
				KeyPoint point = new KeyPoint(i, j, 0);
				if(distance(center, point) > radio)
				{
					outside++;
					if(result.get(i, j)[0] == 1)
						white++;
				}
			}
		}
		return ((double)white) / outside;
	}
	
	public static double getProbability(BufferedImage image) {
		float radio = image.getHeight() / 2;
		KeyPoint center = new KeyPoint(radio, radio, 0);
		int outside = 0;
		int white = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				KeyPoint point = new KeyPoint(i, j, 0);
				if(distance(center, point) > radio)
				{
					outside++;
					int rgb = image.getRGB(i, j);
					if(rgb == -1)
						white++;
				}
			}
		}
		return ((double)white) / outside;
	}
	
	
	
	public static void putCircleImage(Mat dest, double[][] input, int row, int col) {
		int radio = input.length >> 1;
		KeyPoint center = new KeyPoint(radio, radio, 0);
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input.length; j++) {
				KeyPoint point = new KeyPoint(i, j, 0);
				if(distance(center, point) < radio)
				{
					dest.put(row + i, col + j, input[i][j]);
				}
			}
		}
	}
	
	public static Mat drawKeypoints(Mat source, int keyPointSize, int quantity) throws IOException{
		Enumeration<KeyPoint> keyPoints = new KeyPointEnumeration(source, keyPointSize);	    
	    List<KeyPoint> keyPointslist = Collections.list(new TopEnumeration<KeyPoint>(keyPoints, quantity));
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    matOfKeyPoints.fromList(keyPointslist);
	    Mat result = new Mat();
	    Features2d.drawKeypoints(source, matOfKeyPoints, result, Scalar.all(500), 4);
	    return result;	
		
	}

	*/
}
