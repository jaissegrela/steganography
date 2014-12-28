package app;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.opencv.core.Core;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;

public class ReadingImagefromVideo_Test {

public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception, ClassNotFoundException {
		
	    // Load the native library.
		System.loadLibrary("opencv_java2410");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    Class.forName("org.bytedeco.javacpp.avutil");
	    Class.forName("org.bytedeco.javacpp.swresample");
	    
	    int keyPointSize = 8;
	    int pointsByBit = 5;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 10;

        
	    String filename = "input\\globo.mp4";
	    String video_output = "output\\globo.mp4";
	    
	    KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(null, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, null);
	    
	    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
	    Frame frame = null;

		grabber.start();
        System.out.println(String.format("OpenCV %s", Core.VERSION));
        
        int count = 1;
        while ((frame = grabber.grabFrame()) != null) {
            IplImage image = frame.image;
			if (image != null && count > 0) {
				Mat mat = new Mat(image);
				opencv_highgui.imwrite(String.format("output\\images\\globo%s.tif", count++), mat);
            }
        }
        grabber.stop();
		System.out.println("Done!");
	}

}
