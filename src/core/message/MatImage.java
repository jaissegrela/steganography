package core.message;

import java.io.IOException;
import java.io.OutputStream;

import org.opencv.core.Mat;

public class MatImage extends CacheMessage implements ICoverMessage {
	
	private Mat mat;
	private boolean modified;

	public MatImage(Mat mat){
		super(getBytesOfMat(mat));
		this.mat = mat;
	}

	private static byte[] getBytesOfMat(Mat mat) {
		byte[] pixels = new byte[(int)mat.size().area() * mat.channels()];
		mat.get(0, 0, pixels);
		return pixels;
	}

	@Override
	public void setByte(int index, byte value) {
		modified = true;
		cache[index] = value;
	}

	@Override
	public ICoverMessage duplicateMessage() {
		Mat dest;
		dest = new Mat(mat.size(), mat.type());
		if(modified){
			dest.put(0, 0, cache);
		}else{
			mat.copyTo(dest);
		}
		return new MatImage(dest);
	}

	@Override
	public void save(OutputStream stream) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mat getMat(int flags) {
		return mat;
	}

}
