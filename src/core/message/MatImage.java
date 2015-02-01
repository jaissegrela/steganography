package core.message;

import org.bytedeco.javacpp.opencv_core.Mat;

public class MatImage implements ICoverMessage {
	
	protected Mat mat;
	protected String extension;

	public MatImage(Mat mat){
		this.mat = mat;
	}
	
	public MatImage(Mat mat, String extension){
		this.mat = mat;
		this.extension = extension;
	}

	@Override
	public ICoverMessage duplicateMessage() {
		return new MatImage(mat.clone(), extension);
	}

	@Override
	public Mat getMat() {
		return mat;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
