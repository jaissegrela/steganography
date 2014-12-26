package core.utils;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatExpr;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.opencv.core.CvType;

import static org.bytedeco.javacpp.opencv_core.*;

public class Arrays2d {

	public static <T> void transpose(double[][] input, int length) {
		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				double temp = input[i][j];
				input[i][j] = input[j][i];
				input[j][i] = temp;
			}
		}
	}
	
	/*
	public static <T> void transpose(Mat input) {
		for (int i = 0; i < input.rows(); i++) {
			for (int j = i + 1; j < input.cols(); j++) {
				double[] temp_i_j = input.get(i, j);
				double[] temp_j_i = input.get(j, i);
				input.put(j, i, temp_i_j);
				input.put(i, j, temp_j_i);
			}
		}
	}
	*/
	public static void transpose(double[][] input) {
		Arrays2d.transpose(input, input.length);
	}
	
	public static double[][] duplicate(double[][] input) {
		double[][] result = new double[input.length][input[0].length];
		for (int i = 0; i < result.length; i++) {
			System.arraycopy(input[i], 0, result[i], 0, input[0].length);
		}
		return result;
	}
	
	protected static double[][] getError(double[][] left, double[][] right) {
		double[][] error = new double[left.length][left[0].length];
		for (int i = 0; i < left.length; i++) {
			for (int j = 0; j < left[0].length; j++) {
				error[i][j] -= right[i][j] - left[i][j];
			}
		}
		return error;
	}
	
	public static double[][] getSource(Mat mat) {
		double[][] result = new double[mat.rows()][mat.cols()];
		//Arrays2d.printBasicInfo(mat);
		DoubleBuffer buffer = mat.getDoubleBuffer();
		for (int i = 0; i < result.length; i++) {
			buffer.get(result[i]);
		}
		return result;
	}
	
	public static void putSource(Mat dest, double[][] input) {
		DoubleBuffer buffer = dest.getDoubleBuffer();
		for (int i = 0; i < input.length; i++) {
			buffer.put(input[i]);
		}
	}
	
	public static double getPNSR(Mat a, Mat b) {
		Mat diff = new Mat(a.size(), a.type());
		opencv_core.absdiff(a, b, diff);	// |I1 - I2|
	    
		diff.convertTo(diff, opencv_core.CV_32F);  // cannot make a square on 8 bits
	    MatExpr mul = diff.mul(diff);           	  // |I1 - I2|^2
	    diff.put(mul);
	    
	    Scalar s = opencv_core.sumElems(diff);        // sum elements per channel

	    double result = 0;
	    for (int i = 0; i < s.sizeof(); i++) {
	    	result += s.get();	// sum channels
		}
	    
	    if( result <= 1e-10) // for small values return zero
	    	result = 0;
	    else
	    {
	    	result /= (double)(a.channels() * a.total());
	    	result = 10.0 * Math.log10((255 * 255) / result);
	    }
	    
	    return result;
	}
	
	public static void print(double[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			System.out.println(Arrays.toString(mat[i]));
		}
	}
	
	public static void print(Mat mat) {
		DoubleBuffer in = mat.getDoubleBuffer();
		double[] pixel = new double[mat.channels()];
		while(in.hasRemaining()) {
			for(int col = mat.cols(); col > 0; col--) {
				in.get(pixel);
				System.out.print(Arrays.toString(pixel));
			}
			System.out.println();
		}
		System.out.println("--");
	}
	
	public static void printBasicInfo(Mat mat) {
		System.out.println("Channels: " + mat.channels());
		System.out.println("Type: " + mat.type() + " " + getImgType(mat.type()));
		//System.out.println("Type: " + mat.type() + " " + CvType.typeToString(mat.type()));
		System.out.println("Depth: " + mat.depth());
		System.out.println("Size: " + mat.size().height() + ", " + mat.size().width());
	}
	
	public static String getImgType(int imgTypeInt)
	{
	    int numImgTypes = 35; // 7 base types, with five channel options each (none or C1, ..., C4)

	    int enum_ints[] =       {CV_8U,  CV_8UC1,  CV_8UC2,  CV_8UC3,  CV_8UC4,
	                             CV_8S,  CV_8SC1,  CV_8SC2,  CV_8SC3,  CV_8SC4,
	                             CV_16U, CV_16UC1, CV_16UC2, CV_16UC3, CV_16UC4,
	                             CV_16S, CV_16SC1, CV_16SC2, CV_16SC3, CV_16SC4,
	                             CV_32S, CV_32SC1, CV_32SC2, CV_32SC3, CV_32SC4,
	                             CV_32F, CV_32FC1, CV_32FC2, CV_32FC3, CV_32FC4,
	                             CV_64F, CV_64FC1, CV_64FC2, CV_64FC3, CV_64FC4};

	    String enum_strings[] = {"CV_8U",  "CV_8UC1",  "CV_8UC2",  "CV_8UC3",  "CV_8UC4",
	                             "CV_8S",  "CV_8SC1",  "CV_8SC2",  "CV_8SC3",  "CV_8SC4",
	                             "CV_16U", "CV_16UC1", "CV_16UC2", "CV_16UC3", "CV_16UC4",
	                             "CV_16S", "CV_16SC1", "CV_16SC2", "CV_16SC3", "CV_16SC4",
	                             "CV_32S", "CV_32SC1", "CV_32SC2", "CV_32SC3", "CV_32SC4",
	                             "CV_32F",  "CV_32FC1", "CV_32FC2", "CV_32FC3", "CV_32FC4",
	                             "CV_64F", "CV_64FC1", "CV_64FC2", "CV_64FC3", "CV_64FC4"};

	    for(int i=0; i<numImgTypes; i++)
	    {
	        if(imgTypeInt == enum_ints[i]) return enum_strings[i];
	    }
	    return "unknown image type";
	}
	
	public static Mat createMat(double[][] data) {
		Mat result;
		if(data.length == data[0].length)
			result = new Mat(data.length, data[0].length, opencv_core.CV_64FC1);
		else
			result = new Mat(data.length, data[0].length / 3, opencv_core.CV_64FC3);
		putSource(result, data);
		return result;
	}
	
	public static Mat createMat(double[][][] data) {
		Mat result = new Mat(data.length, data[0].length, CvType.CV_64FC3);
		DoubleBuffer buffer = result.getDoubleBuffer();
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				buffer.put(data[row][col]);
			}
		}
		return result;
	}

}
