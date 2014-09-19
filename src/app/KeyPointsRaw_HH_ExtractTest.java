package app;

import java.io.IOException;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_HH_Algorithm;
import core.algorithm.KeyPointRawAlgorithm;
import core.message.MatImage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;

public class KeyPointsRaw_HH_ExtractTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Keypoints algorithm extract test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    int keyPointSize = 8;
		int howManyPoints = 3;
		int levels = 3;
		int visibilityfactor = 194;
		
		String file = String.format("input\\lena.jpg");
	    
		Mat original = Highgui.imread(file);
		
		System.out.println("Channels: " + original.channels());
		System.out.println("Type: " + CvType.typeToString(original.type()));
		
		Transform2d alg = new Transform2dBasic(new DiscreteHaarWavelet());
		
		DWT2D_HH_Algorithm steganoAlgorithm = new DWT2D_HH_Algorithm(null, alg, null, visibilityfactor, levels);
	    
		MatImage coverMessage = new MatImage(original);
		KeyPointRawAlgorithm algorithm = new KeyPointRawAlgorithm(coverMessage,
				steganoAlgorithm, keyPointSize, howManyPoints, coverMessage);
		
		
		double[] zooms = {.5, .75, 1, 2};
		String[] extensions = {"bmp", "jpg", "png", "tiff"};
		//String[] extensions = {"jpg"};
		
		int[] solutions = new int[2];
		
		for (int k = 0; k < 2; k++) {
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
					
					System.out.println(String.format("Message %-4s z:%-4s i:%s %s", extensions[i], zooms[j], k, Arrays.toString(outputMessage)));
					
					if (k == outputMessage[0])
						solutions[k]++;
				}
			}
		}
		System.out.println(String.format("Resumen %s", Arrays.toString(solutions)));
		
		System.out.print("Done!");
	}

}
