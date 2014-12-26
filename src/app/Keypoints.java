package app;

import java.io.IOException;
import java.util.Enumeration;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_highgui;

import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class Keypoints {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints enum Test");
		
		Loader.load(opencv_core.class);
	    
//		List<KeyPoint> listOfKeyPoints = KeyPointOperation.getListOfKeyPoints(opencv_highgui.imread("input\\lena.jpg"), new SURF(100, 4, 4, true, true));
//		Enumeration<KeyPoint> e = Collections.enumeration(listOfKeyPoints);
		
		Enumeration<KeyPoint> e = new KeyPointEnumeration(opencv_highgui.imread("input\\lena.jpg"), 8);
	    Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(e, 5 * 24);
	    
	    while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = (KeyPoint) keyPoints.nextElement();
			System.out.println(String.format("(%s; %s) [%s]", (int)keyPoint.pt().x(), (int)keyPoint.pt().y(), keyPoint.response()));
		}
	}

	

}
