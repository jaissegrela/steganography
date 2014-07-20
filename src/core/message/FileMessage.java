package core.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import core.utils.Utils;

/**
 * @author Jaisse Grela Represents any file message
 */
public class FileMessage extends CacheMessage {

	/**
	 * @param file
	 *            the file message
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if image is null
	 */
	public FileMessage(File file) throws IOException {
		this(toArrayOfByte(new FileInputStream(file)), Utils.getExtension(file));
	}

	/**
	 * @param filename
	 *            the path of the file
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if image is null
	 */
	public FileMessage(String filename) throws IOException {
		this(new File(filename));
	}

	protected FileMessage(byte[] cache, String type) {
		super(cache, type);
	}

	protected static byte[] toArrayOfByte(InputStream in) throws IOException {
		byte[] data = new byte[in.available()];
		in.read(data);
		in.close();
		return data;
	}
}
