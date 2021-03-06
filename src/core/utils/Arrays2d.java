package core.utils;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

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
		double[][] result = new double[mat.height()][mat.width()];
		for (int i = 0; i < result.length; i++) {
			mat.get(i, 0, result[i]);
		}
		return result;
	}
	
	public static void putSource(Mat dest, double[][] input, int row, int col) {
		for (int i = 0; i < input.length; i++) {
			dest.put(i + row, col, input[i]);
		}
	}
	
	public static double getPNSR(Mat a, Mat b) {
		Mat diff = new Mat(a.size(), a.type());
		Core.absdiff(a, b, diff);	// |I1 - I2|
	    
		diff.convertTo(diff, CvType.CV_32F);  // cannot make a square on 8 bits
	    diff = diff.mul(diff);           	  // |I1 - I2|^2

	    Scalar s = Core.sumElems(diff);        // sum elements per channel

	    double result = 0;
	    for (int i = 0; i < s.val.length; i++) {
	    	result += s.val[i];	// sum channels
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
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.rows(); j++) {
				double[] ds = mat.get(i, j);/*
				for (int k = 0; k < ds.length; k++) {
					BigDecimal bd = new BigDecimal(ds[k]);
				    bd = bd.setScale(4, RoundingMode.HALF_UP);
					ds[k] = bd.doubleValue();
				}*/
				System.out.print(Arrays.toString(ds));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public static void printBasicInfo(Mat mat) {
		System.out.println("Channels: " + mat.channels());
		System.out.println("Type: " + CvType.typeToString(mat.type()));
		System.out.println("Size: " + mat.size());
	}
	
	public static Mat createMat(double[][] data) {
		Mat result;
		if(data.length == data[0].length)
			result = new Mat(data.length, data[0].length, CvType.CV_64FC1);
		else
			result = new Mat(data.length, data[0].length / 3, CvType.CV_64FC3);
		for (int row = 0; row < data.length; row++) {
			result.put(row, 0, data[row]);
		}
		return result;
	}

}
