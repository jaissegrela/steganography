package test.core.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import core.utils.Arrays2d;

public class Arrays2dTest {

	protected Mat mat;
	protected double[][][] data;

	@Before
	public void setUp() throws Exception {
		// Load the native library.				
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			    
		data = new double[][][] {
				{ { 0, 0, 0 }, { 0, 1, 1 }, { 0, 2, 2 } },
				{ { 1, 0, 0 }, { 1, 1, 1 }, { 1, 2, 2 } },
				{ { 2, 0, 0 }, { 2, 1, 0 }, { 2, 2, 2 } } };
		mat = createMat(data);

	}

	@Test
	public void testGetPNSRforEqualMat() {
		Mat other = createMat(data);
		double actual = Arrays2d.getPNSR(mat, other);
		Assert.assertEquals(0, actual, .0001);
	}

	@Test
	public void testGetPNSRforZeroMat() {
		double[][][] zeros = new double[][][] {
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } };
		Mat other = createMat(zeros);
		double actual = Arrays2d.getPNSR(mat, other);
		int expected = 46;
		Assert.assertEquals(expected, actual, .01);
	}

	protected Mat createMat(double[][][] data) {
		Mat result = new Mat(data.length, data[0].length, CvType.CV_64FC3);
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				result.put(row, col, data[row][col]);
			}
		}
		return result;
	}
}
