package app.old;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.utils.ImageFactory;

public class ResizeImageFactory {


	public static void main(String[] args) {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		String input = "output\\lena_gray_test.jpg";
		Mat mat = Highgui.imread(input, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		double[] zooms = {.5, 1, 2, 4};
		
		for (int i = 0; i < zooms.length; i++) {
			Mat resizeimage = ImageFactory.resizeImage(mat, zooms[i], zooms[i]);
			String output = String.format("output\\lena_gray_test%s.jpg", mat.height());
			Highgui.imwrite(output, resizeimage);
		}
		
		System.out.println("Done!");
	}

}
