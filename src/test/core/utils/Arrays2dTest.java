package test.core.utils;

import junit.framework.Assert;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Before;
import org.junit.Test;

import core.utils.Arrays2d;

public class Arrays2dTest {

	protected Mat mat;
	protected double[][][] data;

	@Before
	public void setUp() throws Exception {
		// Load the native library.				
		Loader.load(opencv_core.class);
			    
		data = new double[][][] {
				{ { 0, 0, 0 }, { 0, 1, 1 }, { 0, 2, 2 } },
				{ { 1, 0, 0 }, { 1, 1, 1 }, { 1, 2, 2 } },
				{ { 2, 0, 0 }, { 2, 1, 0 }, { 2, 2, 2 } } };
		mat = Arrays2d.createMat(data);

	}

	@Test
	public void testGetPNSRforEqualMat() {
		Mat other = Arrays2d.createMat(data);
		double actual = Arrays2d.getPNSR(mat, other);
		Assert.assertEquals(0, actual, .0001);
	}

	@Test
	public void testGetPNSRforZeroMat() {
		double[][][] zeros = new double[][][] {
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
				{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } } };
		Mat other = Arrays2d.createMat(zeros);
		double actual = Arrays2d.getPNSR(mat, other);
		int expected = 46;
		Assert.assertEquals(expected, actual, .01);
	}
}
