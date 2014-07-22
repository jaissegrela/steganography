package core.utils.comparator;

import java.util.Comparator;

import org.opencv.features2d.KeyPoint;

public class KeyPointSizeComparator implements Comparator<KeyPoint> {

	@Override
	public int compare(KeyPoint keyPoint1, KeyPoint keyPoint2) {
		if(keyPoint1 == null || keyPoint2 == null)
			throw new IllegalArgumentException("KeyPointResponseComparator.compare");
		return keyPoint1.size < keyPoint2.size ? -1 : 
			keyPoint1.size > keyPoint2.size ? 1 : 0;
		
	}
}
