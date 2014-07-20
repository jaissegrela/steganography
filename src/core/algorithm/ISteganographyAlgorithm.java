package core.algorithm;

import core.message.ICoverMessage;
import core.message.IMessage;

/**
 * Represents a steganography algorithm, algorithm to hide/extract data inside a
 * medium
 * 
 * @author Jaisse Grela
 */
public interface ISteganographyAlgorithm {

	/**
	 * Gets the rate of the space that will be occupied for a message inside the
	 * cover message, this rate is non-negative and if it was greater than 1 the
	 * message can not be hidden inside this cover message
	 * 
	 * @param embeddedData
	 *            data to hide
	 * @return the message space rate
	 */
	double getStegoObjectRate(IMessage embeddedData);
	
	/**
	 * Gets the stego-object results from applying this algorithm to the cover
	 * message and the embedded data
	 * 
	 * @param embeddedData
	 *            data to hide
	 * @return the stego-object
	 */
	ICoverMessage getStegoObject(IMessage embeddedData);

	/**
	 * Gets if the medium has a hidden message
	 * 
	 * @return true if the medium contain a message, false otherwise
	 */
	boolean hasHiddenMessage();
	
	/**
	 * Gets the type hidden message
	 * 
	 * @return type hidden message
	 * @throws IllegalStateException
	 *             - if no hidden data
	 */
	byte[] getTypeMessage();

	/**
	 * Gets the hidden message
	 * 
	 * @return the hidden message
	 * @throws IllegalStateException
	 *             - if no hidden data
	 */
	byte[] getEmbeddedData();
	
	public ICoverMessage getCoverMessage();

	void setCoverMessage(ICoverMessage coverMessage);
	
	int getMaxSizeMessageToHide();

}