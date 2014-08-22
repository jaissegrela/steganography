package core.utils.enumerations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;

import core.utils.KeyPointOperation;
import core.utils.comparator.KeyPointRelativeSizeComparator;

public class KeyPointEnumeration implements Enumeration<KeyPoint> {
	
	private List<KeyPoint> keyPoints;

	public KeyPointEnumeration(Mat source, float keyPointSize){
		this(source, keyPointSize, FeatureDetector.SURF);
	}
	
	public KeyPointEnumeration(Mat source, float keyPointSize, int featureDetector){
		keyPoints = getKeyPoints(source, featureDetector);
	    Collections.sort(keyPoints, new KeyPointRelativeSizeComparator(keyPointSize));
	    //Collections.sort(keyPoints, new KeyPointResponseComparator());
	    setSize(keyPoints, keyPointSize);
	}
	
	public List<KeyPoint> getKeyPoints(Mat source, int detectorType){
	    MatOfKeyPoint matOfKeyPoints = new MatOfKeyPoint();
	    FeatureDetector blobDetector = FeatureDetector.create(detectorType);
	    blobDetector.detect(source, matOfKeyPoints);
	    return matOfKeyPoints.toList();	
	}
	
	public void setSize(List<KeyPoint> keyPoints, float n) {
		for (KeyPoint keyPoint : keyPoints) {
			keyPoint.size = n;
		}
	}

	@Override
	public boolean hasMoreElements() {
		return !keyPoints.isEmpty();
	}

	@Override
	public KeyPoint nextElement() {
		if(!hasMoreElements())
			throw new java.util.NoSuchElementException();
		ArrayList<KeyPoint> temp = new ArrayList<KeyPoint>(keyPoints.size() - 1);
		KeyPoint result = keyPoints.get(0);
		for (int j = 1; j < keyPoints.size(); j++) {
			if(!KeyPointOperation.hasManhattanInstersection(result, keyPoints.get(j)))
				temp.add(keyPoints.get(j));
		}
		keyPoints = temp;
		return result;
	}	

}
