package core.algorithm;

import org.opencv.core.Mat;

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

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[getMaxSizeMessageToHide() >> ((levels - 1) << 1)];
		Mat matSource = primeCoverMessage.getMat();
		double factor = visibilityfactor * .35;
		for (int i = mat.height() >> levels, index = 0; i < mat.height() >> (levels - 1); i++) {
			for (int j = mat.width() >> levels; j < mat.width() >> (levels - 1) && index < result.length; j++) {
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
