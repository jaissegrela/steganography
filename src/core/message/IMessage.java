package core.message;

/**
 * Represents a message to be hidden
 * 
 * @author Jaisse Grela
 */
public interface IMessage {

	/**
	 * Gets the quantity of byte of the message
	 * 
	 * @return the quantity
	 */
	public int bytes();

	/**
	 * Gets the specific byte
	 * 
	 * @param index
	 *            position of byte
	 * @return the specific byte
	 */
	public byte getByte(int index);

	/**
	 * Gets the type of message, generally it is a extension of the file message, it means, txt, jpg, pdf.
	 * 
	 * @return the type of message
	 */
	public String getType();
}
