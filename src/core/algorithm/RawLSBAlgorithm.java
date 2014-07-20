package core.algorithm;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.utils.ByteInfo;

/**
 * Hides a message using the LSB strategy. This class does not insert additional
 * information. When extracting the hidden data, it assumes that this data
 * covers the whole message and not part of it.
 * 
 * @author Jaisse Grela
 */
public class RawLSBAlgorithm implements ISteganographyAlgorithm {

	protected ICoverMessage coverMessage;

	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public RawLSBAlgorithm(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.ISteganographyAlgorithm#getStegoObjectRate(int)
	 */
	@Override
	public double getStegoObjectRate(IMessage embeddedData) {
		return ((double) embeddedData.bytes()) * ByteInfo.BYTE_SIZE
				/ coverMessage.bytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.ISteganographyAlgorithm#getStegoObject(byte[])
	 */
	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		byte[] rawStegoObject = getRawStegoObject(embeddedData, 0);
		ICoverMessage result = coverMessage.duplicateMessage();
		for (int i = 0; i < rawStegoObject.length; i++) {
			result.setByte(i, rawStegoObject[i]);
		}
		return result;
	}

	/**
	 * Hides the data inside the cover medium from an initial position, it can
	 * be useful for other strategies
	 * 
	 * @param embeddedData
	 *            data to hide
	 * @param start
	 *            initial position to hide the message
	 * @return the stego-object
	 */
	protected byte[] getRawStegoObject(IMessage embeddedData, int start) {
		if (coverMessage.bytes() - start < embeddedData.bytes()
				* ByteInfo.BYTE_SIZE) {
			throw new IllegalArgumentException();
		}
		byte[] result = new byte[embeddedData.bytes() * ByteInfo.BYTE_SIZE];
		int image_index = 0;
		for (int i = 0; i < embeddedData.bytes(); i++) {
			byte data = embeddedData.getByte(i);
			for (int j = 0; j < ByteInfo.BYTE_SIZE; j++) {
				byte image = coverMessage.getByte(start + image_index);
				boolean isActive = ByteInfo.isActive(data, j);
				image = ByteInfo.modifyLSB(image, isActive);
				result[image_index] = image;
				image_index++;
			}
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.ISteganographyAlgorithm#hasHiddenMessage()
	 */
	@Override
	public boolean hasHiddenMessage() {
		return coverMessage.bytes() > 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.ISteganographyAlgorithm#getEmbeddedData()
	 */
	@Override
	public byte[] getEmbeddedData() {
		return getEmbeddedData(0, coverMessage.bytes());
	}

	/**
	 * Extract the embeddedData from a initial position
	 * 
	 * @param start
	 *            initial position to extract the message
	 * @param lenght
	 *            length of the message
	 * @return the hidden message
	 */
	protected byte[] getEmbeddedData(int start, long length) {
		byte[] result = new byte[(int) Math.ceil(((double) length)
				/ ByteInfo.BYTE_SIZE)];
		int result_index = 0;
		byte temp = 0;
		int move = 0;
		for (int i = 0; i < length; i++) {
			byte byte1 = coverMessage.getByte(start + i);
			byte lsb = ByteInfo.getLSB(byte1);
			temp |= (lsb << move);
			move++;
			if (move >= ByteInfo.BYTE_SIZE) {
				result[result_index] = temp;
				result_index++;
				move = 0;
				temp = 0;
			}
		}
		if (result_index < result.length)
			result[result_index] = temp;
		return result;
	}

	@Override
	public byte[] getTypeMessage() {
		return new byte[0];
	}
	
	public ICoverMessage getCoverMessage() {
		return coverMessage;
	}

	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	@Override
	public int getMaxSizeMessageToHide() {
		return coverMessage.bytes() / ByteInfo.BYTE_SIZE;
	}
}