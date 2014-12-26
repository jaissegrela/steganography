package app;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class Keypoints {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Statistics HH1 test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
//	    List<KeyPoint> listOfKeyPoints = getKeyPoints(Highgui.imread("input\\lena.jpg"));
//	    Enumeration<KeyPoint> e = Collections.enumeration(listOfKeyPoints);
	    
	    Enumeration<KeyPoint> e = new KeyPointEnumeration(Highgui.imread("input\\lena.jpg"), 8);
	    
	    Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(e, 5 * 24);
	    
	    while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = (KeyPoint) keyPoints.nextElement();
			System.out.println(String.format("(%s; %s) [%s]", (int)keyPoint.pt.x, (int)keyPoint.pt.y, keyPoint.response));
		}
	}
	
	public static List<KeyPoint> getKeyPoints(Mat source){
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    FeatureDetector blobDetector = FeatureDetector.create(FeatureDetector.SURF);
	    blobDetector.detect(source, matOfKeyPoints);
	    return matOfKeyPoints.toList();	
	}

	

}
