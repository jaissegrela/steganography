package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_Algorithm;
import core.algorithm.KeyPointImagesAlgorithm1;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
//import core.algorithm.DWT97Algorithm;

public class Console_KeyPointsDoubleTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints algorithm test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 128;
		int howManyPoints = 2;
		int visibilityfactor = 7;
		
		String file = String.format("input\\lena.bmp");
	    
		Mat original = Highgui.imread(file);
		
		System.out.println("Channels: " + original.channels());
		System.out.println("Type: " + CvType.typeToString(original.type()));
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		DWT2D_Algorithm steganoAlgorithm = new DWT2D_Algorithm(null, alg, visibilityfactor);
	    
		KeyPointImagesAlgorithm1 algorithm = new KeyPointImagesAlgorithm1();
		
		String output = String.format("output\\lena%s.bmp", visibilityfactor);
		Mat mat = algorithm.transform(original, output, steganoAlgorithm, keyPointSize, howManyPoints);
		mat = Highgui.imread(output);
		
		String message = String.format("output\\lena_message%s", visibilityfactor);
		algorithm.inverse(mat, message, steganoAlgorithm, keyPointSize, howManyPoints, original);
		
		System.out.print("Done!");
	}

}
