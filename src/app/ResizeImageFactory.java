package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.utils.ImageFactory;
import core.utils.Utils;

public class ResizeImageFactory {


	public static void main(String[] args) throws IOException {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    System.out.println("Creating images");
		
	    args = new String[2];
	    
	    for (int i = 0; i < 2; i++) {
			args[i] = String.format("output\\lena_test_%s.jpg", i);
		}
	    
	    
	    double[] zooms = {.5, .75, 1, 2};
		String[] extensions = {"bmp", "jpg", "png", "tiff"};
		//String[] extensions = {"jpg"};
		
		for (int k = 0; k < args.length; k++) {
			Mat mat = Highgui.imread(args[k]);
			for (int i = 0; i < zooms.length; i++) {
				try{
					Mat result = ImageFactory.zoom(mat, zooms[i], zooms[i]);
					String name = Utils.getPathWithoutExtension(args[k]);
					for (int j = 0; j < extensions.length; j++) {
						String output = String.format("%s_%s.%s", name, zooms[i], extensions[j]);
						System.out.println(String.format("Saving %s", output));
						try{
							Highgui.imwrite(output, result);
						}catch(Exception e){
							System.out.println(String.format("Could not save the image %s", output));	
						}
					}
				}catch(Exception e){
					System.out.println(String.format("Error creating image %s with zoom: %s", args[k], zooms[i]));	
				}
			}	
		}
		
		System.out.println("Done!");
	}

}
