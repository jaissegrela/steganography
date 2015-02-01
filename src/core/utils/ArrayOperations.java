package core.utils;

import java.util.Enumeration;
import java.util.List;

import core.message.CacheMessage;

public class ArrayOperations {
	
	public static int countDifferentElement(byte[] left, byte[] right){
		if(left.length != right.length) 
			throw new IllegalArgumentException(
					String.format("The length of the array must be equal, left = %s, rigth = %s", 
							left.length, right.length));
		int result = 0;
		for (int i = 0; i < left.length; i++)
			if(left[i] != right[i])
				result++;
		return result;
	}
	
	public static int countElements(byte[] input, byte value){
		int result = 0;
		for (int i = 0; i < input.length; i++)
			if(input[i] == value)
				result++;
		return result;
	}
	
	public static int countElements(boolean[] input, boolean value){
		int result = 0;
		for (int i = 0; i < input.length; i++)
			if(input[i] == value)
				result++;
		return result;
	}
	
	public static int countWhiteDifferentElement(byte[] left, byte[] right){
		if(left.length != right.length) 
			throw new IllegalArgumentException(
					String.format("The length of the array must be equal, left = %s, rigth = %s", 
							left.length, right.length));
		int result = 0;
		for (int i = 0; i < left.length; i++)
			if(left[i] == 0 && left[i] != right[i])
				result++;
		return result;
	}
	
	public static int countDifferentElement(boolean[] left, boolean[] right){
		if(left.length != right.length) 
			throw new IllegalArgumentException(
					String.format("The length of the array must be equal, left = %s, rigth = %s", 
							left.length, right.length));
		int result = 0;
		for (int i = 0; i < left.length; i++)
			if(left[i] != right[i])
				result++;
		return result;
	}
	
	public static int countWhiteDifferentElement(boolean[] left, boolean[] right){
		if(left.length != right.length) 
			throw new IllegalArgumentException(
					String.format("The length of the array must be equal, left = %s, rigth = %s", 
							left.length, right.length));
		int result = 0;
		for (int i = 0; i < left.length; i++)
			if(left[i] && !right[i])
				result++;
		return result;
	}
	
	public static boolean[] getBooleans(byte[] input) {
		Enumeration<Boolean> enumeration = new CacheMessage(input).getEnumeration();
		
		boolean[] output = new boolean[input.length * ByteInfo.BYTE_SIZE];
		int index = 0;
		while(enumeration.hasMoreElements()){
			output[index++] = enumeration.nextElement();
		}
		if(index < output.length)
			System.out.println("Warning --");
		return output;
	}
	
	public static byte[] compact(List<byte[]> input){
		if(input.size() == 0)return null;

		int[] temp = new int[input.get(0).length * ByteInfo.BYTE_SIZE];
		for (int i = 0; i < input.size(); i++) {
			byte[] bs = input.get(i);
			boolean[] booleans = getBooleans(bs);
			for (int j = 0; j < booleans.length; j++) {
				if(booleans[j])
					temp[j]++;
			}
		}
		double size = input.size() / 2d;
		boolean[] result = new boolean[temp.length];
		for (int i = 0; i < result.length; i++) {	
				if(temp[i] >= size)
					result[i] = true;
		}
		return Converter.toShrinkArrayofByte(result) ;
	}

}
