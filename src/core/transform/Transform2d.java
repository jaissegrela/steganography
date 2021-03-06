package core.transform;

import org.opencv.core.Mat;

public interface Transform2d {

	public abstract void transform(Mat mat, int levels);

	public abstract void inverse(Mat mat, int levels);

}