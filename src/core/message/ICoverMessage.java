package core.message;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Represents a cover message
 * 
 * @author Jaisse Grela
 */
public interface ICoverMessage extends IMessage {

	/**
	 * Sets a byte on the specific position
	 * 
	 * @param index
	 *            the position
	 * @param value
	 *            the byte to set
	 */
	public abstract void setByte(int index, byte value);

	/**
	 * Gets a copy of this object
	 * 
	 * @return the new ICoverMessage
	 */
	public abstract ICoverMessage duplicateMessage();
	
	public abstract Mat getMat();

}