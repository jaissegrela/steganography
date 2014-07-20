package app.old;

import java.io.IOException;

import kanzi.transform.DCT8;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWTAlgorithm_2;
import core.algorithm.KeyPointImagesAlgorithm1;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.ImageFactory;

public class Console_KeyPoints_Resize_Extract_Test {

	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 64;
		int visibilityfactor = 8;
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		DWTAlgorithm_2 steganoAlgorithm = new DWTAlgorithm_2(null, alg, visibilityfactor);
	    
		KeyPointImagesAlgorithm1 algorithm = new KeyPointImagesAlgorithm1();
		
		String file = "output\\lena_gray_test256.jpg";

		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		mat = ImageFactory.resizeImage(mat, 2, 2);
		algorithm.inverse(mat, "output\\test\\lena_256_", steganoAlgorithm, keyPointSize);

		System.out.print("Resize Extract Done!");
	}

}
