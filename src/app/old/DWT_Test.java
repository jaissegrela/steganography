package app.old;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class DWT_Test {

	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    String file = "lena_gray.jpg";
		Mat mat = Highgui.imread("input\\" + file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		mat.convertTo(mat, CvType.CV_32S);
		Size size = mat.size();
		
		int[][] pixels = get2dArray(mat);
		discreteHaarWaveletTransformOneLevel(pixels, 1);
		put2dArray(mat, pixels);
		
		int[] h = new int[(int)size.area() >> 2];
		mat.get(0, (int)size.width >> 1, h);
		
		int[] v = new int[(int)size.area() >> 2];
		mat.get((int)size.height >> 1, 0, v);
		
		int count = 0;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < v.length; i++) {
			if(h[i] > v[i])
				count++;
			max = Math.max(max, h[i] - v[i]);
			min = Math.min(min, h[i] - v[i]);
		}
		
		System.out.println(String.format("%s %s %s %s", count, v.length - count, max, min));
		
		Highgui.imwrite("output\\DWT_Test_lenaX.jpg", mat);
		
		
		
		System.out.println("DWT_Test Done!");
	    
	}
	
	public static void main2(String[] args) throws IOException {
		int[][] pixels = {
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1}};
		
		
		int levels = 2;
		discreteHaarWaveletTransformOneLevel(pixels, levels);
		inverseDiscreteHaarWaveletTransformOneLevel(pixels, levels);
		
		System.out.println("DWT_Test Done!");
	    
	}
	
	public static int[][] get2dArray(Mat mat)
	{
		int[][] result = new int[mat.height()][mat.width()];
		for (int i = 0; i < result.length; i++) {
			mat.get(i, 0, result[i]);
		}
		return result;
	}
	
	public static void put2dArray(Mat mat, int[][] pixels)
	{
		for (int i = 0; i < pixels.length; i++) {
			mat.put(i, 0, pixels[i]);
		}
	}
	
	public static void discreteHaarWaveletTransformOneLevel(int[][] input, int levels){
		int length = input.length;
		
		for (int k = 0; k < levels; k++) {
			for (int i = 0; i < input.length; i++) {
				discreteHaarWaveletTransformOneLevel(input[i], length);
			}
			
			diagonalSwap(input, length);
			
			for (int i = 0; i < input.length; i++) {
				discreteHaarWaveletTransformOneLevel(input[i], length);
			}
			
			diagonalSwap(input, length);
			
			length >>= 1;
		}
	}
	
	public static void inverseDiscreteHaarWaveletTransformOneLevel(int[][] input, int levels){
		int length = input.length;
		
		for(int i = 1; i < levels; i++)
			length >>= 1; 
		
		for (int k = 0; k < levels; k++) {
			
			diagonalSwap(input, length);
			
			for (int i = 0; i < input.length; i++) {
				inverseDiscreteHaarWaveletTransformOneLevel(input[i], length);
			}
			
			diagonalSwap(input, length);
			
			for (int i = 0; i < input.length; i++) {
				inverseDiscreteHaarWaveletTransformOneLevel(input[i], length);
			}
			
			length <<= 1;
		}
	}
	
	private static void diagonalSwap(int[][] input, int length) {
		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				int temp = input[i][j];
				input[i][j] = input[j][i];
				input[j][i] = temp;
			}
		}
	}

	public static void discreteHaarWaveletTransformOneLevel(int[] input, int length) {
	    int[] output = new int[length];
	    length >>= 1;
       
		for (int i = 0; i < length; i++) {
            int sum = input[i * 2] + input[i * 2 + 1];
            int difference = input[i * 2] - input[i * 2 + 1];
            output[i] = sum;
            output[length + i] = difference;
        }
	    
		System.arraycopy(output, 0, input, 0, length << 1);
	}
	
	public static void inverseDiscreteHaarWaveletTransformOneLevel(int[] input, int length) {
	    int[] output = new int[length];
	    length >>= 1;
       
		for (int i = 0; i < length; i++) {
            int sum = input[i];
            int difference = input[length + i];
            output[i * 2 + 1] = (sum - difference) >> 1;
            output[i * 2] = sum - output[i * 2 + 1];
        }
	    
		System.arraycopy(output, 0, input, 0, length << 1);
	}
	
	public static int[] discreteHaarWaveletTransform(int[] input) {
	    // This function assumes that input.length=2^n, n>1
	    int[] output = new int[input.length];
	    for (int length = input.length >> 1; ; length >>= 1) {
	        // length = input.length / 2^n, WITH n INCREASING to log(input.length) / log(2)
	        for (int i = 0; i < length; ++i) {
	            int sum = input[i * 2] + input[i * 2 + 1];
	            int difference = input[i * 2] - input[i * 2 + 1];
	            output[i] = sum;
	            output[length + i] = difference;
	        }
	        if (length == 1) {
	            return output;
	        }
	        
	        //Swap arrays to do next iteration
	        System.arraycopy(output, 0, input, 0, length << 1);
	    }
	}

}
