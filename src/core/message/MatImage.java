package core.message;

import org.bytedeco.javacpp.opencv_core.Mat;

import core.utils.enumerations.BitEnumeration;

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
	public void setByte(int index, byte value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ICoverMessage duplicateMessage() {
		return new MatImage(mat.clone(), extension);
	}

	@Override
	public Mat getMat() {
		return mat;
	}

	@Override
	public int bytes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getByte(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BitEnumeration getEnumeration() {
		throw new UnsupportedOperationException();
	}

}
