package test.core.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.algorithm.DWT2D_HH_Bit_Algorithm;
import core.algorithm.KeyPointBitRawAlgorithm;
import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.MatImage;
import core.utils.Arrays2d;

public class KeyPointBitRawAlgorithmTest {

	protected KeyPointBitRawAlgorithm algorithm;
	protected Mat coverMessage;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Load the native library.				
		Loader.load(opencv_core.class);
	}

	@Before
	public void setUp() throws Exception {
		double[][] temp = new double[8][24];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				temp[i][j] = 4;
			}
		}
		coverMessage = Arrays2d.createMat(temp);
		
		algorithm = new KeyPointBitRawAlgorithm(new MatImage(coverMessage), null,
				new DWT2D_HH_Bit_Algorithm(2), 1);
		
		List<KeyPoint> keyPoints = new ArrayList<opencv_features2d.KeyPoint>();
		keyPoints.add(new KeyPoint(1, 1, 2));
		keyPoints.add(new KeyPoint(3, 1, 2));
		keyPoints.add(new KeyPoint(5, 1, 2));
		keyPoints.add(new KeyPoint(7, 1, 2));
		
		keyPoints.add(new KeyPoint(1, 3, 2));
		keyPoints.add(new KeyPoint(3, 3, 2));
		keyPoints.add(new KeyPoint(5, 3, 2));
		keyPoints.add(new KeyPoint(7, 3, 2));
		
		keyPoints.add(new KeyPoint(1, 5, 2));
		keyPoints.add(new KeyPoint(3, 5, 2));
		keyPoints.add(new KeyPoint(5, 5, 2));
		keyPoints.add(new KeyPoint(7, 5, 2));
		
		keyPoints.add(new KeyPoint(1, 7, 2));
		keyPoints.add(new KeyPoint(3, 7, 2));
		keyPoints.add(new KeyPoint(5, 7, 2));
		keyPoints.add(new KeyPoint(7, 7, 2));
		KeyPointEnumeration_Dummy enumeration = new KeyPointEnumeration_Dummy(keyPoints);
		algorithm.setEnumeration(enumeration);
		
	}

//	@Test
	public void testGetStegoObject0() {
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{(byte)0}));
		Arrays2d.print(actual.getMat());
	}
	
//	@Test
	public void testGetStegoObject1() {
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{(byte)1}));
		Arrays2d.print(actual.getMat());
	}
	
//	@Test
	public void testGetStegoObject2() {
		System.out.println("5, -3");
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{5, -3}));
		Arrays2d.print(actual.getMat());
	}
	
//	@Test
	public void testGetStegoObject3() {
		System.out.println("5");
		algorithm.setQuantity(2);
		ICoverMessage actual = algorithm.getStegoObject(new CacheMessage(new byte[]{5}));
		Arrays2d.print(actual.getMat());
	}

	@Test
	public void testGetEmbeddedData0() {
		byte[] expected = new byte[]{0};
		CacheMessage embeddedData = new CacheMessage(expected);
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertArrayEquals(actual, expected);
	}
	
	@Test
	public void testGetEmbeddedData1() {
		byte[] expected = new byte[]{1};
		CacheMessage embeddedData = new CacheMessage(expected);
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertArrayEquals(actual, expected);
	}
	
	@Test
	public void testGetEmbeddedData2() {
		byte[] expected = new byte[]{5, -3};
		CacheMessage embeddedData = new CacheMessage(expected);
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertArrayEquals(actual, expected);
	}
	
	@Test
	public void testGetEmbeddedData3() {
		byte[] expected = new byte[]{5};
		CacheMessage embeddedData = new CacheMessage(expected);
		
		algorithm.setQuantity(2);
		ICoverMessage stego = algorithm.getStegoObject(embeddedData);
		algorithm.setStegoMessage(stego);
		byte[] actual = algorithm.getEmbeddedData(embeddedData.size());
		Assert.assertArrayEquals(actual, expected);
	}

}
