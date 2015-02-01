package core.algorithm;

import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core.Mat;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.Transform2d;
import core.utils.AccuracyEvaluator;
import core.utils.Converter;

public abstract class DWT2D_Algorithm implements ISteganographyAlgorithm{

	private ICoverMessage coverMessage;
	private ICoverMessage stegoMessage;
	private double visibilityfactor;
	private int levels;
	private Transform2d transform;
	private AccuracyEvaluator evaluator;
	
	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform, double visibilityfactor, int levels) {
		this(coverMessage, null, transform, visibilityfactor, levels);
	}

	public DWT2D_Algorithm(ICoverMessage coverMessage, Transform2d transform) {
		this(coverMessage, null, transform, 1, 1);
	}
	
	public DWT2D_Algorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage, Transform2d transform) {
		this(coverMessage, stegoMessage, transform, 1, 1);
	}
	
	public DWT2D_Algorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage, Transform2d transform, double visibilityfactor, int levels) {
		this.setCoverMessage(coverMessage);
		this.setStegoMessage(stegoMessage);
		this.setTransform(transform);
		this.setVisibilityfactor(visibilityfactor);
		this.setLevels(levels);
	}
	
	protected abstract void transform(Mat mat, Enumeration<Boolean> enumeration) ;
	
	protected abstract boolean[] inverse(Mat stegomessage, Mat covermessage, int size) ;

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		ICoverMessage result = coverMessage.duplicateMessage();
		Mat mat = result.getMat();
		transform.transform(mat, levels);
		transform(mat, embeddedData.getEnumeration());
		transform.inverse(mat, levels);
		return result;
	}

	@Override
	public byte[] getEmbeddedData(int size) {
		Mat stego  = getStegoMessage().getMat();
		Mat cover = getCoverMessage().getMat();
		int levels = getLevels();
		transform.transform(stego, levels);
		transform.transform(cover, levels);
		boolean[] result = inverse(stego, cover, size);
		transform.inverse(stego, levels);
		transform.inverse(cover, levels);
		return Converter.toShrinkArrayofByte(result);
	}

	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}
	
	public ICoverMessage getCoverMessage() {
		return this.coverMessage;
	}
	
	@Override
	public void setStegoMessage(ICoverMessage stegoMessage) {
		this.stegoMessage = stegoMessage;
	}
	
	public ICoverMessage getStegoMessage() {
		return this.stegoMessage;
	}

	public AccuracyEvaluator getEvaluator() {
		return evaluator;
	}
	
	public void setEvaluator(AccuracyEvaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	public double getVisibilityfactor() {
		return visibilityfactor;
	}
	
	public void setVisibilityfactor(double visibilityfactor) {
		this.visibilityfactor = visibilityfactor;
	}
	
	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}
	
	protected Transform2d getTransform() {
		return transform;
	}
	
	public void setTransform(Transform2d transform) {
		this.transform = transform;
	}

}