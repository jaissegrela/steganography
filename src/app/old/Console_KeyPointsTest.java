package app.old;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import test.core.KeyPointImagesAlgorithm;

//import core.algorithm.DWT97Algorithm;

public class Console_KeyPointsTest {

	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		KeyPointImagesAlgorithm algorithm = new KeyPointImagesAlgorithm();
		
		String file = "input\\lena_gray.jpg";
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		String output = "output\\lena_gray1.jpg";
		Mat drawKeypoints = algorithm.drawKeypoints(mat, 64, 6);
		Highgui.imwrite(output, drawKeypoints);
		
		//algorithm.algorithm("input\\lena.bmp", "output\\lena.bmp", new DWT97Algorithm(1, 32, 32), 64);
		
		//algorithm.des_algorithm("output\\lena_grayA1.jpg", "output\\test\\lenaA1", new DWTAlgorithm(1), 64);
		
		System.out.print("Done!");
	}

}
