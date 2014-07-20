package core.transform;


public class FastDiscreteBiorthogonal_CDF_9_7 implements Transform{

	/**
	 * dwt97.c - Fast discrete biorthogonal CDF 9/7 wavelet forward and inverse
	 * transform (lifting implementation)
	 * 
	 * This code is provided "as is" and is given for educational purposes. 2006
	 * - Gregoire Pau - gregoire.pau@ebi.ac.uk
	 */

	/**
	 * fwt97 - Forward biorthogonal 9/7 wavelet transform (lifting
	 * implementation)
	 * 
	 * x is an input signal, which will be replaced by its output transform. n
	 * is the length of the signal, and must be a power of 2.
	 * 
	 * The first half part of the output signal contains the approximation
	 * coefficients. The second half part contains the detail coefficients (aka.
	 * the wavelets coefficients).
	 * 
	 * See also iwt97.
	 */
	public void transform(double[] x, int n) {
		// Predict 1
		double a = -1.586134342;
		update(x, n, a);

		// Update 1
		a = -0.05298011854;
		update1(x, n, a);

		// Predict 2
		a = 0.8829110762;
		update(x, n, a);

		// Update 2
		a = 0.4435068522;
		update1(x, n, a);

		// Scale
		a = 1 / 1.149604398;
		for (int i = 0; i < n; i++) {
			if (i % 2 != 0)
				x[i] *= a;
			else
				x[i] /= a;
		}

		// Pack
		double[] tempbank = new double[x.length];
		for (int i = 0; i < n; i++) {
			if (i % 2 == 0)
				tempbank[i / 2] = x[i];
			else
				tempbank[x.length / 2 + i / 2] = x[i];
		}
		for (int i = 0; i < x.length; i++)
			x[i] = tempbank[i];
	}

	private void update1(double[] x, int n, double factor) {
		for (int i = 2; i < n; i += 2) {
			x[i] += factor * (x[i - 1] + x[i + 1]);
		}
		x[0] += 2 * factor * x[1];
	}

	private void update(double[] x, int n, double factor) {
		int i;
		for (i = 1; i < n - 2; i += 2) {
			x[i] += factor * (x[i - 1] + x[i + 1]);
		}
		x[n - 1] += 2 * factor * x[n - 2];
	}

	/**
	 * iwt97 - Inverse biorthogonal 9/7 wavelet transform
	 * 
	 * This is the inverse of fwt97 so that iwt97(fwt97(x,n),n)=x for every
	 * signal x of length n.
	 * 
	 * See also fwt97.
	 */
	public void inverse(double[] x, int n) {

		// Unpack
		double[] tempbank = new double[n];
		for (int i = 0; i < n / 2; i++) {
			tempbank[i * 2] = x[i];
			tempbank[i * 2 + 1] = x[i + n / 2];
		}
		for (int i = 0; i < n; i++)
			x[i] = tempbank[i];

		// Undo scale
		double a = 1.149604398;
		for (int i = 0; i < n; i++) {
			if (i % 2 != 0)
				x[i] *= a;
			else
				x[i] /= a;
		}

		// Undo update 2
		a = -0.4435068522;
		update1(x, n, a);

		// Undo predict 2
		a = -0.8829110762;
		update(x, n, a);

		// Undo update 1
		a = 0.05298011854;
		update1(x, n, a);

		// Undo predict 1
		a = 1.586134342;
		update(x, n, a);
	}

}
