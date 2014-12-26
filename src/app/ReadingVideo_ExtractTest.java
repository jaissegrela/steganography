package app;

import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.opencv.core.Core;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.MatImage;

public class ReadingVideo_ExtractTest {

	public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception,
			ClassNotFoundException {

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

		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(null, keyPointSize, howManyPoints,
				pointsByBit, visibilityfactor, null);

		FFmpegFrameGrabber source = new FFmpegFrameGrabber(filename);
		FFmpegFrameGrabber stego = new FFmpegFrameGrabber(video_output);

		Frame frame_source = null;
		Frame frame_stego = null;

		source.start();
		stego.start();
		System.out.println(String.format("OpenCV %s", Core.VERSION));
		
		int count = 3;

		while ((frame_source = source.grabFrame()) != null && count > 0) {
			frame_stego = stego.grabFrame();

			IplImage image = frame_source.image;
			if (image != null) {
				if (frame_stego.image == null) {
					System.out.println("Frame null");
				} else {
					
					opencv_highgui.cvSaveImage(String.format("output\\%s_frame_source.jpg", count), image);
					opencv_highgui.cvSaveImage(String.format("output\\%s_frame_stego.jpg", count), frame_stego.image);
					
					algorithm.setOriginal(new MatImage(new Mat(image)));
					algorithm.setCoverMessage(new MatImage(new Mat(frame_stego.image)));

					byte[] outputMessage = algorithm.getEmbeddedData();
					System.out.println(String.format("Message %s", new String(outputMessage)));
					count--;
				}
			}
			System.out.println("Frame: " + source.getFrameNumber());
		}
		source.stop();
		stego.stop();
		System.out.println("Done!");
	}

}
