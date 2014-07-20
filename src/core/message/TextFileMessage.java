package core.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import core.utils.Utils;

/**
 * @author Jaisse Grela Represents a text file message
 * 
 */
public class TextFileMessage extends CacheMessage {

	/**
	 * @param file
	 *            the text file
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if file is null
	 */
	public TextFileMessage(File file) throws IOException {
		this(toArrayOfByte(file), Utils.getExtension(file));
	}

	/**
	 * @param filename
	 *            the path of the text file
	 * @throws IOException
	 *             if an error occurs during reading
	 * @throws IllegalArgumentException
	 *             if filename is null
	 */
	public TextFileMessage(String filename) throws IOException {
		this(new File(filename));
	}

	protected TextFileMessage(byte[] cache, String type) {
		super(cache, type);
	}

	protected static byte[] toArrayOfByte(File file) throws IOException {
		FileInputStream inputStream = new FileInputStream(file);
		String everything = IOUtils.toString(inputStream);
		return everything.getBytes();
	}

}
