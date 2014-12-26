package app;

import java.io.IOException;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.DWT2D_HH_Algorithm;
import core.algorithm.KeyPointRawAlgorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;

public class KeyPointsRaw_HH_Test {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH algorithm test");
		
		Loader.load(opencv_core.class);
	    
	    int keyPointSize = 8;
		int howManyPoints = 3;
		int levels = 3;
		int visibilityfactor = 192;
		
		String file = "input\\lena.jpg";
	    
		Mat original = opencv_highgui.imread(file);

		
		DWT2D_HH_Algorithm steganoAlgorithm = new DWT2D_HH_Algorithm(null, null, visibilityfactor, levels);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRawAlgorithm algorithm = new KeyPointRawAlgorithm(coverMessage,
				steganoAlgorithm, keyPointSize, howManyPoints, coverMessage);
		
		byte[] temp = algorithm.getEmbeddedData();
		System.out.println(String.format("Message %s", Arrays.toString(temp)));
		
		for(int i = 0; i < 2; i++){
			String output = String.format("output\\lena_test_%s.jpg", i);
			Mat mat;
			IMessage embeddedData = new CacheMessage(new byte[]{(byte)i});
			algorithm.setCoverMessage(coverMessage);
			MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		
			//System.out.println(String.format("Hidding..."));
			mat = stegoObject.getMat();
			
			//System.out.println(String.format("Saving..."));
			opencv_highgui.imwrite(output, mat);
			
			//System.out.println(String.format("Reading..."));
			mat = opencv_highgui.imread(output);
			if(mat.size().width() == 0)
			{
				System.out.println(String.format("Cannot load the image %s", output));
				return;
			}
			
			//System.out.println(String.format("Decoding..."));
			algorithm.setCoverMessage(new MatImage(mat));
			byte[] outputMessage = algorithm.getEmbeddedData();;
			
			System.out.println(String.format("Message %s: %s", i, Arrays.toString(outputMessage)));
		}
//		String kp_output = String.format("output\\lena_kp.jpg", visibilityfactor);
//		original = KeyPointOperation.drawKeypoints(original, keyPointSize, howManyPoints);
//		opencv_highgui.imwrite(kp_output, original);
		
		System.out.print("Done!");
	}

}
