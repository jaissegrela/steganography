package core.algorithm;

import org.opencv.core.Mat;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.Transform2d;
import core.utils.Converter;
import core.utils.enumerations.BitEnumeration;

public class DWT2D_Algorithm implements ISteganographyAlgorithm{

	protected ICoverMessage coverMessage;
	protected double visibilityfactor;
	protected Transform2d transform;
	

	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform, double visibilityfactor) {
		this.coverMessage = coverMessage;
		this.transform = transform;
		this.visibilityfactor = visibilityfactor;
	}

	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform) {
		this(coverMessage, transform, 1);
	}

	public double getStegoObjectRate(IMessage embeddedData) {
		int i = (int)coverMessage.getMat().size().area() >> 5;
		return (double)embeddedData.bytes() / i;
	}

	public ICoverMessage getStegoObject(IMessage embeddedData) {
		if(getStegoObjectRate(embeddedData) > 1) {
			throw new IllegalArgumentException(String.format("Rate: %s", getStegoObjectRate(embeddedData)));
		}
		
		ICoverMessage result = coverMessage.duplicateMessage();
		
		Mat mat = result.getMat();
		
		BitEnumeration enumerator = new BitEnumeration(embeddedData);

		transform.transform(mat, 1);

		transform(mat, enumerator);
		
		transform.inverse(mat, 1);

		return result;
	}

	protected void transform(Mat result, BitEnumeration enumerator) {
		for (int i = 0; i < (result.height() >> 1); i++) {
			for (int j = result.rows() >> 1; j < result.rows(); j++) {
				Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
				double[] result_i_j = result.get(i, j);
				double[] result_j_i = result.get(j, i);
				for (int k = 0; k < result_j_i.length; k++) {
					if(value){
						double d1 = result_i_j[k] - result_j_i[k];
						if(d1 < visibilityfactor){
							result_i_j[k] += (visibilityfactor - d1) / 2;
							result_j_i[k] -= (visibilityfactor - d1) / 2;
						}
					}else{
						double d2 = result_j_i[k] - result_i_j[k];
						if(d2 >= visibilityfactor){
							result_i_j[k] -= (visibilityfactor - d2) / 2;
							result_j_i[k] += (visibilityfactor - d2) / 2;
						}
					}	
				}
				result.put(j, i, result_j_i);
				result.put(i, j, result_i_j);
			}
		}
	}

	public boolean hasHiddenMessage() {
		return getMaxSizeMessageToHide() > 0;
	}

	public byte[] getEmbeddedData() {
		boolean[] result = new boolean[getMaxSizeMessageToHide()];
		Mat mat = coverMessage.getMat();
		transform.transform(mat, 1);
		inverse(result);
		return Converter.toShrinkArrayofByte(result);
	}

	protected void inverse(boolean[] result) {
		Mat mat = coverMessage.getMat();
		for (int i = 0, index = 0; i < mat.height() >> 1; i++) {
			for (int j = mat.width() >> 1; j < mat.width() && index < result.length; j++) {
				double[] result_i_j = mat.get(i, j);
				double[] result_j_i = mat.get(j, i);
				int value = 0;
				for (int k = 0; k < result_j_i.length; k++) {
					if(result_i_j[k] > result_j_i[k])
						value++;
				}
				result[index++] = value >= ((double)result_i_j.length / 2);
			}
		}
	}

	public ICoverMessage getCoverMessage() {
		return this.coverMessage;
	}
	
	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	public int getMaxSizeMessageToHide() {
		return (int)coverMessage.getMat().size().area() >> 2;
	}

	@Override
	public byte[] getTypeMessage() {
		return null;
	}

	

}
