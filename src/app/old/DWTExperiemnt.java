package app.old;

import java.io.IOException;
import java.util.Enumeration;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;
import core.utils.KeyPointOperation;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class DWTExperiemnt {


	public static void main(String[] args) throws IOException {
		System.out.println("Keypoints algorithm test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 8;
		int howManyPoints = 1;
		
		//Reading image
		String file = String.format("input\\lena.jpg");
		Mat mat = Highgui.imread(file);
		if(mat.size().width == 0)
		{
			System.out.println(String.format("Cannot load the image %s", file));
			return;
		}
		
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(mat, keyPointSize), howManyPoints);
		
		mat.convertTo(mat, CvType.CV_64FC1);
		
		Transform2d transform = new Transform2dBasic(new DiscreteHaarWavelet());
	    
		while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat rect = KeyPointOperation.getMatPoint(mat, keyPoint);
	    	Arrays2d.print(rect);
	    	System.out.println();
	    	
	    	transform.transform(rect, 2);
	    	Arrays2d.print(rect);
	    	System.out.println();
	    	
	    	transform.inverse(rect, 2);
	    	Arrays2d.print(rect);
	    	System.out.println();
		}
		
		System.out.println("Done!");
	}

}
