package app.old;

import java.io.IOException;
import java.util.Arrays;

import core.message.CacheMessage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.ByteInfo;
import core.utils.Converter;
import core.utils.enumerations.BitEnumeration;

public class DWT97_Test {

	
	public static void main(String[] args) throws IOException {
		double[][] pixels = createCoverMessage();
		
		boolean[] input = new boolean[]{
				true, true, false, true, false, true, false, true, 
				false, true, true, false, true, true, false, false};
		byte[] hide =  Converter.toShrinkArrayofByte(input);
		
		
		print(pixels);
		
		Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		/*
		DWTAlgorithm_2 steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);
		
		pixels = steganoAlgorithm.getStegoObject(new CacheMessage(hide));
		
		print(pixels);
		

		double[][] error = getError(pixels, steganoAlgorithm.getCoverMessage());
		
		print(error);
		
		steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);
		
		byte[] reverse = steganoAlgorithm.getEmbeddedData();
		
		boolean[] output = get(reverse);

		System.out.println(ArrayOperations.countDifferentElement(input, output));
		
		System.out.println(Arrays.toString(input));
		System.out.println(Arrays.toString(output));
		*/
	}

	protected static double[][] createCoverMessage() {
		double[][] result = new double[8][8];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = i * (result[0].length) + (j + 1);
			}
		}
		return result;
	}

	protected static double[][] getError(double[][] left, double[][] right) {
		double[][] result = new double[left.length][left[0].length];
		for (int i = 0; i < left.length; i++) {
			for (int j = 0; j < left[0].length; j++) {
				result[i][j] = Math.abs(left[i][j] - right[i][j]);
			}
		}
		return result;
	}

	protected static boolean[] get(byte[] reverse) {
		BitEnumeration enumerator = new BitEnumeration(new CacheMessage(reverse));
		
		boolean[] output = new boolean[reverse.length * ByteInfo.BYTE_SIZE];
		int index = 0;
		while(enumerator.hasMoreElements()){
			output[index++] = enumerator.nextElement();
		}
		return output;
	}

	private static void print(double[][] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			System.out.println(Arrays.toString(pixels[i]));
		}
		System.out.println();
	}

}
