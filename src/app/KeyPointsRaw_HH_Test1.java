package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
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
	    int pointsByBit = 3;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 7;
		
		String file = "input\\lena.jpg";
		String folder = "lena";
	    
		Mat original = Highgui.imread(file);

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
			
			String output = String.format("output\\%s\\stego_image.jpg", folder);
			//System.out.println(String.format("Saving..."));
			Highgui.imwrite(output, mat);
			
			//System.out.println(String.format("Reading..."));
			mat = Highgui.imread(output);
			if(mat.size().width == 0)
			{
				System.out.println(String.format("Cannot load the image %s", output));
				return;
			}
		
		//System.out.println(String.format("Decoding..."));
		algorithm.setCoverMessage(new MatImage(mat));
		byte[] outputMessage = algorithm.getEmbeddedData();
		
		System.out.println(String.format("Message %s %s", 0, new String(outputMessage)));
		//}
		output = String.format("output\\%s\\source.jpg", folder);
		Highgui.imwrite(output, original);
		
		String kp_output = String.format("output\\%s\\source_keypoints.jpg", folder);
		original = KeyPointOperation.drawKeypoints(original, keyPointSize, howManyPoints);
		Highgui.imwrite(kp_output, original);
		
		System.out.print("Done!");
	}

}
