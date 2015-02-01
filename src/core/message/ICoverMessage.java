package core.message;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Represents a cover message
 * 
 * @author Jaisse Grela
 */
public interface ICoverMessage {

	/**
	 * Gets a copy of this object
	 * 
	 * @return the new ICoverMessage
	 */
	public abstract ICoverMessage duplicateMessage();
	
	/**
	 * Gets the matrix of values representing the message
	 * 
	 * @return the matrix
	 */
	public abstract Mat getMat();

}