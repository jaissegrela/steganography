package app.old;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.KeyPointImagesAlgorithm;
import core.utils.ImageFactory;

public class ResizeImageTest {


	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	    KeyPointImagesAlgorithm algorithm = new KeyPointImagesAlgorithm();
		
		String file = "input\\lena_gray.jpg";
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		String output = "output\\lena_gray1DP.jpg";
		Mat drawKeypoints = algorithm.drawKeypoints(mat, 64, 6);
		Highgui.imwrite(output, drawKeypoints);
		
		mat = ImageFactory.zoom(mat, .5, .5);
		
		mat = ImageFactory.zoom(mat, 2, 2);
		
		output = "output\\lena_gray2DP.jpg";
		drawKeypoints = algorithm.drawKeypoints(mat, 64, 6);
		Highgui.imwrite(output, drawKeypoints);
		
		System.out.print("Done!");
	}

}
