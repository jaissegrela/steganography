package app;

import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.utils.Arrays2d;

public class ResizeBMPImage {

	public static void main(String[] args) throws IOException {

		System.out.println("Creating bmp");
		Loader.load(opencv_core.class);

		String folder = "globo";
		args = new String[1];

		args[0] = String.format("output\\%s\\source.tif", folder);

		Mat mat = opencv_highgui.imread(args[0], opencv_highgui.CV_LOAD_IMAGE_COLOR);
//		Arrays2d.printBasicInfo(mat);
//		mat.convertTo(mat, CvType.CV_32FC3);
		//mat.convertTo(mat, CvType.CV_8UC3);
		Arrays2d.printBasicInfo(mat);
		String output = String.format("output\\%s\\source.%s", folder, "bmp");
		System.out.println(String.format("Saving %s", output));
		
		opencv_highgui.imwrite(output, mat);

		System.out.println("Done!");
	}

}
