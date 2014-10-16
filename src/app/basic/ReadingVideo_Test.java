package app.basic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
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
	    

	    String filename = "input\\test.avi";
	    String output = "output\\test_%s.jpg";
	    
		VideoCapture buffer = new VideoCapture();
	    boolean result = buffer.open(filename);
	    
	    DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
	    if(!result)
		{
			System.out.println(String.format("Cannot load the video: %s - %s", filename, new File(filename).exists()));
			return;
		}
	    long start = System.currentTimeMillis();
	    Mat image = new Mat();
	    while (buffer.read(image)) {			
			try{
				double milliseconds = buffer.get(0);
				System.out.println(String.format("Frame: %s Time: %s - %s", (int)buffer.get(1),
						formatter.format(new Date((long)milliseconds)),
						formatter.format(new Date((long)System.currentTimeMillis() - start))));
			}catch(Exception e){
				System.out.println(String.format("Could not save the image %s", output));	
			}
	    }
	    System.out.println(String.format("Elapsed Time: %s",
				formatter.format(new Date((long)System.currentTimeMillis() - start))));
		System.out.println(String.format("Frames %s - Time: %s - FPS: %s", (int)buffer.get(1),
				formatter.format(new Date((long)(buffer.get(0)))),
						buffer.get(5)));
		System.out.println("Done!");

	}

}
