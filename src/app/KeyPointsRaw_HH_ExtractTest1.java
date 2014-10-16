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
	    int pointsByBit = 3;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 7;
		
		String file = "input\\aero3.jpg";
	    
		Mat original = Highgui.imread(file);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		
		double[] zooms = {.75, .5, .4};
		String[] extensions = {"bmp", "jpg", "png", "tiff"};
		//String[] extensions = {"jpg"};
		
		for (int k = 0; k < 1; k++) {
			for (int i = 0; i < extensions.length; i++) {
				System.out.println();
				for (int j = 0; j < zooms.length; j++) {
					String output = String.format("output\\test_%s_%s.%s", k, zooms[j], extensions[i]);
					Mat mat;
					mat = Highgui.imread(output);
					if(mat.size().width == 0)
					{
						System.out.println(String.format("Cannot load the image %s", output));
						return;
					}
					algorithm.setCoverMessage(new MatImage(mat));
					byte[] outputMessage = algorithm.getEmbeddedData();
					
					System.out.println(String.format("Message %-4s z:%-4s i:%s %s -> %s", extensions[i], zooms[j], k, new String(outputMessage), Arrays.toString(outputMessage)));
					
				}
			}
		}
		
		System.out.print("Done!");
	}

}
