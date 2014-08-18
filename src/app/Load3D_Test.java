package app;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWTAlgorithm_2;
import core.message.BasicImageMessage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;
import core.utils.ImageFactory;

public class Load3D_Test {
	
	static Logger logger = Logger.getLogger(Load3D_Test.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("Load Test 3D");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    PropertyConfigurator.configure("input\\config.properties");

	    String filename = "input\\lena.jpg";
	    int width = 256;
	    
	    System.out.println("CV_LOAD_IMAGE_COLOR");
	    
		ImageFactory factory = new ImageFactory();
		byte[] abc = factory.createIdentityImageInBytes("ABC", width, width);
	    BasicImageMessage message = new BasicImageMessage(abc);
		
	    Mat input = Highgui.imread(filename);
	    
	    Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		DWTAlgorithm_2 algorithm = new DWTAlgorithm_2(null, alg, 4);
	    
	    List<Mat> list = new ArrayList<Mat>();
	    List<BufferedImage> images = new ArrayList<BufferedImage>();
	    Core.split(input, list);
	    
	    for (int i = 0; i < list.size(); i++) {
			Mat mat = list.get(i);
			mat.convertTo(mat, CvType.CV_64FC1);
			double[][] source = Arrays2d.getSource(mat);
			algorithm.setCoverMessage(source);
		    double[][] stegoObject = algorithm.getStegoObject(message);
		    Arrays2d.putSource(mat, stegoObject, 0, 0);
	    }
	    
		Mat result = new Mat();
		Core.merge(list, result);
		String output = String.format("output\\lena_C.jpg");
		Highgui.imwrite(output, result);
		
		
		list = new ArrayList<Mat>();
		input = Highgui.imread(output);
	    Core.split(input, list);
		
	    for (int i = 0; i < list.size(); i++) {
			Mat mat = list.get(i);
			mat.convertTo(mat, CvType.CV_64FC1);
			double[][] source = Arrays2d.getSource(mat);
			algorithm.setCoverMessage(source);
	    	
		    byte[] embeddedData = algorithm.getEmbeddedData();
		    output = String.format("output\\lena_CI_%s.bmp", i);
		    save(width, width, embeddedData, new FileOutputStream(output));
		    
		    images.add(factory.createImage(width, width, embeddedData));
	    }
		BufferedImage mergeImage = ImageFactory.createMergeImage(images);
		output = String.format("output\\lena_Merge.bmp");
		mergeImage = ImageFactory.filter(mergeImage);
		ImageIO.write(mergeImage, "bmp", new FileOutputStream(output));
		output = String.format("output\\lena_Merge1.bmp");
		mergeImage = ImageFactory.filter(mergeImage);
		ImageIO.write(mergeImage, "bmp", new FileOutputStream(output));
		output = String.format("output\\lena_Merge2.bmp");
		ImageIO.write(mergeImage, "bmp", new FileOutputStream(output));
		
		System.out.println("Done");
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

	public static void save(int width, int height, byte[] data, OutputStream stream) throws IOException{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(data, 0, targetPixels, 0, data.length);
		ImageIO.write(image, "bmp", stream);
	}
	
	public static void print(Mat mat) throws IOException {
		for (int i = 0; i < mat.height(); i++) {
			for (int j = 0; j < mat.width(); j++) {
				double[] ds = mat.get(i, j);
			    logger.info(String.format("%s; ", Arrays.toString(ds)));
			}
		}
	}
	
	public static void statistics(Mat mat) throws IOException {
		double[] values = new double[100];
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
