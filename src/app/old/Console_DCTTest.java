package app.old;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class Console_DCTTest {

	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		String filename = "input\\lena_gray.jpg";
		Mat image = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		printInfo(filename, image);
		//printBynaryImage(image);
		
		image = transformImage(image);
		//printImage(image);
		
		//printInfo("transformed image", image);
		filename = "output\\DCT_Test_logo.bmp";
		//image.convertTo(image, CvType.CV_16S);
		Highgui.imwrite(filename, image);
		printImage(image);
		//image = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		//image = inverseImage(image);
		
		//printBynaryImage(image, 3);
		//Highgui.imwrite("output\\DCT_logo.bmp", image);
		
		System.out.println("Done!");
	}

	protected static void printInfo(String filename, Mat image) {
		System.out.println("Image Info  " + filename);
		System.out.println(String.format("Size: %s; %s Type: %s", image.rows(), image.cols(), CvType.typeToString(image.type())));
	}
	
	public static void printImage(Mat image){
		for (int i = 0; i < image.rows(); i++) {
			for (int j = 0; j < image.cols(); j++) {
				double pixel = image.get(i, j)[0];
				System.out.print(String.format("%s|", (int)pixel));
			}
			System.out.println("*" + i + "*");
		}
	}
	
	public static void printBynaryImage(Mat image, int error){
		for (int i = 0; i < image.rows(); i++) {
			for (int j = 0; j < image.cols(); j++) {
				double pixel = image.get(i, j)[0];
				System.out.print(String.format("%s", Math.abs(pixel) < error ? 0 : 1));
			}
			System.out.println();
		}
	}
	
	public static void truncateBinaryImage(Mat image){
		for (int i = 0; i < image.rows(); i++) {
			for (int j = 0; j < image.cols(); j++) {
				double[] pixel = image.get(i, j);
				pixel[0] = pixel[0] == 0 ? 0 : 1;
				image.put(i, j, pixel);
			}
		}
	}
	
	public static void truncateImage(Mat image){
		for (int i = 0; i < image.rows(); i++) {
			for (int j = 0; j < image.cols(); j++) {
				double[] pixel = image.get(i, j);
				for (int k = 0; k < pixel.length; k++) {
					pixel[k] = (int)pixel[k];
				}
				image.put(i, j, pixel);
			}
		}
	}
	
	public static Mat transformImage(Mat image){
		try {
		    Mat secondImage = new Mat(image.rows(), image.cols(), CvType.CV_32FC1);
		    image.convertTo(secondImage, CvType.CV_32FC1);

		    int m = Core.getOptimalDFTSize(image.rows());
		    int n = Core.getOptimalDFTSize(image.cols()); // on the border add zero values

		    Mat padded = new Mat(new Size(n, m), CvType.CV_32FC1); // expand input image to optimal size

		    Imgproc.copyMakeBorder(secondImage, padded, 0, m - secondImage.rows(), 0, n - secondImage.cols(), Imgproc.BORDER_CONSTANT);

		    Mat result = new Mat(padded.size(), padded.type());
		    Core.dct(padded, result);

		    return result;

		} catch (Exception e) {
		    System.out.println("Blargh: " + e.toString());
		    return null;
		}
	}
	
	public static Mat inverseImage(Mat image){
		try {
		    Mat secondImage = new Mat(image.rows(), image.cols(), CvType.CV_32FC1);
		    image.convertTo(secondImage, CvType.CV_32FC1);

		    Mat result = new Mat(secondImage.size(), secondImage.type());
		    Core.idct(secondImage, result);

		    return result;

		} catch (Exception e) {
		    System.out.println("Blargh: " + e.toString());
		    return null;
		}
	}

}


/*
public static void main(String[] args) throws IOException {
	System.loadLibrary("opencv_java249");
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
	String filename = "input\\scene.bmp";
	Mat source = Highgui.imread(filename);
	
	Size size = source.size();
	byte[] pixels = new byte[(int)size.area() * source.channels()];
	source.get(0, 0, pixels);

	int type = source.type();
	System.out.println(CvType.typeToString(type));
	
	size = source.size();
	Mat trasf = new Mat(size, CvType.CV_32FC1);
	
	source.convertTo(trasf, CvType.CV_32FC1);
	
	size = trasf.size();
	byte[] pixels1 = new byte[(int)size.area() * trasf.channels()];
	trasf.get(0, 0, pixels1);
	
	type = trasf.type();
	System.out.println(CvType.typeToString(type));

	Mat dest = new Mat(size, CvType.CV_32FC1);
	
	Core.dct(trasf, dest);
	
	Highgui.imwrite("output\\DCT_Test_identity1.jpg", dest);
	
	System.out.print("Done!");
}
*/