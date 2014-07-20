package core.attack;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import core.message.IMessage;

public class ChiSquare implements IStatisticsSteganalysis {

	/* (non-Javadoc)
	 * @see core.attack.ISteganalysis#calculate(core.message.IMessage)
	 */
	@Override
	public double calculate(IMessage message) {
		int[] count = countValues(message);

		double[] expected = new double[128];
		long[] observed = new long[128];

		for (int i = 0; i < expected.length; i++) {
			observed[i] = count[i << 1];
			expected[i] = (observed[i] + count[(i << 1) + 1]) / 2;
		}

		return new ChiSquareTest().chiSquareTest(expected, observed);
	}

	protected int[] countValues(IMessage message) {
		int[] result = new int[256];
		
		for (int i = 0; i < message.bytes(); i++) {
			byte value = message.getByte(i);
			result[value & 0xFF]++;
		}
		
		for (int i = 0; i < result.length; i++) {
			result[i] = Math.max(result[i], 1);
		}
		
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Chi-Square";
	}

}
