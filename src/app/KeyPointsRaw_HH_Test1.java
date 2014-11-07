package app;

import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.opencv.core.Core;
import org.opencv.highgui.Highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.KeyPointOperation;

public class KeyPointsRaw_HH_Test1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 8;
	    int pointsByBit = 7;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 21;
		
		String file = "input\\export_04214.tif";
		String folder = "export";
	    
		Mat original = opencv_highgui.imread(file, Highgui.IMREAD_UNCHANGED);

		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		//for (int i = 0; i < 16; i++) {
			Mat mat;
			IMessage embeddedData = new CacheMessage("ABC".getBytes());
			algorithm.setCoverMessage(coverMessage);
			MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		
			//System.out.println(String.format("Hidding..."));
			mat = stegoObject.getMat();
			
			String output = String.format("output\\%s\\stego_image.tif", folder);
			//System.out.println(String.format("Saving..."));
			opencv_highgui.imwrite(output, mat);
			
			//System.out.println(String.format("Reading..."));
			mat = opencv_highgui.imread(output, Highgui.IMREAD_UNCHANGED);
			if(mat.size().width() == 0)
			{
				System.out.println(String.format("Cannot load the image %s", output));
				return;
			}
		
		//System.out.println(String.format("Decoding..."));
		algorithm.setCoverMessage(new MatImage(mat));
		byte[] outputMessage = algorithm.getEmbeddedData();
		
		System.out.println(String.format("Message %s %s", 0, new String(outputMessage)));
		//}
		output = String.format("output\\%s\\source.tif", folder);
		opencv_highgui.imwrite(output, original);
		
		String kp_output = String.format("output\\%s\\stego_image_00_keypoints.tif", folder);
		original = KeyPointOperation.drawKeypoints(original, keyPointSize, howManyPoints);
		opencv_highgui.imwrite(kp_output, original);
		
		System.out.print("Done!");
	}

}
