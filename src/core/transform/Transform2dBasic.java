package core.transform;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import core.utils.Arrays2d;

public class Transform2dBasic implements Transform2d {
	
	protected Transform alg;

	public Transform2dBasic(Transform tranform){
		alg = tranform;
	}

	/* (non-Javadoc)
	 * @see core.transform.Transform2d#transform(double[][], int)
	 */
	@Override
	public void transform(Mat mat, int levels){
		preconditions(levels);
		
		List<Mat> list = new ArrayList<Mat>();
		
		Core.split(mat, list);
	    
	    for (int i = 0; i < list.size(); i++) {
			double[][] source = Arrays2d.getSource(list.get(i));
			int length = source.length;
			for (int k = 0; k < levels; k++) {
				applyTransform(source, length);
				Arrays2d.transpose(source, length);
				applyTransform(source, length);
				Arrays2d.transpose(source, length);
				length >>= 1;
			}
			Arrays2d.putSource(list.get(i), source, 0, 0);
	    }
	    
	    Core.merge(list, mat);
	}

	/* (non-Javadoc)
	 * @see core.transform.Transform2d#inverse(double[][], int)
	 */
	@Override
	public void inverse(Mat mat, int levels){
		preconditions(levels);
		
		List<Mat> list = new ArrayList<Mat>();
		
		Core.split(mat, list);
	    
	    for (int i = 0; i < list.size(); i++) {
			double[][] source = Arrays2d.getSource(list.get(i));
		
			int length = source.length;
			length >>= (levels - 1); 
			for (int k = 0; k < levels; k++) {
				Arrays2d.transpose(source, length);
				applyInverse(source, length);			
				Arrays2d.transpose(source, length);			
				applyInverse(source, length);			
				length <<= 1;
			}
	
			Arrays2d.putSource(list.get(i), source, 0, 0);
	    }
	    
	    Core.merge(list, mat);
	}
	
	protected void preconditions(int levels) {
		if(levels < 1)
			throw new IllegalArgumentException("The level parameter must be greater than 0");
	}

	protected void applyTransform(double[][] input, int length) {
		for (int i = 0; i < input.length; i++) {
			applyTransform(input[i], length);
		}
	}

	protected void applyInverse(double[][] input, int length) {
		for (int i = 0; i < input.length; i++) {
			applyInverse(input[i], length);
		}
	}
	
	protected void applyTransform(double[] input, int length) {
		alg.transform(input, length);
	}

	protected void applyInverse(double[] input, int length) {
		alg.inverse(input, length);
	}
}
