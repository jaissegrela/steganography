package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWTAlgorithm_2;
import core.algorithm.KeyPointImagesAlgorithm1;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
//import core.algorithm.DWT97Algorithm;

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
		
		String file = "output\\Split_4.jpg";
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		System.out.println("Channels: " + mat.channels());
		System.out.println("Type: " + CvType.typeToString(mat.type()));
		
		String output = "output\\Split_KP.jpg";
		
		Mat drawKeypoints = algorithm.drawKeypoints(mat, keyPointSize, howManyPoints);
		Highgui.imwrite(output, drawKeypoints);
		
		output = "output\\Split_test.jpg";
		algorithm.transform(mat, output, steganoAlgorithm, keyPointSize, howManyPoints);
		
		mat = Highgui.imread(output, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		/*
		drawKeypoints = algorithm.drawKeypoints(mat, keyPointSize, howManyPoints);
		Highgui.imwrite("output\\lena_gray_KP_2.bmp", drawKeypoints);
		*/
		algorithm.inverse(mat, "output\\test\\lena_split_4.jpg", steganoAlgorithm, keyPointSize);

		System.out.print("Done!");
	}

}
