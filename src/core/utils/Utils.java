package core.utils;

import java.io.File;

public class Utils {

	// public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";
	public final static String bmp = "bmp";

	/*
	 * Get the extension of a file.
	 */
	/**
	 * Gets the extension of a file.
	 * 
	 * @param file
	 *            the file
	 * @return the extension
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}
	
	public static String getExtension(String filename) {
		if(filename == null)
			return null;
		
		String ext = null;
		int i = filename.lastIndexOf('.');

		if (i > 0 && i < filename.length() - 1) {
			ext = filename.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
