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
import core.utils.Converter;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.BitEnumeration;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointRaw_HH_Algorithm implements ISteganographyAlgorithm{
	
	protected ICoverMessage coverMessage;
	protected ICoverMessage original;
	protected int keyPointSize, quantity, levels;
	protected ISteganographyAlgorithm steganoAlgorithm;
	protected int pointsByBit = 3;
	
	protected static final IMessage[] message = new IMessage[]{
		new CacheMessage(new byte[]{(byte)0}), 
		new CacheMessage(new byte[]{(byte)1})};
	
	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public KeyPointRaw_HH_Algorithm(ICoverMessage coverMessage,
			int keyPointSize, int quantity, int pointsByBit, double visibilityfactor) {
		this(coverMessage, keyPointSize, quantity, pointsByBit, visibilityfactor, null);
	}
	
	public KeyPointRaw_HH_Algorithm(ICoverMessage coverMessage, int keyPointSize,
			int quantity, int pointsByBit, double visibilityfactor, ICoverMessage original) {
		this.coverMessage = coverMessage;
		this.steganoAlgorithm = new DWT2D_HH_Bit_Algorithm(visibilityfactor);
		this.keyPointSize = keyPointSize;
		this.quantity = quantity;
		this.original = original;
		this.pointsByBit = pointsByBit;
	}

	@Override
	public double getStegoObjectRate(IMessage embeddedData) {
		return 1;
	}

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		
		ICoverMessage result = coverMessage.duplicateMessage();
		
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(result.getMat(), keyPointSize), quantity);
		
		Mat source = result.getMat();
		source.convertTo(source, opencv_core.CV_64FC3);
	    
		Mat prime = null;
		if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
			prime = original.getMat().clone();
			prime.convertTo(prime, opencv_core.CV_64FC3);
		}
		
		BitEnumeration enumeration = embeddedData.getEnumeration();
		
		while (enumeration.hasMoreElements() && keyPoints.hasMoreElements()) {
			Boolean value = (Boolean) enumeration.nextElement();
			for (int i = 0; i < pointsByBit  && keyPoints.hasMoreElements(); i++) {
		    	KeyPoint keyPoint = keyPoints.nextElement();
		    	Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
		    	steganoAlgorithm.setCoverMessage(new MatImage(rect.clone()));
		    	if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
		    		Mat temp = KeyPointOperation.getMatPoint(prime, keyPoint);
			    	((ISteganographyMemoryAlgorithm)steganoAlgorithm).setPrimeCoverMessage(new MatImage(temp.clone()));
		    	}
				MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(value ? message[1] : message[0]);
				stegoObject.getMat().copyTo(rect);
			}	
		}
	    return result;
	}

	@Override
	public boolean hasHiddenMessage() {
		return true;
	}

	@Override
	public byte[] getTypeMessage() {
		return null;
	}

	@Override
	public byte[] getEmbeddedData() {
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(
	    		new KeyPointEnumeration(original.getMat(), keyPointSize), quantity);
		
	    Mat source = coverMessage.getMat().clone();
	    
	    if(!areEqualSize(source)){
	    	source = ImageFactory.resizeImage(source, original.getMat().size());
	    }
	    
	    source.convertTo(source, opencv_core.CV_64FC3);
	    
	    Mat prime = null;
		if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
			prime = original.getMat().clone();
			prime.convertTo(prime, opencv_core.CV_64FC3);
		}
	    
		boolean[] result = new boolean[(int)Math.ceil(quantity / pointsByBit)];
		int index = 0;
	    while (keyPoints.hasMoreElements()) {
	    	int value = 0;
	    	//System.out.println("Indice:" + index);
	    	for (int i = 0; i < pointsByBit; i++) {
		    	KeyPoint keyPoint = keyPoints.nextElement();
		    	Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
		    	steganoAlgorithm.setCoverMessage(new MatImage(rect)); //it is no necessary to clone matrix because the algorithm will not modify it
		    	if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
		    		Mat temp = KeyPointOperation.getMatPoint(prime, keyPoint);
			    	((ISteganographyMemoryAlgorithm)steganoAlgorithm).setPrimeCoverMessage(new MatImage(temp.clone()));
		    	}
		    	byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
		    	if(embeddedData[0] > 0)
		    		value++;
	    	}
	    	result[index++] = value >= (pointsByBit / 2d);
	    	//System.out.println("Valor:" + (value >= (pointsByBit / 2d)));
		}
		return Converter.toShrinkArrayofByte(result);
	}

	protected boolean areEqualSize(Mat source) {
		Size s1 = source.size();
		Size s2 = original.getMat().size();
		return s1.width() == s2.width() && s1.height() == s2.height();
	}
	

	@Override
	public ICoverMessage getCoverMessage() {	    
	    return coverMessage;
	}

	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	@Override
	public int getMaxSizeMessageToHide() {
		return 0;
	}
}
