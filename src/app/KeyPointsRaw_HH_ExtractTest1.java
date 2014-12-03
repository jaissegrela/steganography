package app;

import java.io.IOException;
import java.util.Arrays;

import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.Mat;


import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.MatImage;

public class KeyPointsRaw_HH_ExtractTest1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm extract test");
	    
	    int keyPointSize = 8;
	    int pointsByBit = 7;
		int howManyPoints = pointsByBit * 32;
		int visibilityfactor = 21;
		
		String file = "output\\globo\\source.tif";
		String folder = "globo";
	    
		Mat original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		
		double[] zooms = {1, .5, .4, .33, .25, .15};
		String[] extensions = {"tif"};
		//String[] extensions = {"jpg"};
		
		for (int i = 0; i < extensions.length; i++) {
			System.out.println();
			for (int j = 0; j < zooms.length; j++) {
				String output = String.format("output\\%s\\stego_image_%s.%s", folder, zooms[j], extensions[i]);
				Mat mat;
				mat = opencv_highgui.imread(output, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
				if(mat.size().width() == 0)
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
