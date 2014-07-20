package app.old;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWTAlgorithm_2;
import core.algorithm.KeyPointImagesAlgorithm1;
//import core.algorithm.DWT97Algorithm;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;

public class Console_KeyPointsDoubleTest {

	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 64;
		int howManyPoints = 6;
		int visibilityfactor = 8;
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		
		DWTAlgorithm_2 steganoAlgorithm = new DWTAlgorithm_2(null, alg, visibilityfactor);
	    
		KeyPointImagesAlgorithm1 algorithm = new KeyPointImagesAlgorithm1();
		
		String file = "input\\lena_gray.jpg";
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		String output = "output\\lena_gray_KP.jpg";
		
		Mat drawKeypoints = algorithm.drawKeypoints(mat, keyPointSize, howManyPoints);
		Highgui.imwrite(output, drawKeypoints);
		
		output = "output\\lena_gray_test.jpg";
		algorithm.transform(mat, output, steganoAlgorithm, keyPointSize, howManyPoints);
		
		mat = Highgui.imread(output, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		/*
		drawKeypoints = algorithm.drawKeypoints(mat, keyPointSize, howManyPoints);
		Highgui.imwrite("output\\lena_gray_KP_2.bmp", drawKeypoints);
		*/
		algorithm.inverse(mat, "output\\test\\lena128-4-A1", steganoAlgorithm, keyPointSize);

		System.out.print("Done!");
	}

}
