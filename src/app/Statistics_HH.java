package app;

import java.io.IOException;
import java.util.Enumeration;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class Statistics_HH {

	private static Mat source;

	public static void main(String[] args) throws IOException {
		
		System.out.println("Statistics HH test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 8;
		int howManyPoints = 1;
		int levels = 3;
		
		String file = String.format("input\\lena.jpg");
	    
		source = Highgui.imread(file);
		
		Transform2d alg = new Transform2dBasic(new DiscreteHaarWavelet());
	    
		Size size = source.size();
		
		printMat(keyPointSize, howManyPoints, levels, source, alg);
		
		file = String.format("output\\lena_test_1.jpg");
	    
		Mat temp = Highgui.imread(file);
		
		System.out.println();
		printMat(keyPointSize, howManyPoints, levels, temp, alg);
		
		double[] zooms = {.5, .75, 2};
		
		for (int j = 0; j < zooms.length; j++) {
			Mat result = ImageFactory.zoom(temp, zooms[j], zooms[j]);
			result = ImageFactory.resizeImage(result, size);
			System.out.println(zooms[j]);
			printMat(keyPointSize, howManyPoints, levels, result, alg);
		}
		
		System.out.print("Done!");
	}

	protected static void printMat(int keyPointSize, int howManyPoints,
			int levels, Mat mat, Transform2d alg) {
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(source, keyPointSize), howManyPoints);
		int i = 0;
		while (keyPoints.hasMoreElements()) {
			//System.out.println(i++);
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat rect = KeyPointOperation.getMatPoint(mat, keyPoint);
	    	rect.convertTo(rect, CvType.CV_64FC1);
	    	//alg.transform(rect, levels);
	    	Arrays2d.print(rect);
		}
	}

}
