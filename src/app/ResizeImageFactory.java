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

		String folder = "lena_gray";
		args = new String[1];

		args[0] = String.format("output\\%s\\stego_image.jpg", folder);
		System.out.println(String.format("Loading image %s...", args[0]));
		double[] zooms = {1, .75, .5, .4, .33 };
		//String[] extensions = { "bmp", "jpg", "png", "tif" };
		String[] extensions = { "jpg" };
		Mat mat = opencv_highgui.imread(args[0], opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
		for (int k = 0; k < args.length; k++) {
			String name = Utils.getPathWithoutExtension(args[k]);
			for (int j = 0; j < extensions.length; j++) {
//				if (hasTwoDepth(extensions[j])) {
//					mat = opencv_highgui.imread(args[k], opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
//				} else {
//					mat = opencv_highgui.imread(args[k], opencv_highgui.CV_LOAD_IMAGE_COLOR);
//				}
				for (int i = 0; i < zooms.length; i++) {
					try {
						Mat result = ImageFactory.zoom(mat, zooms[i], zooms[i]);
						String output = String.format("%s_%s.%s", name, zooms[i], extensions[j]);
						int quality = 80;
						System.out.println(String.format("Saving %s - %s", output, quality));
						
						try {
							
							opencv_highgui.imwrite(output, result, new int[]{opencv_highgui.IMWRITE_JPEG_QUALITY, quality});
						} catch (Exception e) {
							System.out.println(String.format("Could not save the image %s", output));
						}

					} catch (Exception e) {
						System.out.println(String.format("Error creating image %s with zoom: %s", args[k], zooms[i]));
					}
				}
			}
		}

		System.out.println("Done!");
	}

	private static boolean hasTwoDepth(String format) {
		format = format.toLowerCase();
		return !"bmp".equals(format) && !"jpg".equals(format);
	}

}
