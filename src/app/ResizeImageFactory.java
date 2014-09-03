package app;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
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
	    
	    System.out.println("Resizing image");
	    
		String input = "output\\lena7.jpg";
		Mat mat = Highgui.imread(input);
				
		double[] zooms = {.5, .75, 1, 2, 4};
		
		for (int i = 0; i < zooms.length; i++) {
			Mat result = ImageFactory.zoom(mat, zooms[i], zooms[i]);
			String output = String.format("output\\lena7_test%s.jpg", result.height());
			System.out.println(String.format("Saving %s", output));
			Highgui.imwrite(output, result);
		}
		
		System.out.println("Done!");
	}

	protected static void createImage() throws IOException,
			FileNotFoundException {
		ImageFactory factory = new ImageFactory();
		BufferedImage image = factory.createIdentityImage("abc", 256, 256);
		ImageIO.write(image, "bmp", new FileOutputStream("output\\image.bmp"));
	}

}
