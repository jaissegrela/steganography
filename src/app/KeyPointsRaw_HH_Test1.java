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

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.Arrays2d;
import core.utils.KeyPointImagesAlgorithm;

public class KeyPointsRaw_HH_Test1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm test");
		
		Loader.load(opencv_core.class);
	    
	    int keyPointSize = 8;
	    int pointsByBit = 7;
		int howManyPoints = pointsByBit * 32;
		int visibilityfactor = 21;
		
		String file = "input\\export_04214.tif";
		String folder = "globo";
	    
		Mat original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		
		System.out.println("Original");
		Arrays2d.printBasicInfo(original);

		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		IMessage embeddedData = new CacheMessage("ABCD".getBytes());
		algorithm.setCoverMessage(coverMessage);
		
		new File(String.format("output\\%s", folder)).mkdirs();
		
		System.out.println(String.format("Hidding..."));
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		Mat mat = stegoObject.getMat();
		
		String output = String.format("output\\%s\\stego_image.tif", folder);
		System.out.println(String.format("Saving..."));
		Arrays2d.printBasicInfo(mat);
		opencv_highgui.cvSaveImage(output, mat.asIplImage());
		
		System.out.println(String.format("Reading..."));
		mat = opencv_highgui.imread(output, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		Arrays2d.printBasicInfo(mat);

		if(mat.size().width() == 0)
		{
			System.out.println(String.format("Cannot load the image %s", output));
			return;
		}
		
		System.out.println(String.format("Decoding..."));
		original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		MatImage originalMessage = new MatImage(original);
		
		coverMessage = new MatImage(mat);

		algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, originalMessage);
		
		byte[] outputMessage = algorithm.getEmbeddedData();
		
		System.out.println(String.format("Message %s %s", 0, new String(outputMessage)));
		
		original = opencv_highgui.imread(file);
		mat = opencv_highgui.imread(output);
		System.out.println(String.format("PSNR: %s", opencv_imgproc.PSNR(original, mat)));
		
		original = KeyPointImagesAlgorithm.drawKeypoints(original, keyPointSize, howManyPoints);
		String kp_output = String.format("output\\%s\\stego_image_00_keypoints.tif", folder);
		opencv_highgui.imwrite(kp_output, original);
		
		output = String.format("output\\%s\\source.tif", folder);
		Files.copy(FileSystems.getDefault().getPath(file),
				FileSystems.getDefault().getPath(output), StandardCopyOption.REPLACE_EXISTING);
		
		System.out.print("Done!");
	}

}
