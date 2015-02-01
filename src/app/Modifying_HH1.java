package app;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.Enumeration;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Range;

import core.message.CacheMessage;
import core.message.IMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.Arrays2d;

public class Modifying_HH1 {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Statistics HH1 test");
		
		Loader.load(opencv_core.class);
	    
		int levels = 3;
		int visibilityfactor = (int)Math.pow(4, levels) * 3;
		int length = 8;
		
		Transform2d alg = new Transform2dBasic(new DiscreteHaarWavelet());
		IMessage embeddedData = new CacheMessage(new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1,});
		
		Enumeration<Boolean> enumeration = embeddedData.getEnumeration();
		
		double[][] values = new double[length][length];

		Mat mat = Arrays2d.createMat(values);
		alg.transform(mat, levels);
		transform(mat, enumeration, levels, visibilityfactor);
		Arrays2d.print(mat);
		alg.inverse(mat, levels);
		Arrays2d.print(mat);
	}

	protected static void transform(Mat mat, Enumeration<Boolean> enumeration, int levels, int visibilityfactor) {
		Mat subMat = new opencv_core.Mat(mat, new Range(mat.rows() >> levels, mat.rows() >> (levels - 1)), new Range(
				mat.cols() >> levels, mat.cols() >> (levels - 1)));
		Mat clone = subMat.clone();
		double[] pixel = new double[mat.channels()];
		DoubleBuffer in = clone.getDoubleBuffer();
		DoubleBuffer out = clone.getDoubleBuffer();
		while (in.hasRemaining()) {
			Boolean value = enumeration.hasMoreElements() ? enumeration.nextElement() : false;
			in.get(pixel);
			for (int k = 0; k < pixel.length; k++) {
				if (value) {
					pixel[k] += visibilityfactor;
				}
			}
			out.put(pixel);
		}
		clone.copyTo(subMat);
	}

}
