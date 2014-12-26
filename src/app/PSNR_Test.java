package app;

import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgproc;

public class PSNR_Test {

	public static void main(String[] args) throws IOException {
		
		System.out.println("PSNR");
		
		Loader.load(opencv_core.class);
		
		printPSNR("D:\\Stenganograhy\\steganography opencv\\steganography-opencv\\output\\lena\\source.jpg",
				"D:\\Projects\\workspace\\Steganography\\output\\lena\\source.jpg");
		
		printPSNR("D:\\Stenganograhy\\steganography opencv\\steganography-opencv\\output\\lena\\stego_image.jpg",
				"D:\\Projects\\workspace\\Steganography\\output\\lena\\stego_image.jpg");
		
		
		System.out.print("Done!");
	}

	protected static void printPSNR(String file1, String file2) {
		Mat mat1 = opencv_highgui.imread(file1);
		Mat mat2 = opencv_highgui.imread(file2);
		System.out.println(String.format("PSNR: %s", opencv_imgproc.PSNR(mat1, mat2)));
	}

}
