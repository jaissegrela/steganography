package test.core.algorithm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.algorithm.DWT2D_Algorithm;
import core.message.CacheMessage;
import core.transform.*;
import core.utils.ArrayOperations;
import core.utils.ByteInfo;
import core.utils.Converter;
import core.utils.enumerations.BitEnumeration;

public class DWT97forRealValuesAlgorithmTest {
	
	DWT2D_Algorithm steganoAlgorithm;
	double[][] coverMessage;

	@Before
	public void setUp() throws Exception {
		Transform2d transform = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		coverMessage = createValue();
		//steganoAlgorithm = new DWTAlgorithm_2(coverMessage, transform);
	}

	@Test
	public void testGetStegoObjectRateFull() {
		double expected = 1;
		int quantityOfBytes = coverMessage.length * coverMessage[0].length / 4 / 8;
		//double actual = steganoAlgorithm.getStegoObjectRate(quantityOfBytes);
		//assertEquals(expected, actual, .001);
	}
	
	@Test
	public void testGetStegoObjectRate() {
		int quantityOfBytes = 10;
		double expected = ((double)quantityOfBytes) / (coverMessage.length * coverMessage[0].length / 4 / 8);
		//double actual = steganoAlgorithm.getStegoObjectRate(quantityOfBytes);
		//assertEquals(expected, actual, .001);
	}

	@Test
	public void testGetStegoObject() {
		boolean[] input = new boolean[]{
				true, true, false, true, false, true, false, true, 
				false, true, true, false, true, true, false, false};
		byte[] hide =  Converter.toShrinkArrayofByte(input);
		
		//double[][] stegoObject = steganoAlgorithm.getStegoObject(new CacheMessage(hide));
		
		//steganoAlgorithm.setCoverMessage(stegoObject);
		
		byte[] reverse = steganoAlgorithm.getEmbeddedData();
		
		boolean[] output = getBooleans(reverse);
		
		int count = ArrayOperations.countDifferentElement(input, output);
		
		assertTrue(count <= 3);
	}
	
	protected static boolean[] getBooleans(byte[] reverse) {
		BitEnumeration enumerator = new BitEnumeration(new CacheMessage(reverse));
		
		boolean[] output = new boolean[reverse.length * ByteInfo.BYTE_SIZE];
		
		int index = 0;
		while(enumerator.hasMoreElements())
			output[index++] = enumerator.nextElement();
		return output;
	}

	@Test
	public void testHasHiddenMessageTrue() {
		assertTrue(steganoAlgorithm.hasHiddenMessage());
	}
	
	@Test
	public void testHasHiddenMessageFalse() {
		//steganoAlgorithm.setCoverMessage(new double[1][3]);
		assertFalse(steganoAlgorithm.hasHiddenMessage());
	}
	
	protected double[][] createValue(){
		double[][] result = new double[8][8];
		for (int i = 0; i < result.length; i++)
			for (int j = 0; j < result[0].length; j++)
				result[i][j] = i * (result[0].length) + (j + 1);
		return result;
	}

}
