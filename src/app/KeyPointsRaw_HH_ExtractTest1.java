package app;

import java.io.IOException;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.MatImage;
import core.utils.IMessageComparator;
import core.utils.MessageComparator;
import core.utils.MessageComparatorByPosition;

public class KeyPointsRaw_HH_ExtractTest1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm extract test");
		
		Loader.load(opencv_core.class);
	    
		int keyPointSize = 32;
	    int pointsByBit = 7;
	    String message = "ABC";
	    CacheMessage cacheMessage = new CacheMessage(message.getBytes());
	    IMessageComparator comp1 = new MessageComparator(cacheMessage);
	    IMessageComparator comp2 = new MessageComparatorByPosition(cacheMessage);
	    
		int howManyPoints = pointsByBit * message.length() * 8;
		int visibilityfactor = 40;
		
		String folder = "globo";
		String file = String.format("output\\%s\\source.tif", folder);
		System.out.println(String.format("Loading image %s...", file));
	    
		Mat original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		
		double[] zooms = {.75, .5, .4, .33};
		String[] extensions = {"bmp", "jpg", "png", "tif"};
		
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
				algorithm.setCoverMessage(null);
				CacheMessage actual = new CacheMessage(outputMessage);
				System.out.println(String.format("Format:%-4s zoom:%-4s Message: %s -> %s, Similarity: (%f; %f)",
						extensions[i], zooms[j], new String(outputMessage), Arrays.toString(outputMessage),
						comp1.similarity(actual), comp2.similarity(actual)));
				
			}
		}
		
		System.out.print("Done!");
	}

}
