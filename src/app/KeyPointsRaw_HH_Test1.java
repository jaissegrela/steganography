package app;

import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.KeyPointImagesAlgorithm;

public class KeyPointsRaw_HH_Test1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm test");
		
		Loader.load(opencv_core.class);
	    
	    int keyPointSize = 8;
	    int pointsByBit = 3;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 7;
		
		String file = "input\\lena.jpg";
		String folder = "export";
	    
		Mat original = opencv_highgui.imread(file);

		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		IMessage embeddedData = new CacheMessage("ABC".getBytes());
		algorithm.setCoverMessage(coverMessage);
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
	
		System.out.println(String.format("Hidding..."));
		Mat mat = stegoObject.getMat();
		
		String output = String.format("output\\%s\\stego_image.jpg", folder);
		System.out.println(String.format("Saving..."));
		opencv_highgui.imwrite(output, mat);
		
		System.out.println(String.format("Reading..."));
//		mat = opencv_highgui.imread(output);
//		if(mat.size().width() == 0)
//		{
//			System.out.println(String.format("Cannot load the image %s", output));
//			return;
//		}
		
		System.out.println(String.format("Decoding..."));
		algorithm.setCoverMessage(new MatImage(mat));
		byte[] outputMessage = algorithm.getEmbeddedData();
		
		System.out.println(String.format("Message %s %s", 0, new String(outputMessage)));
		
		String kp_output = String.format("output\\%s\\stego_image_00_keypoints.jpg", folder);
		original = KeyPointImagesAlgorithm.drawKeypoints(original, keyPointSize, howManyPoints);
		opencv_highgui.imwrite(kp_output, original);
		
		System.out.print("Done!");
	}

}
