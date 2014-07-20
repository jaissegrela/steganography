package core.transform;

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
	public void transform(double[][] input, int levels){
		preconditions(levels);
		
		int length = input.length;
		for (int k = 0; k < levels; k++) {
			applyTransform(input, length);
			Arrays2d.transpose(input, length);
			applyTransform(input, length);
			Arrays2d.transpose(input, length);
			length >>= 1;
		}
	}

	/* (non-Javadoc)
	 * @see core.transform.Transform2d#inverse(double[][], int)
	 */
	@Override
	public void inverse(double[][] input, int levels){
		preconditions(levels);
		int length = input.length;
		length >>= (levels - 1); 
		for (int k = 0; k < levels; k++) {
			Arrays2d.transpose(input, length);
			applyInverse(input, length);			
			Arrays2d.transpose(input, length);			
			applyInverse(input, length);			
			length <<= 1;
		}
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
