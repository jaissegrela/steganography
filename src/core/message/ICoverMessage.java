package core.message;

import java.io.IOException;
import java.io.OutputStream;

import org.opencv.core.Mat;

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

	/**
	 * Save the message
	 * 
	 * @param stream
	 *            the stream
	 * @throws IOException if an error occurs during writing
	 * @throws IllegalArgumentException
	 *             if filename is null
	 */
	public abstract void save(OutputStream stream) throws IOException;
	
	public abstract Mat getMat();

}