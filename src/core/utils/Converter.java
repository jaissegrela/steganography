package core.utils;


/**
 * Operations for converting a {@link Integer} to array of {@link Byte} and vice
 * versa
 * 
 * @author Jaisse Grela
 */
public class Converter {

	/**
	 * Gets the array of bytes representation for an integer value
	 * 
	 * @param value
	 *            the int value
	 * @return the array of bytes
	 */
	public static byte[] toArrayOfByte(int value) {
		byte[] result = new byte[4];
		for (int i = result.length - 1; i >= 0; i--) {
			result[i] = (byte) value;
			value >>= 8;
		}
		return result;
	}
	
	public static byte[] toArrayOfSByte(int value) {
		byte[] result = new byte[4];
		for (int i = result.length - 1; i >= 0; i--) {
			result[i] = (byte) value;
			if(result[i] < 0)
				result[i] += 256;
			value >>= 8;
		}
		return result;
	}
	
	public static boolean[] toArrayofBoolean(byte input){
		boolean[] result = new boolean[ByteInfo.BYTE_SIZE];
		for (int i = 0; i < result.length; i++) {
			result[i] = ByteInfo.getLSB(input) > 0;
			input >>= 1;
		}
		return result;
	}
	
	public static byte[] toShrinkArrayofByte(boolean[] input){
		int length = (int)Math.ceil((double)input.length / ByteInfo.BYTE_SIZE);
		byte[] result = new byte[length];
		for (int i = 0; i < input.length; i++) {
			if(input[i])
				result[i / ByteInfo.BYTE_SIZE] = ByteInfo.activeByte(result[i / ByteInfo.BYTE_SIZE],
						i % ByteInfo.BYTE_SIZE);
		}
		return result;
	}

	
	public static byte[] toArrayofByte(boolean[] input){
		byte[] result = new byte[input.length];
		for (int i = 0; i < input.length; i++)
			if(input[i])
				result[i] = 1;
		return result;
	}
	
	
	

	/**
	 * Gets the int representation of an array of bytes
	 * 
	 * @param value
	 *            the array of byte
	 * @return the int representation
	 * @throws IllegalArgumentException
	 *             if the length of value is greater than 4
	 */
	public static int toInt(byte[] value) {
		if (value.length > 4)
			throw new IllegalArgumentException();
		int result = 0;
		for (int i = 0; i < value.length; i++) {
			result <<= 8;
			int temp = (int) value[i];
			if (temp < 0)
				temp += 256;
			result |= temp;
		}
		return result;
	}
	
	public static int equal(int[] a, int[] b){
		if(a.length != b.length)
			return -2;
		for (int i = 0; i < b.length; i++) {
			if(a[i] != b[i])
				return i;
		}
		return -1;
	}

	public static int[] toPixel(int[] ipixels, int quantity) {
		int[] result = new int[ipixels.length / quantity];
		int position = 0;
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < quantity; j++) {
				result[i] = (result[i] << 8) | ipixels[position++];
			}
		}
		return result;
	}
	
	public static int[] fromPixel(int[] ipixels, int quantity) {
		int[] result = new int[ipixels.length * quantity];
		int position = 0;
		for (int i = 0; i < ipixels.length; i++) {
			byte[] arrayOfByte = Converter.toArrayOfByte(ipixels[i]);
			for (int j = arrayOfByte.length - quantity; j < arrayOfByte.length; j++) {
				if(arrayOfByte[j] < 0)
					result[position++] = arrayOfByte[j] + 256;
				else
					result[position++] = arrayOfByte[j];
			}
		}
		return result;
	}

	public static int[] toInteger(byte[] pixels) {
		int[] ipixels = new int[pixels.length];
		for(int i = 0; i < pixels.length; i++)
			ipixels[i] = pixels[i] & 255;
		return ipixels;
	}
	
	public static int[] fromByteBGRtoIntRGB(byte[] bgrBytes){
		int[] result = new int[bgrBytes.length / 3];
		for (int i = 0; i < result.length; i++) {
			int b = (bgrBytes[i * 3] & 0xFF) ;
            int g = (bgrBytes[i * 3 + 1] & 0xFF) << 8;
            int r = (bgrBytes[i * 3 + 2] & 0xFF) << 16;
            result[i] = r | g | b;
		}
		return result;
	}
	
	public static byte[] fromIntRGBtoByteBGR(int[] intRGB){
		byte[] result = new byte[intRGB.length * 3];
		for (int i = 0; i < intRGB.length; i++) {
			byte[] arrayOfByte = Converter.toArrayOfSByte(intRGB[i]);
            result[i * 3 + 2] = arrayOfByte[1];
            result[i * 3 + 1] = arrayOfByte[2];
            result[i * 3] = arrayOfByte[3];
		}
		return result;
	}
}
