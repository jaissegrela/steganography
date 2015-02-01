package core.algorithm;

import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.AccuracyEvaluator;
import core.utils.Converter;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.IKeyPointEnumeration;

public class KeyPointBitRawAlgorithm implements ISteganographyAlgorithm{
	
	protected static final IMessage[] message = new IMessage[]{
		new CacheMessage(new byte[]{(byte)0}), 
		new CacheMessage(new byte[]{(byte)1})};
	
	private ICoverMessage coverMessage, stegoMessage;
	private ISteganographyAlgorithm steganoAlgorithm;
	private int quantity;
	private IKeyPointEnumeration enumeration;
	private AccuracyEvaluator evaluator;

	public KeyPointBitRawAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm, int quantity) {
		this(coverMessage, null, steganoAlgorithm, quantity);
	}

	public KeyPointBitRawAlgorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage,
			ISteganographyAlgorithm steganoAlgorithm, int quantity) {
		this.stegoMessage = stegoMessage;
		this.coverMessage = coverMessage;
		this.steganoAlgorithm = steganoAlgorithm;
		this.quantity = quantity;
	}

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		if(steganoAlgorithm == null)
			throw new IllegalStateException("You must set the stegano algorithm");
		
		ICoverMessage result = getCoverMessage().duplicateMessage();
		enumeration.reset(result.getMat());
		Mat source = result.getMat();
		int type = source.type();
		source.convertTo(source, opencv_core.CV_64FC3);
		Enumeration<Boolean> bitEnumeration = embeddedData.getEnumeration();
		while (bitEnumeration.hasMoreElements()) {
			Boolean value = (Boolean) bitEnumeration.nextElement();
			for (int i = 0; i < quantity; i++) {
				if(!enumeration.hasMoreElements())
					throw new IllegalStateException("The key points are insufficient.");
		    	KeyPoint keyPoint = enumeration.nextElement();
		    	Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
		    	steganoAlgorithm.setCoverMessage(new MatImage(rect.clone()));
				MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(value ? message[1] : message[0]);
				stegoObject.getMat().copyTo(rect);
			}	
		}
		
		source.convertTo(source, type);
	    return result;
	}

	@Override
	public byte[] getEmbeddedData(int size) {
		if(steganoAlgorithm == null)
			throw new IllegalStateException("You must set the stegano algorithm");
		
	    Mat stegoMat = getStegoMessage().getMat(); //.clone();
	    Mat coverMat = getCoverMessage().getMat();
	    
	    if(!areEqualSize(stegoMat)){	
			stegoMat = ImageFactory.resizeImage(stegoMat, coverMat.size());
	    }
	    
	    stegoMat.convertTo(stegoMat, opencv_core.CV_64FC3);
	    enumeration.reset(coverMat);
		coverMat.convertTo(coverMat, opencv_core.CV_64FC3);
	    
		boolean[] result = new boolean[size];
		int index = 0;
		for(int i = 0; i < size; i++){
			int value = 0;
	    	for (int j = 0; j < quantity; j++) {
		    	KeyPoint keyPoint = enumeration.nextElement();
		    	Mat rect = KeyPointOperation.getMatPoint(stegoMat, keyPoint);
		    	steganoAlgorithm.setStegoMessage(new MatImage(rect.clone())); 
		    	rect = KeyPointOperation.getMatPoint(coverMat, keyPoint);
			    steganoAlgorithm.setCoverMessage(new MatImage(rect.clone()));
		    	byte[] embeddedData = steganoAlgorithm.getEmbeddedData(1);
		    	boolean actived = embeddedData[0] > 0;
				if(actived)
		    		value++;
				if(evaluator != null)
					evaluator.addMinorStepResult(actived);
	    	}
	    	result[index] = value >= (quantity / 2d);
			if(evaluator != null)
				evaluator.addSincronizationResult(result[index]);
			index++;
		}
		return Converter.toShrinkArrayofByte(result);
	}

	protected boolean areEqualSize(Mat source) {
		Size s1 = source.size();
		Size s2 = getCoverMessage().getMat().size();
		return s1.width() == s2.width() && s1.height() == s2.height();
	}
	
	public ICoverMessage getCoverMessage() {
		return coverMessage;
	}

	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	public ICoverMessage getStegoMessage() {
		return stegoMessage;
	}
	
	@Override
	public void setStegoMessage(ICoverMessage stegoMessage) {
		this.stegoMessage = stegoMessage;
	}

	public ISteganographyAlgorithm getSteganoAlgorithm() {
		return steganoAlgorithm;
	}

	public void setSteganoAlgorithm(ISteganographyAlgorithm steganoAlgorithm) {
		this.steganoAlgorithm = steganoAlgorithm;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public IKeyPointEnumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(IKeyPointEnumeration enumeration) {
		this.enumeration = enumeration;
	}
	
	public AccuracyEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(AccuracyEvaluator evaluator) {
		this.evaluator = evaluator;
	}


}
