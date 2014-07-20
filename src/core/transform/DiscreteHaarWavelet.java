package core.transform;

public class DiscreteHaarWavelet implements Transform {

	@Override
	public void transform(double[] input, int length) {
		double[] output = new double[length];
	    length >>= 1;
       
		for (int i = 0; i < length; i++) {
            int position = i << 1;
            output[i] = input[position] + input[position + 1];
            output[length + i] = input[position] - input[position + 1];
        }
	    
		System.arraycopy(output, 0, input, 0, length << 1);
	}

	@Override
	public void inverse(double[] input, int length) {
		double[] output = new double[length];
	    length >>= 1;
       
		for (int i = 0; i < length; i++) {
            int position = i << 1;
			output[position + 1] = (input[i] - input[length + i]) / 2;
            output[position] = input[i] - output[position + 1];
        }
	    
		System.arraycopy(output, 0, input, 0, length << 1);
	}

}
