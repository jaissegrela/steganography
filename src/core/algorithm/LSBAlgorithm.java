package core.algorithm;

import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.IMessage;
import core.utils.ByteInfo;
import core.utils.Converter;

/**
 * Hides a message using the LSB strategy. This algorithm insert a header with
 * additional information, useful for extracting this data later.
 * 
 * @author Jaisse Grela
 */
public class LSBAlgorithm extends RawLSBAlgorithm {

	private static final int INT_SIZE = 32;
	private static final int INT_BYTE_RATIO = 4;
	private static final int TYPE_LENGHT = 3;

	public LSBAlgorithm(ICoverMessage cover_message) {
		super(cover_message);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.RawLSBAlgorithm#getStegoObjectRate(int)
	 */
	@Override
	public double getStegoObjectRate(IMessage embeddedData) {
		return ((double) embeddedData.bytes() + INT_BYTE_RATIO + TYPE_LENGHT)
				* ByteInfo.BYTE_SIZE / coverMessage.bytes();
	}
	
	@Override
	public int getMaxSizeMessageToHide() {
		return coverMessage.bytes() / ByteInfo.BYTE_SIZE - INT_BYTE_RATIO - TYPE_LENGHT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.RawLSBAlgorithm#getStegoObject(byte[])
	 */
	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		if (coverMessage.bytes() < ((embeddedData.bytes() + INT_BYTE_RATIO) * ByteInfo.BYTE_SIZE)) {
			throw new IllegalArgumentException();
		}

		int size = embeddedData.bytes();
		
		byte[] bsize = Converter.toArrayOfByte(size);
		byte[] type = normalize(embeddedData.getType());
		
		bsize = getRawStegoObject(new CacheMessage(bsize), 0);
		type = getRawStegoObject(new CacheMessage(type), bsize.length);
		
		byte[] rawStegoObject = getRawStegoObject(embeddedData, bsize.length + type.length);
		
		ICoverMessage result = coverMessage.duplicateMessage();
		
		hideInformation(bsize, result, 0);
		hideInformation(type, result, bsize.length);
		hideInformation(rawStegoObject, result, bsize.length + type.length );
		
		return result;
	}

	private void hideInformation(byte[] message, ICoverMessage coverMessage, int beginIndex) {
		for (int i = 0; i < message.length; i++) {
			coverMessage.setByte(beginIndex++, message[i]);
		}
	}
	
	protected byte[] normalize(String type) {
		if(type == null)
			type = "";
		type += "   ";
		return type.substring(0, 3).getBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.RawLSBAlgorithm#hasHiddenMessage()
	 */
	@Override
	public boolean hasHiddenMessage() {
		if (!super.hasHiddenMessage()) {
			return false;
		}
		int bytes = INT_BYTE_RATIO * ByteInfo.BYTE_SIZE;
		byte[] size = getEmbeddedData(0, bytes);
		int length = Converter.toInt(size);
		int cover_message_length = coverMessage.bytes();
		return length > 0
				&& cover_message_length >= ((length - TYPE_LENGHT) * ByteInfo.BYTE_SIZE - INT_SIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.RawLSBAlgorithm#getEmbeddedData()
	 */
	@Override
	public byte[] getEmbeddedData() {
		if (!hasHiddenMessage()){
			throw new IllegalStateException("There is no hidden message");
		}
		
		int bytes = INT_BYTE_RATIO * ByteInfo.BYTE_SIZE;
		byte[] size = getEmbeddedData(0, bytes);
		
		int length = Converter.toInt(size);
		return getEmbeddedData(LSBAlgorithm.INT_SIZE + LSBAlgorithm.TYPE_LENGHT * ByteInfo.BYTE_SIZE, length
				* ByteInfo.BYTE_SIZE);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see core.algorithm.RawLSBAlgorithm#getEmbeddedData()
	 */
	@Override
	public byte[] getTypeMessage() {
		if (!hasHiddenMessage()){
			throw new IllegalStateException("There is no hidden message");
		}
		
		return getEmbeddedData(LSBAlgorithm.INT_SIZE, LSBAlgorithm.TYPE_LENGHT
				* ByteInfo.BYTE_SIZE);
	}

}
