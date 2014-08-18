package app;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Split_Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Split Test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    String input = "input\\lena.jpg";
		Mat mRgb = Highgui.imread(input);
	    System.out.println("Channels: " + mRgb.channels());
	    
		List<Mat> lRgb = new ArrayList<Mat>(mRgb.channels());
		List<Mat> lzeros = new ArrayList<Mat>(mRgb.channels());
		List<Mat> temp = new ArrayList<Mat>(mRgb.channels());
		Core.split(mRgb, lRgb);
		
		for (Mat mat : lRgb) {
			Mat zeros = new Mat(mat.size(), mat.type());
			lzeros.add(zeros);
		}
		
		for (int i = 0; i < lRgb.size(); i++) {
			temp.clear();
			for (int j = 0; j < lRgb.size(); j++) {
				Mat mat; 
				if(j != i)	
					mat = lzeros.get(j);
				else
					mat = lRgb.get(j);
				temp.add(mat);
			}
			Mat mTemp = new Mat();
			Core.merge(temp, mTemp);
			Highgui.imwrite(String.format("output\\Split_%s.jpg", i), mTemp);
		}
		
		System.out.println("info");
		System.out.println(CvType.typeToString(lRgb.get(0).type()));
		
		Highgui.imwrite(String.format("output\\Split_%s.jpg", 4), lRgb.get(0));

		Mat mSum = new Mat();
		Core.merge(lRgb, mSum);

		String output = "output\\Split.jpg";
		Highgui.imwrite(output, mSum);
		
		System.out.println("Done");
	}

}
