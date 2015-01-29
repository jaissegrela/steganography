package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.MatImage;
import core.utils.AccuracyEvaluator;
import core.utils.IMessageComparator;
import core.utils.KPoint;
import core.utils.MessageComparator;

public class KeyPointsRaw_HH_ExtractTest1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm extract test");
		
		Loader.load(opencv_core.class);
	    
		int keyPointSize = 16;
	    int pointsByBit = 9;
	    /*String message = "ABC";
	    CacheMessage cacheMessage = new CacheMessage(message.getBytes());*/
	    CacheMessage cacheMessage = new CacheMessage(new byte[]{-101, 65, 78});
	    IMessageComparator comp1 = new MessageComparator(cacheMessage);
	    //IMessageComparator comp2 = new MessageComparatorByPosition(cacheMessage);
	    
	    AccuracyEvaluator evaluator = new AccuracyEvaluator(cacheMessage, pointsByBit);
	    
//	    CacheMessage cacheMessage1 = new CacheMessage(new byte[]{78, -101, 65});
	    //CacheMessage cacheMessage3 = new CacheMessage(new byte[]{65, 78, 101});
//	    IMessageComparator err1 = new MessageComparator(cacheMessage1);
	    //IMessageComparator err2 = new MessageComparatorByPosition(cacheMessage1);
	    //IMessageComparator err3 = new MessageComparatorByPosition(cacheMessage3);
	    
//	    System.out.println(new MessageComparator(cacheMessage).similarity(cacheMessage1));
	    
		int howManyPoints = pointsByBit * cacheMessage.bytes() * 8;
		int visibilityfactor = 3;
		
		String folder = "lena";
		String file = String.format("output\\%s\\source.jpg", folder);
		System.out.println(String.format("Loading image %s...", file));
	    
		Mat original = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		long size = new File(file).length();
		
		//Mat stego = opencv_highgui.imread(String.format("output\\%s\\stego_image.tif", folder), opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		//System.out.println(String.format("Test -%s Compression and Zoom Attack-, Matrix Size: %s Point by Bit: %s Visibility Factor:%s", "100%", keyPointSize, pointsByBit, visibilityfactor));
		//System.out.println(String.format("PSNR: %.3f", opencv_imgproc.PSNR(original, stego)));
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, coverMessage);
		
		algorithm.setEvaluator(evaluator);
		
		double[] zooms = {1, .75, .5, .4, .33};
		//String[] extensions = {"bmp", "jpg", "png", "tif"};
		String[] extensions = {"jpg"};
		
		for (int i = 0; i < extensions.length; i++) {
			System.out.println();
			System.out.println("Format:" + extensions[i].toUpperCase());
//			System.out.println("Zoom\tADR\tBER\tADR\tBER\t");
			System.out.println("Zoom\tADR\tBER\tSIZE %");
			for (int j = 0; j < zooms.length; j++) {
				String output = String.format("output\\%s\\stego_image_%s.%s", folder, zooms[j], extensions[i]);
				Mat mat = opencv_highgui.imread(output, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
				if(mat.size().width() == 0)
				{
					System.out.println(String.format("Cannot load the image %s", output));
					return;
				}
				algorithm.setCoverMessage(new MatImage(mat));
				byte[] outputMessage = algorithm.getEmbeddedData();
				algorithm.setCoverMessage(null);
				CacheMessage actual = new CacheMessage(outputMessage);
//				
				System.out.println(String.format("%.2f\t%6.2f\t%6.3f\t%6.2f",
						zooms[j], comp1.similarity(actual), evaluator.minorStepResultErrorAccurancy(), new File(output).length() * 100d / size));
				
//				System.out.print(String.format("%.2f\t%6.2f\t%6.3f\t",
//						zooms[j], comp1.similarity(actual), evaluator.minorStepResultErrorAccurancy()));
//				evaluator.setMessage(cacheMessage1);
//				System.out.println(String.format("%5.2f\t%5.2f\t", evaluator.minorStepResultErrorAccurancy(), 
//						err1.similarity(actual)));
//				evaluator.setMessage(cacheMessage3);
//				System.out.print(String.format("%5.2f\t%5.2f\t%s", evaluator.minorStepResultErrorAccurancy(), 
//						err3.similarity(actual), Arrays.toString(outputMessage)));
//				evaluator.setMessage(cacheMessage);
//				System.out.println(comp2.similarity(actual) <= err2.similarity(actual) ? "**" : "");
				evaluator.reset();
			}
		}
		
		System.out.print("Done!");
	}
	
	@SuppressWarnings("unchecked")
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
