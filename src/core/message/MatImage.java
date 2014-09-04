package core.message;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

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
	public void save(OutputStream stream) throws IOException {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode("." + extension, this.mat, matOfByte);
		byte[] b = matOfByte.toArray();
		stream.write(b);
		stream.close();
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
