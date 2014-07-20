package app.old;

import java.io.IOException;

import math.jwave.exceptions.JWaveFailure;
import math.jwave.transforms.BasicTransform;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.WaveletPacketTransform;
import math.jwave.transforms.wavelets.Haar1;
import math.jwave.transforms.wavelets.Haar1Orthogonal;
import math.jwave.transforms.wavelets.biorthogonal.*;
import math.jwave.transforms.wavelets.coiflet.*;
import math.jwave.transforms.wavelets.daubechies.*;
import math.jwave.transforms.wavelets.legendre.*;
import math.jwave.transforms.wavelets.symlets.*;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class JWave_Test {

	public static void main(String[] args) throws IOException, JWaveFailure {
		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
	    BasicTransform[] transform = new BasicTransform[]{
	    		new FastWaveletTransform( new BiOrthogonal11( )),
	    		new FastWaveletTransform( new BiOrthogonal13( )),
	    		new FastWaveletTransform( new BiOrthogonal15( )),
	    		new FastWaveletTransform( new BiOrthogonal22( )),
	    		new FastWaveletTransform( new BiOrthogonal24( )),
	    		new FastWaveletTransform( new BiOrthogonal26( )),
	    		new FastWaveletTransform( new BiOrthogonal28( )),
	    		new FastWaveletTransform( new BiOrthogonal31( )),
	    		new FastWaveletTransform( new BiOrthogonal35( )),
	    		new FastWaveletTransform( new BiOrthogonal37( )),
	    		new FastWaveletTransform( new BiOrthogonal39( )),
	    		new FastWaveletTransform( new BiOrthogonal44( )),
	    		new FastWaveletTransform( new BiOrthogonal55( )),
	    		new FastWaveletTransform( new BiOrthogonal68( )),
	    		new FastWaveletTransform( new Coiflet1( )),
	    		new FastWaveletTransform( new Coiflet2( )),
	    		new FastWaveletTransform( new Coiflet3( )),
	    		new FastWaveletTransform( new Coiflet4( )),
	    		new FastWaveletTransform( new Coiflet5( )),
	    		new FastWaveletTransform( new Daubechies10( )),
	    		new FastWaveletTransform( new Daubechies2( )),
	    		new FastWaveletTransform( new Daubechies20( )),
	    		new FastWaveletTransform( new Daubechies3( )),
	    		new FastWaveletTransform( new Daubechies4( )),
	    		new FastWaveletTransform( new Daubechies5( )),
	    		new FastWaveletTransform( new Daubechies6( )),
	    		new FastWaveletTransform( new Daubechies7( )),
	    		new FastWaveletTransform( new Daubechies8( )),
	    		new FastWaveletTransform( new Legendre1( )),
	    		new FastWaveletTransform( new Legendre2( )),
	    		new FastWaveletTransform( new Legendre3( )),
	    		new FastWaveletTransform( new Symlets2( )),
	    		new FastWaveletTransform( new Symlets20( )),
	    		new FastWaveletTransform( new Haar1( )),
	    		new FastWaveletTransform( new Haar1Orthogonal( )),
	    		
	    		new WaveletPacketTransform( new BiOrthogonal11( )),
	    		new WaveletPacketTransform( new BiOrthogonal13( )),
	    		new WaveletPacketTransform( new BiOrthogonal15( )),
	    		new WaveletPacketTransform( new BiOrthogonal22( )),
	    		new WaveletPacketTransform( new BiOrthogonal24( )),
	    		new WaveletPacketTransform( new BiOrthogonal26( )),
	    		new WaveletPacketTransform( new BiOrthogonal28( )),
	    		new WaveletPacketTransform( new BiOrthogonal31( )),
	    		new WaveletPacketTransform( new BiOrthogonal35( )),
	    		new WaveletPacketTransform( new BiOrthogonal37( )),
	    		new WaveletPacketTransform( new BiOrthogonal39( )),
	    		new WaveletPacketTransform( new BiOrthogonal44( )),
	    		new WaveletPacketTransform( new BiOrthogonal55( )),
	    		new WaveletPacketTransform( new BiOrthogonal68( )),
	    		new WaveletPacketTransform( new Coiflet1( )),
	    		new WaveletPacketTransform( new Coiflet2( )),
	    		new WaveletPacketTransform( new Coiflet3( )),
	    		new WaveletPacketTransform( new Coiflet4( )),
	    		new WaveletPacketTransform( new Coiflet5( )),
	    		new WaveletPacketTransform( new Daubechies10( )),
	    		new WaveletPacketTransform( new Daubechies2( )),
	    		new WaveletPacketTransform( new Daubechies20( )),
	    		new WaveletPacketTransform( new Daubechies3( )),
	    		new WaveletPacketTransform( new Daubechies4( )),
	    		new WaveletPacketTransform( new Daubechies5( )),
	    		new WaveletPacketTransform( new Daubechies6( )),
	    		new WaveletPacketTransform( new Daubechies7( )),
	    		new WaveletPacketTransform( new Daubechies8( )),
	    		new WaveletPacketTransform( new Legendre1( )),
	    		new WaveletPacketTransform( new Legendre2( )),
	    		new WaveletPacketTransform( new Legendre3( )),
	    		new WaveletPacketTransform( new Symlets2( )),
	    		new WaveletPacketTransform( new Symlets20( )),
	    		new WaveletPacketTransform( new Haar1( )),
	    		new WaveletPacketTransform( new Haar1Orthogonal( )),
			};
	    

	    String file = "lena.jpg";
		Mat mat = Highgui.imread("input\\" + file);
		mat.convertTo(mat, CvType.CV_64FC1);
//		double[] pixels = new double[(int)mat.size().area() * mat.channels()];
//		mat.get(0, 0, pixels);
		
		double[][] pixels = get2dArray(mat);
				
		for (int i = 0; i < transform.length; i++) {
			System.out.println(String.format("Transform: %s", i));
			double[][] forward = transform[i].forward(pixels);
			put2dArray(mat, forward);
			Highgui.imwrite(String.format("output\\test\\JWave_Test_%s_%s", i, file), mat);
		}
		System.out.println("Done!");
	    
	}
	
	public static double[][] get2dArray(Mat mat)
	{
		double[][] result = new double[mat.height()][mat.width()];
		for (int i = 0; i < result.length; i++) {
			mat.get(i, 0, result[i]);
		}
		return result;
	}
	
	public static void put2dArray(Mat mat, double[][] pixels)
	{
		for (int i = 0; i < pixels.length; i++) {
			mat.put(i, 0, pixels[i]);
		}
	}
}
