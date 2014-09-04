package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;

public class DWT_Transform_Image {

	
	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    System.out.println("DWT97_Image_Test.java");
		
	    String file = "lena";
		String inputFile = String.format("input\\%s.jpg", file);
		String outputFile = String.format("output\\%s_DWT_Haar.jpg", file);
			
		Mat mat = Highgui.imread(inputFile);
		mat.convertTo(mat, CvType.CV_64FC1);
		
		Transform2d alg = new Transform2dBasic(new DiscreteHaarWavelet());
		
		alg.transform(mat, 1);
		
		Highgui.imwrite(outputFile, mat);
		
		System.out.println("DWT97_Image_Test Done!!");
	}
}
