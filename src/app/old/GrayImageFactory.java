package app.old;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class GrayImageFactory {


	public static void main(String[] args) {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		String input = "input\\lena.jpg";
		Mat mat = Highgui.imread(input, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		String output = "input\\lena_gray.bmp";
		Highgui.imwrite(output, mat);
	}

}
