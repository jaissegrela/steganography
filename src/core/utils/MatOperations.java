package core.utils;

import java.nio.DoubleBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;

public class MatOperations {

	private Mat mat;
	private DoubleBuffer buffer;
	
	public MatOperations(Mat mat){
		setMat(mat);
	}
	
	public double[] getPixel(int row, int col){
		double[] pixel = createPixel();
		position(row, col);
		buffer.get(pixel);
		return pixel;
	}
	
	public void setPixel(int row, int col, double[] pixel){
		position(row, col);
		buffer.put(pixel);
	}
	
	public double[] createPixel(){
		return new double[mat.channels()];
	}
	
	public void start(){
		buffer.position();
	}
	
	public void position(int row, int col){
		buffer.position((row * mat.cols() + col) * mat.channels());
	}

	public Mat getMat() {
		return mat;
	}

	public void setMat(Mat mat) {
		this.mat = mat;
		buffer = this.mat.getDoubleBuffer();
	}

}
