package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.core.CvType;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.Arrays2d;
import core.utils.KeyPointImagesAlgorithm;

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
