package app.old;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.highgui.Highgui;

import core.algorithm.Daub;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;

public class Daub_Test {
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

	    String file = "lena_gray.jpg";
		Mat mat = Highgui.imread("input\\" + file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		System.out.println(String.format("%s %s", CvType.typeToString(mat.type()), mat.channels()));
		CvType.typeToString(mat.type());
		
		byte[] ipixels = new byte[(int)mat.size().area() * mat.channels()];
		mat.get(0, 0, ipixels);
		Mat dest = new Mat(mat.size(), CvType.CV_64FC1);
		System.out.println(String.format("%s %s", CvType.typeToString(dest.type()), dest.channels()));
		mat.convertTo(dest, CvType.CV_64FC1);
		System.out.println(String.format("%s %s", CvType.typeToString(dest.type()), dest.channels()));
		double[] dpixels = new double[(int)dest.size().area() * dest.channels()];
		dest.get(0, 0, dpixels);
		Daub alg = new Daub();
		alg.daubTrans(dpixels);
//		alg.fwt97(dpixels, dpixels.length / 2);
//		alg.fwt97(dpixels, dpixels.length / 4);
		dest.put(0, 0, dpixels);
		Highgui.imwrite("output\\lena_grayDaub" + file, dest);
		
		System.out.println("Daub_Test - Done!");
		
	}
	


	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		FastDiscreteBiorthogonal_CDF_9_7 alg = new FastDiscreteBiorthogonal_CDF_9_7();
		
		  double[] x = new double[32];
		  
		  // Makes a fancy cubic signal
		  for (int i=0; i < x.length; i++) 
			  x[i] = 5 + i + 0.4 * i * i -0.02 * i * i * i;
			  //x[i] = i;
		  
		  // Prints original sigal x
		  System.out.println("Original signal:");
		  for (int i = 0; i < x.length; i++) 
			  System.out.print(String.format("%s:%f, ", i, x[i]));
		  System.out.println();

		  // Do the forward 9/7 transform
		  alg.transform(x, x.length);
		  
		  // Prints the wavelet coefficients
		  System.out.println("Wavelets coefficients:");
		  for (int i = 0; i < x.length; i++) 
			  System.out.print(String.format("%s:%f, ", i, x[i]));
		  System.out.println("\n");

		  // Do the inverse 9/7 transform
		  alg.inverse(x, x.length); 

		  // Prints the reconstructed signal 
		  System.out.println("Reconstructed signal:");
		  for (int i = 0; i < x.length; i++) 
			  System.out.print(String.format("%f, ", x[i]));
	}

}
