package app.basic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;


public class KPR_HH_Video_Test {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		//SET PATH opencv\build\x64\vc10\bin opencv\sources\3rdparty\ffmpeg

		System.out.println("KPR_HH_Video_Test");
		
	    // Load the native library.
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    

	    String filename = "input\\test.avi";
	    String output_source = "output\\%s_source.jpg";
	    String output_stego = "output\\%s_stego.jpg";
	    
	    int keyPointSize = 8;
	    int pointsByBit = 5;
		int howManyPoints = pointsByBit * 24;
		int visibilityfactor = 5;
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");

		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(null, keyPointSize, 
				howManyPoints, pointsByBit, visibilityfactor, null);
		byte[] message = "ABC".getBytes();
		printInfo(message);
		
		IMessage embeddedData = new CacheMessage(message);
		
		VideoCapture buffer = new VideoCapture();

		boolean result = buffer.open(filename);
	    if(!result)
		{
			System.out.println(String.format("Cannot load the video: %s - %s", filename, new File(filename).exists()));
			return;
		}
	    long start = System.currentTimeMillis();
	    Mat image = new Mat();
	    int position = 0;
	    while (buffer.read(image)) {			
			position = (int)buffer.get(1);
			try{
				double milliseconds = buffer.get(0);
				System.out.println(String.format("Frame: %s Time: %s - %s", position,
						formatter.format(new Date((long)milliseconds)),
						formatter.format(new Date((long)System.currentTimeMillis() - start))));
				
				ICoverMessage coverMessage = new MatImage(image.clone());
				algorithm.setCoverMessage(coverMessage);
				MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
				stegoObject.getMat();
				Highgui.imwrite(String.format(output_stego, position), stegoObject.getMat());
				//Highgui.imwrite(String.format(output_source, position), image);
				
			}catch(Exception e){
				System.out.println(e.getMessage());
				System.out.println(String.format("Could not save the image %s", buffer.get(0)));	
			}
	    }
	    System.out.println(String.format("Elapsed Time: %s",
				formatter.format(new Date((long)System.currentTimeMillis() - start))));
		System.out.println(String.format("Frames %s - Time: %s - FPS: %s", position,
				formatter.format(new Date((long)(buffer.get(0)))),
						buffer.get(5)));
						
		System.out.println("KPR_HH_Video_Test - Done!");

	}

	private static void printInfo(byte[] message) {
		StringBuilder str = new StringBuilder(message.length * 8);
		for (int i = 0; i < message.length; i++) {
			str.append(String.format("%8s", Integer.toBinaryString(message[i])).replace(' ', '0'));
		}
		int count = 0;
		for (int i = 0; i < message.length * 8; i++) {
			if(str.charAt(i) == '1')
				count++;
		}
		System.out.println();
		System.out.println(String.format("%s - %s - %s", new String(message), count, str));
	}
	

}
	