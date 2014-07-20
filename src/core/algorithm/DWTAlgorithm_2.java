package core.algorithm;

import core.message.IMessage;
import core.transform.Transform2d;
import core.utils.Arrays2d;
import core.utils.BitEnumeration;
import core.utils.Converter;

public class DWTAlgorithm_2{

	protected double[][] coverMessage;
	protected double visibilityfactor;
	protected Transform2d transform;
	

	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public DWTAlgorithm_2(double[][] coverMessage, Transform2d transform, double visibilityfactor) {
		this.coverMessage = coverMessage;
		this.transform = transform;
		this.visibilityfactor = visibilityfactor;
	}

	public DWTAlgorithm_2(double[][] coverMessage, Transform2d transform) {
		this(coverMessage, transform, 1);
	}

	public double getStegoObjectRate(int quantityOfBytes) {
		int i = coverMessage.length * coverMessage[0].length >> 5;
		return (double)quantityOfBytes
				/ i;
	}

	public double[][] getStegoObject(IMessage embeddedData) {
		double[][] result = Arrays2d.duplicate(coverMessage);
		
		BitEnumeration enumerator = new BitEnumeration(embeddedData);

		transform.transform(result, 1);

		transform(result, enumerator);
		
		transform.inverse(result, 1);

		return result;
	}

	protected void transform(double[][] result, BitEnumeration enumerator) {
		for (int i = 0; i < result.length / 2; i++) {
			for (int j = result.length / 2; j < result.length; j++) {
				Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
				if(value){
					double d1 = result[i][j] - result[j][i];
					if(d1 < visibilityfactor){
						result[i][j] += (visibilityfactor - d1) / 2;
						result[j][i] -= (visibilityfactor - d1) / 2;
					}
				}else{
					double d2 = result[j][i] - result[i][j];
					if(d2 >= visibilityfactor){
						result[i][j] -= (visibilityfactor - d2) / 2;
						result[j][i] += (visibilityfactor - d2) / 2;
					}
				}
			}
		}
	}

	public boolean hasHiddenMessage() {
		return getMaxSizeMessageToHide() > 0;
	}

	public byte[] getEmbeddedData() {
		boolean[] result = new boolean[getMaxSizeMessageToHide()];
		transform.transform(coverMessage, 1);
		inverse(result);
		return Converter.toShrinkArrayofByte(result);
	}

	protected void inverse(boolean[] result) {
		for (int i = 0, index = 0; i < coverMessage.length / 2 && index < result.length; i++) {
			for (int j = coverMessage.length / 2; j < coverMessage.length && index < result.length; j++) {
				result[index++] = coverMessage[i][j] > coverMessage[j][i];
			}
		}
	}

	public double[][] getCoverMessage() {
		return this.coverMessage;
	}

	public void setCoverMessage(double[][] coverMessage) {
		this.coverMessage = coverMessage;
	}

	public int getMaxSizeMessageToHide() {
		return coverMessage.length * coverMessage[0].length >> 2;
	}

}
