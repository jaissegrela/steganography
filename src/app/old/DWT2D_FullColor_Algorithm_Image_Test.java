package app.old;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_Algorithm;
import core.algorithm.DWT2D_HL_LH_Algorithm;
import core.message.BasicImageMessage;
import core.message.MatImage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.ImageFactory;

public class DWT2D_FullColor_Algorithm_Image_Test {
	
	public static Logger logger = Logger.getLogger(DWT2D_FullColor_Algorithm_Image_Test.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		PropertyConfigurator.configure("input\\config.properties");
		
		logger.info("Loading OpenCV...");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    String filename = "input\\lena.jpg";
	    int width = 256;
	    
	    logger.info(String.format("Loading image %s ...", filename));
	    
	    Mat mat = Highgui.imread(filename);
		mat.convertTo(mat, CvType.CV_64FC1);
	    
	    ImageFactory factory = new ImageFactory();
		
	    logger.info("Creating image identity...");
	    
		byte[] abc = factory.createIdentityImageInBytes("ABC", width, width);
	    BasicImageMessage message = new BasicImageMessage(abc);
	    
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
	    
		DWT2D_Algorithm algorithm = new DWT2D_HL_LH_Algorithm(new MatImage(mat), alg, 4, 1);
		
		logger.info("Hiding the image...");
		
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(message);
	    
		logger.info("Saving the stego-image...");
		
	    String output = "output\\lena_gray_64.jpg";
		Highgui.imwrite(output, stegoObject.getMat());

		logger.info("Reading the stego-image...");
	    Mat other = Highgui.imread(output);
	    
	    other.convertTo(other, CvType.CV_64FC1);
	    algorithm.setCoverMessage(new MatImage(other));
	    
	    logger.info("Extracting the image identity...");
	    byte[] embeddedData = algorithm.getEmbeddedData();
	    
	    logger.info("Saving the image identity...");
	    BufferedImage result = factory.createImage(width, width, embeddedData);
	    ImageIO.write(result, "bmp", new FileOutputStream("output\\lena_C.bmp"));
	    
	    logger.info("Applying filter and saving...");
	    result = ImageFactory.filter(result);
	    ImageIO.write(result, "bmp", new FileOutputStream("output\\lena_F.bmp"));
	    
	    logger.info("Applying filter and saving...");
	    result = ImageFactory.filter(result);
	    ImageIO.write(result, "bmp", new FileOutputStream("output\\lena_F2.bmp"));
	    
		logger.info("Done");
	}
	
	public static void truncate(Mat mat) {
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.width(); j++) {
				double[] ds = mat.get(i, j);
				for (int k = 0; k < ds.length; k++) {
					ds[k] = (int)ds[k];
				}
				mat.put(i, j, ds);
			}
			System.out.println();
		}
	}
	
	public static void print(Mat mat) throws IOException {
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.width(); j++) {
				double[] ds = mat.get(i, j);
				if(Math.abs(ds[0]) > .1)
					System.out.print(String.format("%s; ", Arrays.toString(ds)));
			}
			System.out.println();
		}
	}
	
	public static void statistics(Mat mat) throws IOException {
		double[] values = new double[1000];
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.width(); j++) {
				double[] ds = mat.get(i, j);
				for (int k = 0; k < ds.length; k++) {
					long round = Math.round(Math.abs(ds[k]));
					values[(int)round]++;
				}
			}
		}
		
		double count = mat.channels() * mat.size().area() / 100;
		for (int i = 0; i < values.length; i++) {
			values[i] /= count;
		}
		
		System.out.println(Arrays.toString(values));
	}
	
	protected BasicImageMessage getMessage(ImageFactory factory, int size)
			throws IOException {
		byte[] identityImageData = factory.createIdentityImageInBytes("ABC", size, size);
		BasicImageMessage message = new BasicImageMessage(identityImageData);
		return message;
	}

}
