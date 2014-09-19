package core.algorithm;

import org.opencv.core.Mat;

import core.message.ICoverMessage;
import core.transform.Transform2d;
import core.utils.enumerations.BitEnumeration;

public class DWT2D_HH_Algorithm extends DWT2D_Algorithm implements ISteganographyMemoryAlgorithm{
	
	protected ICoverMessage primeCoverMessage;

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, Transform2d transform, ICoverMessage primeCoverMessage, double visibilityfactor, int levels) {
		super(coverMessage, transform, visibilityfactor, levels);
		setPrimeCoverMessage(primeCoverMessage);
	}

	public DWT2D_HH_Algorithm(ICoverMessage coverMessage, Transform2d transform, ICoverMessage originalMessage) {
		this(coverMessage, transform, originalMessage, 1, 1);
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
				result[index] = value >= ((double)pixel.length / 2);
				index++;
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
