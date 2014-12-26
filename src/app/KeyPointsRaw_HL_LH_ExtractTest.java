package app;

import java.io.IOException;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.opencv.core.CvType;

import core.algorithm.DWT2D_HL_LH_Algorithm;
import core.algorithm.KeyPointRawAlgorithm;
import core.message.MatImage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;

public class KeyPointsRaw_HL_LH_ExtractTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints algorithm extract test");
		
		Loader.load(opencv_core.class);
	    
	    int keyPointSize = 4;
		int howManyPoints = 128;
		
		String file = String.format("input\\lena.jpg");
	    
		Mat original = opencv_highgui.imread(file);
		
		System.out.println("Channels: " + original.channels());
		System.out.println("Type: " + CvType.typeToString(original.type()));
		
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		
		DWT2D_HL_LH_Algorithm steganoAlgorithm = new DWT2D_HL_LH_Algorithm(null, alg, 0, 1);
	    
		KeyPointRawAlgorithm algorithm = new KeyPointRawAlgorithm(new MatImage(original),
				steganoAlgorithm, keyPointSize, howManyPoints, new MatImage(original));
		
		
		double[] zooms = {.5, .75, 1, 2};
		//String[] extensions = {"bmp", "jpg", "png", "tiff"};
		String[] extensions = {"jpg"};
		
		for (int k = 0; k < 16; k++) {
			System.out.println();
			for (int i = 0; i < extensions.length; i++) {
				for (int j = 0; j < zooms.length; j++) {
					String output = String.format("output\\lena_test_%s_%s.%s", k, zooms[j], extensions[i]);
					Mat mat;
					mat = opencv_highgui.imread(output);
					if(mat.size().width() == 0)
					{
						System.out.println(String.format("Cannot load the image %s", output));
						return;
					}
					algorithm.setCoverMessage(new MatImage(mat));
					byte[] outputMessage = algorithm.getEmbeddedData();
					
					System.out.println(String.format("Message %s %s", output, Arrays.toString(outputMessage)));
				}
			}
		}
		System.out.print("Done!");
	}

}
