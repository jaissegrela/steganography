package test.core.algorithm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import core.algorithm.DWT2D_HH_Bit_Algorithm;
import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.MatImage;
import core.utils.Arrays2d;

public class DWT2D_HH_Bit_AlgorithmTest {

	protected DWT2D_HH_Bit_Algorithm algorithm;
	protected Mat message;
	protected Mat original;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the native library.				
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	@Before
	public void setUp() throws Exception {
		double[][] temp = new double[8][24];
		message = Arrays2d.createMat(temp);
		original = Arrays2d.createMat(temp);
		
		algorithm = new DWT2D_HH_Bit_Algorithm(new MatImage(message), new MatImage(original), 192);
		
	}

	@Test
	public void testGetStegoObject0() {
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{(byte)0}));
		Arrays2d.print(actual.getMat());
	}
	
	@Test
	public void testGetStegoObject1() {
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{(byte)1}));
		Arrays2d.print(actual.getMat());
	}

	@Test
	public void testGetEmbeddedData0() {
		byte expected = 0;
		ICoverMessage stego = algorithm.getStegoObject(new CacheMessage(new byte[]{expected}));
		algorithm.setCoverMessage(stego);
		byte[] actual = algorithm.getEmbeddedData();
		Assert.assertEquals(actual.length, 1);
		Assert.assertEquals(actual[0], expected);
	}
	
	@Test
	public void testGetEmbeddedData1() {
		byte expected = 1;
		ICoverMessage stego = algorithm.getStegoObject(new CacheMessage(new byte[]{expected}));
		algorithm.setCoverMessage(stego);
		byte[] actual = algorithm.getEmbeddedData();
		Assert.assertEquals(actual.length, 1);
		Assert.assertEquals(actual[0], expected);
	}

}
