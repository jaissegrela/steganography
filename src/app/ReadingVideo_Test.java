package app;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.opencv.core.Core;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.IMessage;
import core.message.MatImage;

public class ReadingVideo_Test {

public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception, ClassNotFoundException {
		
	    // Load the native library.
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    Class.forName("org.bytedeco.javacpp.avutil");
	    Class.forName("org.bytedeco.javacpp.swresample");
	    
	    int keyPointSize = 8;
	    int pointsByBit = 5;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 10;

        
	    String filename = "input\\cartoon.mp4";
	    String video_output = "output\\cartoon.mp4";
	    
	    KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(null, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, null);
	    
	    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
	    Frame frame = null;

		grabber.start();
        System.out.println(String.format("OpenCV %s", Core.VERSION));
        
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(video_output, grabber.getImageWidth(),
        		grabber.getImageHeight(), grabber.getAudioChannels());
        
        
        recorder.setFormat(grabber.getFormat());
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.setFrameRate(grabber.getFrameRate());
        //recorder.setVideoBitrate(grabber.getV);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        System.out.println("Pixel Format:" + grabber.getPixelFormat());
        //recorder.setPixelFormat(grabber.getPixelFormat());
        
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.start();
        
        IMessage embeddedData = new CacheMessage("ABC".getBytes());
        int count = 3;
        while ((frame = grabber.grabFrame()) != null) {
        	
            IplImage image = frame.image;
			if (image != null && count > 0) {
				Mat mat = new Mat(image);
				algorithm.setCoverMessage(new MatImage(mat));
				MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
				stegoObject.getMat().copyTo(mat);
				count--;
            }
			recorder.record(frame);
			System.out.println("Frame: " + grabber.getFrameNumber());
        }
        recorder.stop();
        grabber.stop();
		System.out.println("Done!");
	}

}
