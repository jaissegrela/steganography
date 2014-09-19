package core.algorithm;

import core.message.ICoverMessage;

/**
 * Represents a steganography algorithm, algorithm to hide/extract data inside a
 * medium
 * 
 * @author Jaisse Grela
 */
public interface ISteganographyMemoryAlgorithm extends ISteganographyAlgorithm{
	
	void setPrimeCoverMessage(ICoverMessage primeCoverMessage);

}