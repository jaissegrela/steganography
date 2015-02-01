package test.core.algorithm;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.algorithm.DWT2D_HH_Bit_Algorithm;
import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.MatImage;
import core.utils.Arrays2d;

public class DWT2D_HH_Bit_AlgorithmTest {

	protected DWT2D_HH_Bit_Algorithm algorithm;
	protected Mat coverMessage;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the native library.				
		Loader.load(opencv_core.class);
	}

	@Before
	public void setUp() throws Exception {
		double[][] temp = new double[8][24];
		coverMessage = Arrays2d.createMat(temp);
		
		algorithm = new DWT2D_HH_Bit_Algorithm(new MatImage(coverMessage), null, 3);
		
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
		CacheMessage embeddedData = new CacheMessage(new byte[]{expected});
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertEquals(actual.length, 1);
		Assert.assertEquals(actual[0], expected);
	}
	
	@Test
	public void testGetEmbeddedData1() {
		byte expected = 1;
		CacheMessage embeddedData = new CacheMessage(new byte[]{expected});
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertEquals(actual.length, 1);
		Assert.assertEquals(actual[0], expected);
	}

}
