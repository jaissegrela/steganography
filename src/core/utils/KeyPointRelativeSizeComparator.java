package core.utils;

import java.util.Comparator;

import org.opencv.features2d.KeyPoint;

public class KeyPointRelativeSizeComparator implements Comparator<KeyPoint> {
	
	private float size;

	public KeyPointRelativeSizeComparator(float size){
		this.size = size;
	}

	@Override
	public int compare(KeyPoint keyPoint1, KeyPoint keyPoint2) {
		if(keyPoint1 == null || keyPoint2 == null)
			throw new IllegalArgumentException("KeyPointResponseComparator.compare");
		float keyPoint1Size = Math.abs(keyPoint1.size - this.size);
		float keyPoint2Size = Math.abs(keyPoint2.size - this.size);
		return keyPoint1Size < keyPoint2Size ? -1 : 
			keyPoint1Size > keyPoint2Size ? 1 : 0;
		
	}
}
