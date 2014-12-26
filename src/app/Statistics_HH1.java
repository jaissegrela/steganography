package app;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import core.message.CacheMessage;
import core.message.IMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;
import core.utils.enumerations.BitEnumeration;

public class Statistics_HH1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Statistics HH1 test");
		
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
		int levels = 5;
		int visibilityfactor = (int)Math.pow(4, levels) * 3;
		int length = (int)Math.pow(2, levels);
		
		Transform2d alg = new Transform2dBasic(new DiscreteHaarWavelet());
		IMessage embeddedData = new CacheMessage(new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1,});
		
		BitEnumeration enumerator = new BitEnumeration(embeddedData);
		
		double[][] values = new double[length][length];

		Mat mat = Arrays2d.createMat(values);
		alg.transform(mat, levels);
		transform(mat, enumerator, levels, visibilityfactor);
		//Arrays2d.print(mat);
		alg.inverse(mat, levels);
		Arrays2d.print(mat);
	}

	protected static void transform(Mat mat, BitEnumeration enumerator, int levels, int visibilityfactor) {
		for (int i = mat.height() >> levels; i < mat.height() >> (levels - 1); i++) {
			for (int j = mat.width() >> levels; j < mat.width() >> (levels - 1); j++) {
				Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
				double[] pixel = mat.get(i, j);
				for (int k = 0; k < pixel.length; k++) {
					if(value){
						pixel[k] += visibilityfactor;
					}	
				}
				mat.put(i, j, pixel);
			}
		}
	}

}
