package app.old;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.utils.ImageFactory;

public class ResizeImageFactory {


	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
//		String input = "output\\lena_gray_test.jpg";
//		Mat mat = Highgui.imread(input, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//		
		ImageFactory factory = new ImageFactory();
		BufferedImage image = factory.createIdentityImage("abc", 256, 256);
		ImageIO.write(image, "bmp", new FileOutputStream("output\\image.bmp"));
//		
//		double[] zooms = {.5, 1, 2, 4};
//		
//		for (int i = 0; i < zooms.length; i++) {
//			Mat resizeimage = ImageFactory.resizeImage(mat, zooms[i], zooms[i]);
//			String output = String.format("output\\lena_gray_test%s.jpg", mat.height());
//			Highgui.imwrite(output, resizeimage);
//		}
		
		System.out.println("Done!");
	}

}
