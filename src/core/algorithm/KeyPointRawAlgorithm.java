package core.algorithm;

import java.util.ArrayList;
import java.util.Enumeration;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.features2d.KeyPoint;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.ArrayOperations;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointRawAlgorithm implements ISteganographyAlgorithm{
	
	protected ICoverMessage coverMessage;
	protected ICoverMessage original;
	protected int keyPointSize, quantity, levels;
	protected ISteganographyAlgorithm steganoAlgorithm;
	
	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public KeyPointRawAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm,
			int keyPointSize, int quantity) {
		this(coverMessage, steganoAlgorithm,
				keyPointSize, quantity, null);
	}
	
	public KeyPointRawAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm,
			int keyPointSize, int quantity, ICoverMessage original) {
		this.coverMessage = coverMessage;
		this.steganoAlgorithm = steganoAlgorithm;
		this.keyPointSize = keyPointSize;
		this.quantity = quantity;
		this.original = original;
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
		source.convertTo(source, CvType.CV_64FC1);
	    
		Mat prime = null;
		if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
			prime = original.getMat().clone();
			prime.convertTo(prime, CvType.CV_64FC1);
		}
		
		while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
	    	steganoAlgorithm.setCoverMessage(new MatImage(rect.clone()));
//	    	System.out.println("Source");
//	    	Arrays2d.print(rect);
	    	if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
	    		Mat temp = KeyPointOperation.getMatPoint(prime, keyPoint);
//	    		System.out.println("Prime");
//		    	Arrays2d.print(temp);
		    	((ISteganographyMemoryAlgorithm)steganoAlgorithm).setPrimeCoverMessage(new MatImage(temp.clone()));
	    	}
	    	
			MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(embeddedData);
			stegoObject.getMat().copyTo(rect);
			
//			System.out.println("Source 1");
//	    	Arrays2d.print(rect);
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
	    
	    if(!source.size().equals(original.getMat().size())){
	    	source = ImageFactory.resizeImage(source, original.getMat().size());
	    }
	    
	    source.convertTo(source, CvType.CV_64FC1);
	    
	    Mat prime = null;
		if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
			prime = original.getMat().clone();
			prime.convertTo(prime, CvType.CV_64FC1);
		}
	    
	    ArrayList<byte[]> messages = new ArrayList<byte[]>();
	    while (keyPoints.hasMoreElements()) {
	    	KeyPoint keyPoint = keyPoints.nextElement();
	    	Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
//	    	System.out.println("Source 2");
//	    	Arrays2d.print(rect);
	    	steganoAlgorithm.setCoverMessage(new MatImage(rect)); //it is no necessary to clone matrix because the algorithm will not modify it
	    	if(steganoAlgorithm instanceof ISteganographyMemoryAlgorithm){
	    		Mat temp = KeyPointOperation.getMatPoint(prime, keyPoint);
//	    		System.out.println("Prime 2");
//	    		Arrays2d.print(temp);
		    	((ISteganographyMemoryAlgorithm)steganoAlgorithm).setPrimeCoverMessage(new MatImage(temp.clone()));
	    	}
	    	byte[] embeddedData = steganoAlgorithm.getEmbeddedData();
//	    	System.out.println(Arrays.toString(embeddedData));
			messages.add(embeddedData);
		}
		return messages.size() > 0 ? ArrayOperations.compact(messages) : new byte[0];
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
