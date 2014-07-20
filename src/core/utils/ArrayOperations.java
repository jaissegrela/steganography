package core.utils;

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
		BitEnumeration enumerator = new BitEnumeration(new CacheMessage(input));
		
		boolean[] output = new boolean[input.length * ByteInfo.BYTE_SIZE];
		int index = 0;
		while(enumerator.hasMoreElements()){
			output[index++] = enumerator.nextElement();
		}
		if(index < output.length)
			System.out.println("Warning --");
		return output;
	}
}
