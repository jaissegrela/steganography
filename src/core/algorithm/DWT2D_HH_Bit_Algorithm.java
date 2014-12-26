package core.algorithm;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2dBasic;
import core.utils.Converter;
import core.utils.enumerations.BitEnumeration;

public class DWT2D_HH_Bit_Algorithm extends DWT2D_Algorithm implements ISteganographyMemoryAlgorithm{
	
	protected ICoverMessage primeCoverMessage;

	public DWT2D_HH_Bit_Algorithm(ICoverMessage coverMessage, ICoverMessage primeCoverMessage,
			double visibilityfactor) {
		super(coverMessage, new Transform2dBasic(new DiscreteHaarWavelet()), visibilityfactor, 1);
		setPrimeCoverMessage(primeCoverMessage);
	}
	
	public DWT2D_HH_Bit_Algorithm(double visibilityfactor){
		this(null, null, visibilityfactor);
	}

	protected void transform(Mat mat, BitEnumeration enumerator) {
		Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
		if(value){
			double factor = visibilityfactor;
			double[] values = new double[mat.channels()];
			Arrays.fill(values, factor);
			
			Scalar scalar = new Scalar(values);
			int width = mat.width();
			int width2 = width >> 1;
			
			Mat submat = mat.submat(0, width2, 0, width2);
			Core.add(submat, scalar, submat);
			
			submat = mat.submat(width2, width, width2, width);
			Core.add(submat, scalar, submat);
			
			Arrays.fill(values, -factor);
			scalar = new Scalar(values);
			
			submat = mat.submat(0, width2, width2, width);
			Core.add(submat, scalar, submat);
			
			submat = mat.submat(width2, width, 0, width2);
			Core.add(submat, scalar, submat);
		}
	}

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[1];
		Mat matSource = primeCoverMessage.getMat();
		double factor = visibilityfactor * (4 << ((getLevels(mat.width()) - 1) << 1)); //Mat.pow(4, level1)* .25;
		factor *= .3;
		double[] pixel = mat.get(1, 1);
		double[] source = matSource.get(1, 1);
		int value = 0;
		for (int k = 0; k < pixel.length; k++) {
			double abs = Math.abs(pixel[k] - source[k]);
			//System.out.println(String.format("Diference: %s %s", abs, abs / visibilityfactor));
			if(abs > factor)
				value++;
		}
		result[0] = value >= ((double)pixel.length / 2);
		return result;
	}

	@Override
	public void setPrimeCoverMessage(ICoverMessage primeCoverMessage) {
		if(primeCoverMessage != null) {
			Mat mat = primeCoverMessage.getMat();
			transform.transform(mat, getLevels(mat.width()));
		}
		this.primeCoverMessage = primeCoverMessage;
	}
	
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		ICoverMessage result = coverMessage.duplicateMessage();
		Mat mat = result.getMat();
		BitEnumeration enumerator = new BitEnumeration(embeddedData);
		transform(mat, enumerator);
		return result;
	}
	
	public byte[] getEmbeddedData() {
		Mat mat = coverMessage.getMat();
		int levels = getLevels(mat.width());
		transform.transform(mat, levels);
		boolean[] result = inverse(mat);
		transform.inverse(mat, levels);
		return Converter.toShrinkArrayofByte(result);
	}

	protected int getLevels(int width) {
		return (int)(Math.log(width)/Math.log(2));
	}

}
