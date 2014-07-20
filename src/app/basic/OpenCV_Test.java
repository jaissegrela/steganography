package app.basic;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class OpenCV_Test {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
	    // Load the native library.
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("m = " + m.dump());
	    
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			System.out.print(b);
			System.out.print(" ");
			int i = b & 0xFF;
			System.out.println(i);
		}
		System.out.print(Byte.MAX_VALUE);
		System.out.print(" ");
		System.out.println(Byte.MAX_VALUE & 0xFF);
	}

}
