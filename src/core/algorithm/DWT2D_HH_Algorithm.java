package core.algorithm;

import java.nio.DoubleBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;

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
		double[] pixel = new double[mat.channels()];
		DoubleBuffer in = mat.getDoubleBuffer();
		DoubleBuffer out = mat.getDoubleBuffer();
		for (int i = mat.rows() >> levels; i < mat.rows() >> (levels - 1); i++) {
			for (int j = mat.cols() >> levels; j < mat.cols() >> (levels - 1); j++) {
				Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
				in.get(pixel);
				for (int k = 0; k < pixel.length; k++) {
					if(value){
						pixel[k] += visibilityfactor;
					}	
				}
				out.put(pixel);
			}
		}
	}

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[getMaxSizeMessageToHide() >> ((levels - 1) << 1)];
		Mat matSource = primeCoverMessage.getMat();
		double factor = visibilityfactor * .35;
		for (int i = mat.rows() >> levels, index = 0; i < mat.rows() >> (levels - 1); i++) {
			for (int j = mat.cols() >> levels; j < mat.cols() >> (levels - 1) && index < result.length; j++) {
				double[] pixel = mat.get(i, j);
				double[] source = matSource.get(i, j);
				int value = 0;
				for (int k = 0; k < pixel.length; k++) {
					if(Math.abs(pixel[k] - source[k]) > factor)
						value++;
				}
				result[index++] = value >= ((double)pixel.length / 2);
			}
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
