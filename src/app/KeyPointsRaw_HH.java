package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgproc;

import core.algorithm.DWT2D_HH_Bit_Algorithm;
import core.algorithm.KeyPointBitRawAlgorithm;
import core.message.CacheMessage;
import core.message.MatImage;
import core.utils.KeyPointImagesAlgorithm;
import core.utils.enumerations.KeyPointEnumeration;

public class KeyPointsRaw_HH {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Key Points Raw HH 1 algorithm test");
		
		System.out.println("Loading system...");
		Loader.load(opencv_core.class);
	    
	    int keyPointSize = 16;
	    int pointsByBit = 7;
	    int visibilityfactor = 4;
	    
	    CacheMessage cacheMessage = new CacheMessage(new byte[]{-101, 65, 78});
	    //CacheMessage cacheMessage = new CacheMessage(new byte[]{1});
		
	    String file = "input\\lena.jpg";
		String folder = "lena";
	    
		System.out.println(String.format("Loading image %s...", file));
		Mat coverimage = opencv_highgui.imread(file, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		MatImage coverMessage = new MatImage(coverimage);
		
		KeyPointBitRawAlgorithm algorithm = new KeyPointBitRawAlgorithm(coverMessage, 
				new DWT2D_HH_Bit_Algorithm(visibilityfactor), pointsByBit);
		algorithm.setEnumeration(new KeyPointEnumeration(keyPointSize));
		
		System.out.println("Hidding...");
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(cacheMessage);
		
		
		System.out.println(String.format("Saving..."));
		new File(String.format("output\\%s", folder)).mkdirs();
		String output = String.format("output\\%s\\stego_image.jpg", folder);
		opencv_highgui.cvSaveImage(output, stegoObject.getMat().asIplImage());
		
		System.out.println(String.format("Reading..."));
		Mat stegoimage = opencv_highgui.imread(output, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);

		if(stegoimage.size().width() == 0)
		{
			System.out.println(String.format("Cannot load the image %s", output));
			return;
		}
		
		System.out.println(String.format("Decoding..."));		
		algorithm.setStegoMessage(new MatImage(stegoimage));	
		
		byte[] outputMessage = algorithm.getEmbeddedData(cacheMessage.size());
		
		System.out.println(String.format("Message %s", Arrays.toString(outputMessage)));
		
		printPSNR(file, output);
		
		coverimage = opencv_highgui.imread(file);
		saveKeypoints(folder, coverimage, keyPointSize, cacheMessage.size() * pointsByBit);
		
		Files.copy(FileSystems.getDefault().getPath(file),
				FileSystems.getDefault().getPath(String.format("output\\%s\\source.jpg", folder)), StandardCopyOption.REPLACE_EXISTING);
		
		System.out.print("Done!");
	}

	protected static void saveKeypoints(String folder, Mat coverimage, int keyPointSize, int points) throws IOException {
		coverimage = KeyPointImagesAlgorithm.drawKeypoints(coverimage, keyPointSize, points);		
		String kp_output = String.format("output\\%s\\keypoints.jpg", folder);
		opencv_highgui.imwrite(kp_output, coverimage);
	}

	protected static void printPSNR(String file, String output) {
		Mat coverimage = opencv_highgui.imread(file);
		Mat stegoimage = opencv_highgui.imread(output);
		System.out.println(String.format("PSNR: %s", opencv_imgproc.PSNR(coverimage, stegoimage)));
	}

}
