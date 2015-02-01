package core.algorithm;

import java.util.ArrayList;
import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.utils.ArrayOperations;
import core.utils.ImageFactory;
import core.utils.KeyPointOperation;
import core.utils.enumerations.IKeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointRawAlgorithm implements ISteganographyAlgorithm {

	private ICoverMessage coverMessage, stegoMessage;
	private ISteganographyAlgorithm steganoAlgorithm;
	private int quantity;
	private IKeyPointEnumeration enumeration;

	public KeyPointRawAlgorithm(ICoverMessage coverMessage, ISteganographyAlgorithm steganoAlgorithm, int quantity) {
		this(coverMessage, null, steganoAlgorithm, quantity);
	}

	public KeyPointRawAlgorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage,
			ISteganographyAlgorithm steganoAlgorithm, int quantity) {
		this.stegoMessage = stegoMessage;
		this.coverMessage = coverMessage;
		this.steganoAlgorithm = steganoAlgorithm;
		this.quantity = quantity;
	}

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		if(steganoAlgorithm == null)
			throw new IllegalStateException("You must set the ategano algorithm");
		
		ICoverMessage result = coverMessage.duplicateMessage();
		enumeration.reset(result.getMat());
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(enumeration, quantity);

		Mat source = result.getMat();
		source.convertTo(source, opencv_core.CV_64FC1);

		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setCoverMessage(new MatImage(rect.clone()));
			MatImage stegoObject = (MatImage) steganoAlgorithm.getStegoObject(embeddedData);
			stegoObject.getMat().copyTo(rect);
		}

		return result;
	}

	@Override
	public byte[] getEmbeddedData(int size) {
		if(steganoAlgorithm == null)
			throw new IllegalStateException("You must set the ategano algorithm");
		
		Enumeration<KeyPoint> keyPoints = new TopEnumeration<KeyPoint>(enumeration, quantity);

		Mat source = stegoMessage.getMat().clone();

		if (!source.size().equals(coverMessage.getMat().size())) {
			source = ImageFactory.resizeImage(source, coverMessage.getMat().size());
		}
		source.convertTo(source, opencv_core.CV_64FC1);

		Mat stego = coverMessage.getMat().clone();
		stego.convertTo(stego, opencv_core.CV_64FC1);

		ArrayList<byte[]> messages = new ArrayList<byte[]>();
		while (keyPoints.hasMoreElements()) {
			KeyPoint keyPoint = keyPoints.nextElement();
			Mat rect = KeyPointOperation.getMatPoint(source, keyPoint);
			steganoAlgorithm.setStegoMessage((new MatImage(rect))); // it is no
																	// necessary
																	// to clone
																	// matrix
																	// because
																	// the
																	// algorithm
																	// will not
																	// modify it
			Mat temp = KeyPointOperation.getMatPoint(stego, keyPoint);
			steganoAlgorithm.setStegoMessage(new MatImage(temp.clone()));
			
			byte[] embeddedData = steganoAlgorithm.getEmbeddedData(size);
			messages.add(embeddedData);
		}
		return messages.size() > 0 ? ArrayOperations.compact(messages) : new byte[0];
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

}
