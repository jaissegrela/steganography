package core.algorithm;

import java.nio.DoubleBuffer;
import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Range;

import core.message.ICoverMessage;
import core.transform.Transform2d;

public class DWT2D_HH_Algorithm extends DWT2D_Algorithm {

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, Transform2d transform, double visibilityfactor, int levels) {
		super(coverMessage, transform, visibilityfactor, levels);
	}

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, Transform2d transform) {
		super(coverMessage, transform);
	}

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage, Transform2d transform) {
		super(coverMessage, stegoMessage, transform, 1, 1);
	}

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage, Transform2d transform,
			double visibilityfactor, int levels) {
		super(coverMessage, stegoMessage, transform, visibilityfactor, levels);
	}

	@Override
	protected void transform(Mat mat, Enumeration<Boolean> enumeration) {
		int levels = getLevels();
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
					pixel[k] += getVisibilityfactor();
				}
			}
			out.put(pixel);
		}
		clone.copyTo(subMat);
	}

	protected boolean[] inverse(Mat stegomessage, Mat covermessage, int size) {
		boolean[] result = new boolean[size];

		int rows = stegomessage.rows();
		int cols = stegomessage.cols();
		int levels = getLevels();
		Mat subMat = new opencv_core.Mat(stegomessage, new Range(rows >> levels, rows >> (levels - 1)), new Range(
				cols >> levels, cols >> (levels - 1))).clone();

		Mat subMatCoverMessage = new opencv_core.Mat(covermessage, new Range(rows >> levels, rows >> (levels - 1)),
				new Range(cols >> levels, cols >> (levels - 1))).clone();

		DoubleBuffer inMat = subMat.getDoubleBuffer();
		DoubleBuffer inCover = subMatCoverMessage.getDoubleBuffer();

		double[] pixel = new double[stegomessage.channels()];
		double[] source = new double[covermessage.channels()];
		int index = 0;
		double factor = getVisibilityfactor() * .3;
		while (inMat.hasRemaining()) {
			inMat.get(pixel);
			inCover.get(source);
			int value = 0;
			for (int k = 0; k < pixel.length; k++) {
				boolean actived = Math.abs(pixel[k] - source[k]) > factor;
				if (actived)
					value++;
			}
			result[index++] = value >= ((double) pixel.length / 2);
		}
		return result;
	}

}
