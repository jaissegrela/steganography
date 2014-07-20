package core.utils;

/**
 * Operations over a byte
 * 
 * @author Jaisse Grela
 */
public class ByteInfo {

	public static final int BYTE_SIZE = 8;
	private static final byte ACTIVE = 1;
	private static final byte INACTIVE = ~ACTIVE;

	/**
	 * Gets a byte as a result to activating/deactivating the lower significant
	 * bit of a byte
	 * 
	 * @param data
	 *            a base byte
	 * @param toActive
	 *            true for active the lower significant bit, false for
	 *            deactivate the lower significant bit
	 * @return the new byte
	 */
	public static byte modifyLSB(byte data, boolean toActive) {
		return (byte) (toActive ? data | ACTIVE : data & INACTIVE);
	}
	
	public static byte activeByte(byte data, int position) {
		return (byte) ((byte) data | (ACTIVE << position));
	}

	/**
	 * Gets if the specific position of the byte is active
	 * 
	 * @param data
	 *            the byte
	 * @param bit_position
	 *            position of the bit to be consulting
	 * @return true if the bit is active, false otherwise
	 */
	public static boolean isActive(byte data, int bit_position) {
		int active = ACTIVE << bit_position;
		return (data & active) == active;
	}

	/**
	 * Gets new byte with the lower significant bit of byte parameter and other
	 * bits are setting to 0
	 * 
	 * @param data
	 *            the byte to be consulting
	 * @return new byte with the lower significant bit
	 */
	public static byte getLSB(byte data) {
		return (byte) (data & ACTIVE);
	}
}