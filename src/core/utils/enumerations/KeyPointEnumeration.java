package core.utils.enumerations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SURF;

import core.utils.KeyPointOperation;
import core.utils.comparator.KeyPointResponseComparator;

public class KeyPointEnumeration implements Enumeration<KeyPoint> {

	private List<KeyPoint> keyPoints;

	public KeyPointEnumeration(Mat source) {
		this(source, -1);
	}

	public KeyPointEnumeration(Mat source, float keyPointSize) {
		keyPoints = KeyPointOperation.getListOfKeyPoints(source, new SURF(100, 4, 4, true, true));
		if (keyPointSize > 0) {
			removeInvalidPoints(keyPoints, keyPointSize, source.cols(), source.rows());
			Collections.sort(keyPoints, new KeyPointResponseComparator());
			setSize(keyPoints, keyPointSize);
		}
	}

	protected void removeInvalidPoints(List<KeyPoint> keyPoints, float keyPointSize, int witdh, int height) {
		for (int i = keyPoints.size() - 1; i >= 0; i--) {
			KeyPoint keyPoint = keyPoints.get(i);
			if (!isValid(keyPoint.pt(), keyPointSize, witdh, height))
				keyPoints.remove(i);
		}
	}

	protected boolean isValid(Point2f point, float keyPointSize, int witdh, int height) {
		return point.x() >= keyPointSize && point.y() >= keyPointSize && point.x() <= witdh - keyPointSize
				&& point.y() <= height - keyPointSize;
	}

	public void setSize(List<KeyPoint> keyPoints, float n) {
		for (KeyPoint keyPoint : keyPoints) {
			keyPoint.size(n);
		}
	}

	@Override
	public boolean hasMoreElements() {
		return !keyPoints.isEmpty();
	}

	@Override
	public KeyPoint nextElement() {
		if (!hasMoreElements())
			throw new java.util.NoSuchElementException();
		ArrayList<KeyPoint> temp = new ArrayList<KeyPoint>(keyPoints.size() - 1);
		KeyPoint result = keyPoints.get(0);
		for (int j = 1; j < keyPoints.size(); j++) {
			if (!KeyPointOperation.hasManhattanInstersection(result, keyPoints.get(j)))
				temp.add(keyPoints.get(j));
		}
		keyPoints = temp;
		return result;
	}

}
