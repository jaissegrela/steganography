package core.algorithm;

import org.bytedeco.javacpp.opencv_core.Mat;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.Transform2d;
import core.utils.Arrays2d;
import core.utils.Converter;
import core.utils.enumerations.BitEnumeration;

public abstract class DWT2D_Algorithm implements ISteganographyAlgorithm{

	protected ICoverMessage coverMessage;
	protected double visibilityfactor;
	protected int levels;
	protected Transform2d transform;

	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform, double visibilityfactor, int levels) {
		this.coverMessage = coverMessage;
		this.transform = transform;
		this.visibilityfactor = visibilityfactor;
		this.levels = levels;
	}

	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform) {
		this(coverMessage, transform, 1, 1);
	}
	
	protected abstract void transform(Mat mat, BitEnumeration enumerator) ;
	
	protected abstract boolean[] inverse(Mat mat) ;

	public double getStegoObjectRate(IMessage embeddedData) {
		int i = (int)coverMessage.getMat().size().area() >> 5;
		return (double)embeddedData.bytes() / i;
	}

	public ICoverMessage getStegoObject(IMessage embeddedData) {
		ICoverMessage result = coverMessage.duplicateMessage();
		Mat mat = result.getMat();
		BitEnumeration enumerator = new BitEnumeration(embeddedData);
		transform.transform(mat, levels);
		transform(mat, enumerator);
		transform.inverse(mat, levels);
		return result;
	}

	public boolean hasHiddenMessage() {
		return getMaxSizeMessageToHide() > 0;
	}

	public byte[] getEmbeddedData() {
		Mat mat = coverMessage.getMat();
		transform.transform(mat, levels);
		boolean[] result = inverse(mat);
		transform.inverse(mat, levels);
		return Converter.toShrinkArrayofByte(result);
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