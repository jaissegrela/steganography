package app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_highgui;

import core.utils.KPoint;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class Keypoints {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints enum Test - CV_LOAD_IMAGE_UNCHANGED");
		
		Loader.load(opencv_core.class);
	    
//		List<KeyPoint> listOfKeyPoints = KeyPointOperation.getListOfKeyPoints(opencv_highgui.imread("input\\lena.jpg"), new SURF(100, 4, 4, true, true));
//		Enumeration<KeyPoint> e = Collections.enumeration(listOfKeyPoints);
		
		Enumeration<KeyPoint> e = new KeyPointEnumeration(opencv_highgui.imread("input\\export_04214.tif", opencv_highgui.CV_LOAD_IMAGE_UNCHANGED), 8);
	    Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(e, 7 * 24);
	    
	    ArrayList<KPoint> list = new ArrayList<KPoint>(7 * 24);
	    
	    int i = 1;
	    
	    while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = (KeyPoint) keyPoints.nextElement();
			System.out.println(String.format("%3d. (%3d; %3d) [%s]", i++, (int)keyPoint.pt().x(), (int)keyPoint.pt().y(), keyPoint.response()));
			list.add(new KPoint(keyPoint));
		}
	    
	    try
	      {
	         FileOutputStream fileOut = new FileOutputStream("output\\globo\\keypoints.kps");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(list);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in export_04214.kp");
	      }catch(IOException ex)
	      {
	          ex.printStackTrace();
	      }
	    
	    System.out.println("Done");
	}

	

}
