package core.utils;

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

}
