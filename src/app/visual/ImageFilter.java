package app.visual;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import core.utils.Utils;

/**
 * @author Jaisse Grela
 * 
 */
public class ImageFilter extends FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.bmp) || extension.equals(Utils.tiff)
					|| extension.equals(Utils.tif)
					|| extension.equals(Utils.gif) ||
					// extension.equals(Utils.jpeg) ||
					extension.equals(Utils.jpg) || extension.equals(Utils.png)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Just Images";
	}

}
