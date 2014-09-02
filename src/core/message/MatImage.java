package core.message;

import java.io.IOException;
import java.io.OutputStream;

import org.opencv.core.Mat;

public class MatImage implements ICoverMessage {
	
	private Mat mat;

	public MatImage(Mat mat){
		this.mat = mat;
	}


	@Override
	public void setByte(int index, byte value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ICoverMessage duplicateMessage() {
		return new MatImage(mat.clone());
	}

	@Override
	public void save(OutputStream stream) throws IOException {
		throw new UnsupportedOperationException();
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

}
