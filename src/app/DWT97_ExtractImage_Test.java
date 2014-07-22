package app;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import core.algorithm.DWTAlgorithm_2;
import core.message.BasicImageMessage;
import core.message.CacheMessage;
import core.transform.*;
import core.utils.ByteInfo;
import core.utils.enumerations.BitEnumeration;

public class DWT97_ExtractImage_Test {

	
	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	    String[] files = {"lena_gray_test256.bmp", "lena_gray_test512.bmp", "lena_gray_test1024.bmp", "lena_gray_test2048.bmp"};
	    
	    for (int i = 0; i < files.length; i++) {
			
		String inputFile = String.format("output\\%s", files[i]);
		
		double[][] pixels = createCoverMessage(inputFile);
		
		Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		DWTAlgorithm_2 steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);

		steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);
		
		byte[] reverse = steganoAlgorithm.getEmbeddedData();
		
		String outputFile = String.format("output\\message%s.bmp", pixels.length >> 1);
		save(pixels.length >> 1, pixels[0].length >> 1, reverse, 
				new FileOutputStream(outputFile));	
		
	    }
	}
	
	public void save(OutputStream stream, byte[] image, int dimention) throws IOException {
		BufferedImage result = new BufferedImage(dimention, dimention, BufferedImage.TYPE_BYTE_BINARY);
		byte[] targetPixels = ((DataBufferByte) result.getRaster().getDataBuffer()).getData();
		System.arraycopy(image, 0, targetPixels, 0, image.length);
		ImageIO.write(result, "bmp", stream);
	}

	protected static double[][] createCoverMessage(String file) {
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Size size = mat.size();
		System.out.println(String.format("%s %s %s %s", size.height, size.width, mat.channels(), CvType.typeToString(mat.type())));
		mat.convertTo(mat, CvType.CV_64FC1);
		size = mat.size();
		System.out.println(String.format("%s %s %s %s", size.height, size.width, mat.channels(), CvType.typeToString(mat.type())));
		double[][] result = new double[mat.height()][mat.width()];
		for (int i = 0; i < result.length; i++) {
			mat.get(i, 0, result[i]);
		}
		return result;
	}
	
	protected static void saveSteganoMessage(String inputFile, String outputFile, double[][] input) {
		Mat mat = Highgui.imread(inputFile, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		mat.convertTo(mat, CvType.CV_64FC1);
		for (int i = 0; i < input.length; i++) {
			mat.put(i, 0, input[i]);
		}
		Highgui.imwrite(outputFile, mat);
	}
	
	protected static byte[] createMessage(int dimention) throws IOException {
		String file = String.format("input\\identity%s.bmp", dimention >> 1);
		BasicImageMessage basicImageMessage = new BasicImageMessage(file);
		return basicImageMessage.getAllBytes();
	}
	
	protected static void save(int width, int height, byte[] data, OutputStream stream) throws IOException{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(data, 0, targetPixels, 0, data.length);
		ImageIO.write(image, "bmp", stream);
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
}
