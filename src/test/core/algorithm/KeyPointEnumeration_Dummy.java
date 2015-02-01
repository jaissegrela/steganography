package test.core.algorithm;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

import core.utils.enumerations.IKeyPointEnumeration;


public class KeyPointEnumeration_Dummy implements IKeyPointEnumeration {
	
	private Enumeration<KeyPoint> enumeration;
	private List<KeyPoint> keyPoints;
	
	public KeyPointEnumeration_Dummy(List<KeyPoint> keyPoints){
		this.keyPoints = keyPoints;
		reset(null);
	}
	
	@Override
	public void reset(Mat source) {
		enumeration = Collections.enumeration(this.keyPoints);
	}
	
	@Override
	public boolean hasMoreElements() {
		return enumeration.hasMoreElements();
	}
	
	@Override
	public KeyPoint nextElement() {
		return enumeration.nextElement();
	}

}
