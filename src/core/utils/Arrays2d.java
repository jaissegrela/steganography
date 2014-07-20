package core.utils;

import org.opencv.core.Mat;

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

}
