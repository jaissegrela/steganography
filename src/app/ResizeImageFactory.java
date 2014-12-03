package app;

import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.utils.ImageFactory;
import core.utils.Utils;

public class ResizeImageFactory {


	public static void main(String[] args) throws IOException {
	    
	    System.out.println("Creating images");
	    Loader.load(opencv_core.class);

	    String folder = "globo";
	    args = new String[1];
	    
	    args[0] = String.format("output\\%s\\stego_image.tif", folder);
		
	    double[] zooms = {1, .5, .4, .33, .25};
		String[] extensions = {"tif"};
		//String[] extensions = {"jpg"};
		
		for (int k = 0; k < args.length; k++) {
			Mat mat = opencv_highgui.imread(args[k], opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
			for (int i = 0; i < zooms.length; i++) {
				try{
					Mat result = ImageFactory.zoom(mat, zooms[i], zooms[i]);
					String name = Utils.getPathWithoutExtension(args[k]);
					for (int j = 0; j < extensions.length; j++) {
						String output = String.format("%s_%s.%s", name, zooms[i], extensions[j]);
						System.out.println(String.format("Saving %s", output));
						try{
							opencv_highgui.imwrite(output, result);
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
