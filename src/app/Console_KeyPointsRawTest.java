package app;

import java.io.IOException;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_Algorithm;
import core.algorithm.KeyPointRawAlgorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.KeyPointOperation;

public class Console_KeyPointsRawTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints algorithm test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 4;
		int howManyPoints = 128;
		int visibilityfactor = 20;
		
		String file = String.format("input\\lena.jpg");
	    
		Mat original = Highgui.imread(file);
		
		System.out.println("Channels: " + original.channels());
		System.out.println("Type: " + CvType.typeToString(original.type()));
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		DWT2D_Algorithm steganoAlgorithm = new DWT2D_Algorithm(null, alg, visibilityfactor);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRawAlgorithm algorithm = new KeyPointRawAlgorithm(coverMessage,
				steganoAlgorithm, keyPointSize, howManyPoints, coverMessage);
		
		
		
		for(int i = 0; i < 16; i++){
			String output = String.format("output\\lena_test_%s.jpg", i);
			Mat mat;
			IMessage embeddedData = new CacheMessage(new byte[]{(byte)i});
			algorithm.setCoverMessage(coverMessage);
			MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		
			System.out.println(String.format("Hidding..."));
			mat = stegoObject.getMat();
			
			System.out.println(String.format("Saving..."));
			Highgui.imwrite(output, mat);
			
			System.out.println(String.format("Reading..."));
			mat = Highgui.imread(output);
			if(mat.size().width == 0)
			{
				System.out.println(String.format("Cannot load the image %s", output));
				return;
			}
			
			System.out.println(String.format("Decoding..."));
			algorithm.setCoverMessage(new MatImage(mat));
			byte[] outputMessage = algorithm.getEmbeddedData();
			
			System.out.println(String.format("Message %s", Arrays.toString(outputMessage)));
		}
		String kp_output = String.format("output\\lena_kp.jpg", visibilityfactor);
		original = KeyPointOperation.drawKeypoints(original, keyPointSize, howManyPoints);
		Highgui.imwrite(kp_output, original);
		
		System.out.print("Done!");
	}

}
