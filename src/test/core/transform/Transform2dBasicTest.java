package test.core.transform;

import static org.junit.Assert.assertArrayEquals;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;

public class Transform2dBasicTest {
	
	Mat init;
	Mat transformed;
	Transform2dBasic transform;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the native library.				
		Loader.load(opencv_core.class);
	}

	@Before
	public void setUp() throws Exception {
		
		double[][] values = new double[][]{
				{100, 20, 50, 128, 0, -100, 10, 80},
				{  1,  2,  3,   4, 5,    6,  7,  8},
				{ -1, -2, -3, -4, -5,  -6, -7, -8},
				{ -3, -2, -1,   0, 1,    2, 3,   4},
				{ -3, -2, -1,   0, 1,    2, 3,   4},
				{ 10, 20, 30, 40, 50,   60, 70, 80},
				{-10,-20,-30,-40,-50, -60, -70,-80},
				{  0,  0,  0,  0,  0,   0,   0,  0}};
		init = Arrays2d.createMat(values);
		
		/*
		double[][] t = new double[][]{
				{120, 178, -100, 90, 80, -78, 100, -70},
				{  3,   7,   11, 15, -1,  -1,  -1, -1},
				{ -3,  -7,  -11,-15,  1,   1,   1,  1},
				{ -5,  -1,    3,  7, -1,  -1,  -1, -1},
				{ -5,  -1,    3,  7, -1,  -1,  -1, -1},
				{ 30,  70,  110,150,-10, -10, -10,-10},
				{-30, -70, -110,-150,10,  10,  10, 10},
				{  0,   0,    0,   0, 0,   0,   0,  0},
				};
		*/
		double[][] t = new double[][]{
				{123, 185, -89, 105, 79, -79, 99, -71},
				{ -8,  -8,  -8,  -8,  0,   0,  0,   0},
				{ 25,  69, 113, 157,-11, -11, -11,-11},
				{-30, -70,-110,-150, 10,  10,  10, 10},
				{117, 171,-111,  75, 81, -77, 101,-69},
				{  2,  -6, -14, -22,  2,   2,  2,   2},
				{-35, -71,-107,-143,  9,   9,  9,   9},
				{-30, -70,-110,-150, 10,  10,  10, 10}};
		
		transformed = Arrays2d.createMat(t);
		transform = new Transform2dBasic(new DiscreteHaarWavelet());
	}


	@Test
	public void testTransformLevel1() {
		transform.transform(init, 1);
		double[][] actual = Arrays2d.getSource(init);
		double[][] expected = Arrays2d.getSource(transformed);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i], .001);	
		}
	}

	@Test
	public void testInverseLevel1() {
		transform.inverse(transformed, 1);
		double[][] actual = Arrays2d.getSource(transformed);
		double[][] expected = Arrays2d.getSource(init);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i], .001);	
		}
	}
	
	@Test
	public void testTransformLevel2() {
		transform.transform(init, 2);
		double[][] actual = Arrays2d.getSource(init);
		/*
		double[][] expected = new double[][]{
				{ 308,  16, -62, -194, 79, -79, 99, -71},
				{ -16, -16,   0,   0,  0,   0,  0,   0},
				{  94, 270, -44, -44,-11, -11, -11,-11},
				{-100,-260,  40, 40, 10,  10,  10, 10},
				{117, 171,-111,  75, 81, -77, 101,-69},
				{  2,  -6, -14, -22,  2,   2,  2,   2},
				{-35, -71,-107,-143,  9,   9,  9,   9},
				{-30, -70,-110,-150, 10,  10,  10, 10}};
		*/
		double[][] expected = new double[][]{
				{ 292,   0, -62,-194, 79, -79, 99, -71},
				{  -6,  10,  -4,  -4,  0,   0,  0,   0},
				{ 324,  32, -62,-194,-11, -11, -11,-11},
				{ 194, 530, -84, -84, 10,  10,  10, 10},
				{117, 171,-111,  75, 81, -77, 101,-69},
				{  2,  -6, -14, -22,  2,   2,  2,   2},
				{-35, -71,-107,-143,  9,   9,  9,   9},
				{-30, -70,-110,-150, 10,  10,  10, 10}};
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i], .001);	
		}
	}

	@Test
	public void testInverseLevel2() {
		double[][] temp = new double[][]{
				{ 292,   0, -62,-194, 79, -79, 99, -71},
				{  -6,  10,  -4,  -4,  0,   0,  0,   0},
				{ 324,  32, -62,-194,-11, -11, -11,-11},
				{ 194, 530, -84, -84, 10,  10,  10, 10},
				{117, 171,-111,  75, 81, -77, 101,-69},
				{  2,  -6, -14, -22,  2,   2,  2,   2},
				{-35, -71,-107,-143,  9,   9,  9,   9},
				{-30, -70,-110,-150, 10,  10,  10, 10}};
		Mat transformed = Arrays2d.createMat(temp);
		
		transform.inverse(transformed, 2);
		double[][] actual = Arrays2d.getSource(transformed);
		double[][] expected = Arrays2d.getSource(init);
		
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i], .001);	
		}
	}

}
