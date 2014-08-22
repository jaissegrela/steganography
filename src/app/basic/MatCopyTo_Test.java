package app.basic;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import core.utils.Arrays2d;

public class MatCopyTo_Test {
	
	public static Logger logger = Logger.getLogger(MatCopyTo_Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("input\\config.properties");
		
		logger.info("Loading OpenCV...");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		double[][][] original = new double[][][] {
				{ { 0, 0, 1 }, { 0, 1, 1 }, { 0, 2, 1 } },
				{ { 1, 0, 1 }, { 1, 1, 1 }, { 1, 2, 1 } },
				{ { 2, 0, 1 }, { 2, 1, 1 }, { 2, 2, 1 } } };
		
		Mat dest = createMat(original);
		
		double[][][] test = new double[][][] {
				{ { 1, 2, 3 }, { 4, 5, 6 } }, 
				{ { 7, 8, 9 }, { 0, 1, 2 } } };
		
		Mat source = createMat(test);
		
		Rect rect = new Rect(0, 1, 2, 2);
		Mat regionDest = new Mat(dest, rect);
		
		logger.info("dest");
		Arrays2d.print(dest);
		
		logger.info("regionDest");
		Arrays2d.print(regionDest);
		
		logger.info("source.setTo(regionDest)");
		source.copyTo(regionDest);
		
		logger.info("regionDest");
		Arrays2d.print(regionDest);
		
		logger.info("source");
		Arrays2d.print(dest);
	}
	
	protected static Mat createMat(double[][][] data) {
		Mat result = new Mat(data.length, data[0].length, CvType.CV_64FC3);
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				result.put(row, col, data[row][col]);
			}
		}
		return result;
	}

}
