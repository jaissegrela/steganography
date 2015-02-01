package core.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ListIterator;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SURF;

import core.utils.enumerations.KeyPointEnumeration;
import core.utils.enumerations.TopEnumeration;

public class KeyPointImagesAlgorithm {

	public static Mat drawKeypoints(Mat source, int keyPointSize, int quantity) throws IOException {
		KeyPointEnumeration keyPoints = new KeyPointEnumeration(source, keyPointSize);
		Enumeration<KeyPoint> enumeration = new TopEnumeration<KeyPoint>(keyPoints, quantity);
		Mat result = source.clone();
		Scalar[] colors = new Scalar[] { new Scalar(256, 0, 0, 0), new Scalar(128, 0, 0, 0), new Scalar(0, 256, 0, 0),
				new Scalar(0, 128, 0, 0), new Scalar(0, 0, 256, 0), new Scalar(0, 0, 128, 0),
				new Scalar(256, 256, 0, 0), new Scalar(0, 256, 256, 0), new Scalar(256, 0, 256, 0),
				new Scalar(256, 256, 256, 0) };
		int i = 0;
		while (enumeration.hasMoreElements()) {
			KeyPoint kp = enumeration.nextElement();
			opencv_features2d.drawKeypoints(result, kp, result, colors[i++ % colors.length],
					DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
		}
		return result;
	}

	public static Mat drawAllKeypoints(Mat source) throws IOException {
		ListIterator<KeyPoint> it = KeyPointOperation.getListOfKeyPoints(source, new SURF(1000, 4, 4, true, true)).listIterator();
		Mat result = new Mat(source);
		Scalar[] colors = new Scalar[] { new Scalar(256, 0, 0, 0), new Scalar(128, 0, 0, 0), new Scalar(0, 256, 0, 0),
				new Scalar(0, 128, 0, 0), new Scalar(0, 0, 256, 0), new Scalar(0, 0, 128, 0),
				new Scalar(256, 256, 0, 0), new Scalar(0, 256, 256, 0), new Scalar(256, 0, 256, 0),
				new Scalar(256, 256, 256, 0) };
		int i = 0;
		while (it.hasNext()) {
			KeyPoint kp = it.next();
			opencv_features2d.drawKeypoints(result, kp, result, colors[i++ % colors.length],
					DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
		}
		return result;
	}
}
