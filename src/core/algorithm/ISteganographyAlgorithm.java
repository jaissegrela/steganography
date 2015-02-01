package core.algorithm;

import core.message.ICoverMessage;
import core.message.IMessage;

/**
 * Represents a steganography algorithm, algorithm to hide data inside a
 * medium
 * 
 * @author Jaisse Grela
 */
public interface ISteganographyAlgorithm {

	void setStegoMessage(ICoverMessage stegoMessage);
	
	void setCoverMessage(ICoverMessage coverMessage);	
	
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
	 * Gets the hidden message
	 * 
	 * @return the hidden message
	 * @param size the size in bits of the embedded message
	 * @throws IllegalStateException
	 *             - if no hidden data
	 */
	byte[] getEmbeddedData(int size);
	
	

}