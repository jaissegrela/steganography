package app;

import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.utils.Arrays2d;

public class CopyImage {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Copying Image Test");
		
		Loader.load(opencv_core.class);
		
		String file = "input\\export_04214.tif";
		String folder = "globo";
	    
		Mat mat = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		Arrays2d.printBasicInfo(mat);
		
		mat.convertTo(mat, opencv_core.CV_64FC3);
		Arrays2d.printBasicInfo(mat);
		
		mat.convertTo(mat, opencv_core.CV_16UC3);
		Arrays2d.printBasicInfo(mat);
		
		String output = String.format("output\\%s\\stego_image.tif", folder);
		opencv_highgui.cvSaveImage(output, mat.asIplImage());
				System.out.print("Done!");
	}

}
