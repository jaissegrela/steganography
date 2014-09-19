package app.old;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_HL_LH_Algorithm;
import core.algorithm.KeyPointImageAlgorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.ImageFactory;

public class Console_KeyPointsDoubleTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints algorithm test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 128;
		int howManyPoints = 2;
		int visibilityfactor = 1;
		
		String file = String.format("input\\lena.jpg");
	    
		Mat original = Highgui.imread(file);
		
		System.out.println("Channels: " + original.channels());
		System.out.println("Type: " + CvType.typeToString(original.type()));
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		DWT2D_HL_LH_Algorithm steganoAlgorithm = new DWT2D_HL_LH_Algorithm(null, alg, visibilityfactor, 1);
	    
		KeyPointImageAlgorithm algorithm = new KeyPointImageAlgorithm(new MatImage(original),
				steganoAlgorithm, visibilityfactor, keyPointSize, howManyPoints, new MatImage(original));
		
		IMessage embeddedData = new CacheMessage("ABC".getBytes());
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		
		String output = String.format("output\\lena%s_test.jpg", visibilityfactor);
		Mat mat;
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
		
		ImageFactory factory = new ImageFactory();
		
		System.out.println(String.format("Decoding..."));
		algorithm.setCoverMessage(new MatImage(mat));
		byte[] outputMessage = algorithm.getEmbeddedData();
		BufferedImage image = factory.createImage(keyPointSize >> 1, keyPointSize >> 1, outputMessage);
		
		System.out.println(String.format("Saving..."));
		String filename = String.format("output\\lena_message%s_nofilter.bmp", visibilityfactor);
		ImageIO.write(image, "bmp", new FileOutputStream(filename));
		
		//algorithm.inverse(mat, filename, steganoAlgorithm, keyPointSize, howManyPoints, original);
		
		System.out.print("Done!");
	}

	protected static void printDifferenceValue(Mat mat, Mat temp) {
		List<Mat> list1 = new ArrayList<Mat>();
		List<Mat> list2 = new ArrayList<Mat>();
		Core.split(mat, list1);
		Core.split(temp, list2);
		for (int i = 0; i < list1.size(); i++) {
			mat = list1.get(i);
			temp = list2.get(i);
			Mat subtract = new Mat();
			Core.subtract(mat, temp, subtract);
			for (int k = 0; k < mat.height(); k++) {
				for (int j = 0; j < mat.rows(); j++) {
					double[] ds = subtract.get(k, j);
					if(ds[0] > 0)
						System.out.println(String.format("%s, %s, %s, %s, %s", k, j, Arrays.toString(ds), 
								Arrays.toString(mat.get(k,  j)), Arrays.toString(temp.get(k, j))));
				}
			}
		}
	}

}
