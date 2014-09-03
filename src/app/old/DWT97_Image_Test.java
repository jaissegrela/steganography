package app.old;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.message.BasicImageMessage;
import core.message.CacheMessage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;
import core.utils.ByteInfo;
import core.utils.enumerations.BitEnumeration;

public class DWT97_Image_Test {

	
	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    System.out.println("DWT97_Image_Test");
		
	    String file = "lena_gray";
		String inputFile = String.format("input\\%s.jpg", file);
		String outputFile = String.format("output\\%s_test.bmp", file);
		
		
		double[][] pixels = createCoverMessage(inputFile);

		byte[] hide = createMessage(pixels.length);
		
		Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		/*
		DWTAlgorithm_2 steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);
		
		pixels = steganoAlgorithm.getStegoObject(new CacheMessage(hide));
		saveSteganoMessage(inputFile, outputFile, pixels);

		steganoAlgorithm = new DWTAlgorithm_2(pixels, alg);
		
		byte[] reverse = steganoAlgorithm.getEmbeddedData();
		
		boolean[] eHide = ArrayOperations.getBooleans(hide);
		boolean[] eReverse = ArrayOperations.getBooleans(reverse);
	
		int differentElements = ArrayOperations.countWhiteDifferentElement(eHide, eReverse);
		int countElements = ArrayOperations.countElements(eHide, true);
		System.out.println(String.format("%s %s %f", differentElements, countElements, (double)differentElements/countElements));
		save(pixels.length >> 1, pixels[0].length >> 1, reverse, 
				new FileOutputStream("output\\identity256_bmp.bmp"));
		
		System.out.println("DWT97_Image_Test Done!!");
		*/
	}
	
	public void save(OutputStream stream, byte[] image, int dimention) throws IOException {
		BufferedImage result = new BufferedImage(dimention, dimention, BufferedImage.TYPE_BYTE_BINARY);
		byte[] targetPixels = ((DataBufferByte) result.getRaster().getDataBuffer()).getData();
		System.arraycopy(image, 0, targetPixels, 0, image.length);
		ImageIO.write(result, "bmp", stream);
	}

	protected static double[][] createCoverMessage(String file) {
		Mat mat = Highgui.imread(file, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		mat.convertTo(mat, CvType.CV_64FC1);
		return Arrays2d.getSource(mat);
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
