package core.utils.enumerations;

import java.util.Collections;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SURF;

import core.utils.KeyPointOperation;
import core.utils.comparator.KeyPointResponseComparator;

public class KeyPointEnumeration implements IKeyPointEnumeration {

	protected List<KeyPoint> keyPoints;
	private int keyPointSize;

	public KeyPointEnumeration() {
		this(null);
	}

	public KeyPointEnumeration(Mat source) {
		this(source, -1);
	}

	public KeyPointEnumeration(int keyPointSize) {
		this(null, keyPointSize);
	}

	public KeyPointEnumeration(Mat source, int keyPointSize) {
		this.keyPointSize = keyPointSize;
		reset(source);
	}

	/* (non-Javadoc)
	 * @see core.utils.enumerations.IKeyPointEnumeration#reset(org.bytedeco.javacpp.opencv_core.Mat)
	 */
	public void reset(Mat source) {
		if (source == null) {
			keyPoints = Collections.emptyList();
		} else {
			keyPoints = KeyPointOperation.getListOfKeyPoints(source, new SURF(100, 4, 4, true, true));
			Collections.sort(keyPoints, new KeyPointResponseComparator());
			if (keyPointSize > 0) {
				removeInvalidPoints(keyPoints, keyPointSize, source.cols(), source.rows());
				setSize(keyPoints, keyPointSize);
			}
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
		KeyPoint result = keyPoints.remove(0);
		for (int j = keyPoints.size() - 1; j >= 0; j--) {
			if (KeyPointOperation.hasManhattanInstersection(result, keyPoints.get(j)))
				keyPoints.remove(j);
		}
		return result;
	}

}
