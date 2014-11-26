package app;

import java.io.IOException;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.MatImage;

public class KeyPointsRaw_HH_ExtractTest1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm extract test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 8;
	    int pointsByBit = 7;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 21;
		
		String file = "input\\export_04214.tif";
		String folder = "export";
	    
		Mat original = Highgui.imread(file);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		
		double[] zooms = {.5, .4, .33, .25, .15};
		String[] extensions = {"bmp", "jpg", "png", "tiff"};
		//String[] extensions = {"jpg"};
		
		for (int i = 0; i < extensions.length; i++) {
			System.out.println();
			for (int j = 0; j < zooms.length; j++) {
				String output = String.format("output\\%s\\stego_image_%s.%s", folder, zooms[j], extensions[i]);
				Mat mat;
				mat = Highgui.imread(output);
				if(mat.size().width == 0)
				{
					System.out.println(String.format("Cannot load the image %s", output));
					return;
				}
				algorithm.setCoverMessage(new MatImage(mat));
				byte[] outputMessage = algorithm.getEmbeddedData();
				
				System.out.println(String.format("Format:%-4s zoom:%-4s Message: %s -> %s", extensions[i], zooms[j], new String(outputMessage), Arrays.toString(outputMessage)));
				
			}
		}
		
		System.out.print("Done!");
	}

}
