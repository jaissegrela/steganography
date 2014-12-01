package core.algorithm;

import java.nio.DoubleBuffer;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Range;

import core.message.ICoverMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2dBasic;
import core.utils.enumerations.BitEnumeration;

public class DWT2D_HH_Algorithm extends DWT2D_Algorithm implements ISteganographyMemoryAlgorithm{
	
	protected ICoverMessage primeCoverMessage;

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, ICoverMessage primeCoverMessage, double visibilityfactor, int levels) {
		super(coverMessage, new Transform2dBasic(new DiscreteHaarWavelet()) , visibilityfactor, levels);
		setPrimeCoverMessage(primeCoverMessage);
	}

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, ICoverMessage originalMessage) {
		this(coverMessage, originalMessage, 1, 1);
	}

	protected void transform(Mat mat, BitEnumeration enumerator) {
		Mat subMat = new opencv_core.Mat(mat, 
				new Range(mat.rows() >> levels,  mat.rows() >> (levels - 1)), 
				new Range(mat.cols() >> levels,  mat.cols() >> (levels - 1)));
		Mat clone = subMat.clone();
		double[] pixel = new double[mat.channels()];
		DoubleBuffer in = clone.getDoubleBuffer();
		DoubleBuffer out = clone.getDoubleBuffer();
		while(in.hasRemaining()){
			Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
			in.get(pixel);
			for (int k = 0; k < pixel.length; k++) {
				if(value){
					pixel[k] += visibilityfactor;
				}	
			}
			out.put(pixel);
		}
		clone.copyTo(subMat);
	}

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[getMaxSizeMessageToHide() >> ((levels - 1) << 1)];
		
		
		Mat subMat = new opencv_core.Mat(mat, 
				new Range(mat.rows() >> levels,  mat.rows() >> (levels - 1)), 
				new Range(mat.cols() >> levels,  mat.cols() >> (levels - 1))).clone();
		
		Mat matCoverMessage = primeCoverMessage.getMat();
		Mat subMatCoverMessage = new opencv_core.Mat(matCoverMessage, 
				new Range(matCoverMessage.rows() >> levels,  matCoverMessage.rows() >> (levels - 1)), 
				new Range(matCoverMessage.cols() >> levels,  matCoverMessage.cols() >> (levels - 1))).clone();
		
		DoubleBuffer inMat = subMat.getDoubleBuffer();
		DoubleBuffer inCover = subMatCoverMessage.getDoubleBuffer();
		
		double[] pixel = new double[mat.channels()];
		double[] source = new double[mat.channels()];
		int index = 0;
		double factor = visibilityfactor * .35;
		while(inMat.hasRemaining()){
			inMat.get(pixel);
			inCover.get(pixel);
			int value = 0;
			for (int k = 0; k < pixel.length; k++) {
				if(Math.abs(pixel[k] - source[k]) > factor)
					value++;
			}
			result[index++] = value >= ((double)pixel.length / 2);
		}
		return result;
	}

	@Override
	public void setPrimeCoverMessage(ICoverMessage primeCoverMessage) {
		if(primeCoverMessage != null)
			transform.transform(primeCoverMessage.getMat(), levels);
		this.primeCoverMessage = primeCoverMessage;
	}

}
