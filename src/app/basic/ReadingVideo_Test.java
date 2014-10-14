package app.basic;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


public class ReadingVideo_Test {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		//SET PATH opencv\build\x64\vc10\bin opencv\sources\3rdparty\ffmpeg

		System.out.println("ReadingVideo_Test");
		
	    // Load the native library.
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    

	    String filename = "input\\test1.avi";
	    String output = "output\\test_%s.jpg";
	    
		VideoCapture buffer = new VideoCapture();
	    boolean result = buffer.open(filename);
	    if(!result)
		{
			System.out.println(String.format("Cannot load the video: %s - %s", filename, new File(filename).exists()));
			return;
		}
	    
	    Mat image = new Mat();
	    for (int i = 0; i < 24; i++) {			
		    buffer.read(image);
			try{
				Highgui.imwrite(String.format(output, i), image);
			}catch(Exception e){
				System.out.println(String.format("Could not save the image %s", output));	
			}
	    }
		
		System.out.println("Done!");

	}

}
