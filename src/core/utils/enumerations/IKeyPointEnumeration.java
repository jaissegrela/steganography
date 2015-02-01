package core.utils.enumerations;

import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

public interface IKeyPointEnumeration extends Enumeration<KeyPoint>{

	public abstract void reset(Mat source);

}