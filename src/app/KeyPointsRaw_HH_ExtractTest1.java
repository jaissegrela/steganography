package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.MatImage;
import core.utils.IMessageComparator;
import core.utils.KPoint;
import core.utils.MessageComparator;
import core.utils.MessageComparatorByPosition;

public class KeyPointsRaw_HH_ExtractTest1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm extract test");
		
		Loader.load(opencv_core.class);
	    
		int keyPointSize = 16;
	    int pointsByBit = 7;
	    /*String message = "ABC";
	    CacheMessage cacheMessage = new CacheMessage(message.getBytes());*/
	    CacheMessage cacheMessage = new CacheMessage(new byte[]{-101, 65, 78});
	    IMessageComparator comp1 = new MessageComparator(cacheMessage);
	    IMessageComparator comp2 = new MessageComparatorByPosition(cacheMessage);
	    
	    CacheMessage cacheMessage1 = new CacheMessage(new byte[]{10, -18, 70});
	    IMessageComparator err1 = new MessageComparator(cacheMessage1);
	    IMessageComparator err2 = new MessageComparatorByPosition(cacheMessage1);
	    
		int howManyPoints = pointsByBit * cacheMessage.bytes() * 8;
		int visibilityfactor = 4;
		
		String folder = "lena";
		String file = String.format("output\\%s\\source.jpg", folder);
		System.out.println(String.format("Loading image %s...", file));
	    
		Mat original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		double[] zooms = {1, .75, .5, .4, .33};
		//String[] extensions = {"bmp", "jpg", "png", "tif"};
		String[] extensions = {"jpg"};
		
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
				System.out.println(String.format("Format:%-4s zoom:%-4s Message: %s -> %s, Similarity: (%f; %f) -%s%s- (%f; %f)",
						extensions[i], zooms[j], new String(outputMessage), Arrays.toString(outputMessage),
						comp1.similarity(actual), comp2.similarity(actual),
						comp1.similarity(actual) <= err1.similarity(actual) ? "****" : "",
						comp2.similarity(actual) <= err2.similarity(actual) ? "++++" : "",
						err1.similarity(actual), err2.similarity(actual)));
				
			}
		}
		
		System.out.print("Done!");
	}
	
	public static ArrayList<KeyPoint> getKeyPoints(String filename){
		ArrayList<KeyPoint> result = null;
		try
	      {
	         FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         ArrayList<KPoint> e = (ArrayList<KPoint>) in.readObject();
	         in.close();
	         fileIn.close();
	         result = new ArrayList<KeyPoint>(e.size());
	         for (KPoint kPoint : e) {
				result.add(kPoint.getKeyPoint());
			}
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	      }
		return result;
	}

}
