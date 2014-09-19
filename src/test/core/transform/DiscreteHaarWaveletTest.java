package test.core.transform;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.transform.DiscreteHaarWavelet;

public class DiscreteHaarWaveletTest {
	
	double[] init;
	double[] transformed;
	DiscreteHaarWavelet transform;

	@Before
	public void setUp() throws Exception {
		init = new double[]{100, 20, 50, 128, 0, -100, 10, 80};
		transformed = new double[]{120, 178, -100, 90, 80, -78, 100, -70};
		transform = new DiscreteHaarWavelet();
	}

	@Test
	public void testTransformLevel1() {
		transform.transform(init, init.length);
		assertArrayEquals(transformed, init, .001);
	}

	@Test
	public void testInverseLevel1() {
		transform.inverse(transformed, transformed.length);
		assertArrayEquals(init, transformed, .001);
	}
	
	@Test
	public void testTransformLevel2() {
		transform.transform(init, init.length);
		transform.transform(init, init.length >> 1);
		double[] expected = new double[]{298, -10, -58, -190, 80, -78, 100, -70};
		assertArrayEquals(expected, init, .001);
	}

	@Test
	public void testInverseLevel2() {
		double[] input = new double[]{298, -10, -58, -190, 80, -78, 100, -70};
		transform.inverse(input, input.length >> 1);
		transform.inverse(input, input.length);
		assertArrayEquals(init, input, .001);
	}

}
