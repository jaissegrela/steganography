package app;

import java.io.IOException;

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
		int howManyPoints = 3 * 24;
		int visibilityfactor = 64 * 8;
		
		String file = "input\\lena.jpg";
	    
		Mat original = Highgui.imread(file);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, visibilityfactor, coverMessage);
		
		
		double[] zooms = {.5, .75};
		String[] extensions = {"bmp", "jpg", "png", "tiff"};
		//String[] extensions = {"jpg"};
		
		for (int k = 0; k < 1; k++) {
			System.out.println();
			for (int i = 0; i < extensions.length; i++) {
				for (int j = 0; j < zooms.length; j++) {
					String output = String.format("output\\lena_test_%s_%s.%s", k, zooms[j], extensions[i]);
					Mat mat;
					mat = Highgui.imread(output);
					if(mat.size().width == 0)
					{
						System.out.println(String.format("Cannot load the image %s", output));
						return;
					}
					algorithm.setCoverMessage(new MatImage(mat));
					byte[] outputMessage = algorithm.getEmbeddedData();
					
					System.out.println(String.format("Message %-4s z:%-4s i:%s %s", extensions[i], zooms[j], k, new String(outputMessage)));
					
				}
			}
		}
		
		System.out.print("Done!");
	}

}
