package core.algorithm;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2dBasic;
import core.utils.Converter;
import core.utils.MatOperations;
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
			int width = mat.cols();
			int width2 = width >> 1;
			Mat v = new Mat(width2, width2, mat.type(), new Scalar(visibilityfactor, visibilityfactor, visibilityfactor, visibilityfactor));
			
			Mat submat = mat.colRange(0, width2).rowRange(0, width2);
			opencv_core.add(submat, v, submat);
			
			submat = mat.colRange(width2, width).rowRange(width2, width);
			opencv_core.add(submat, v, submat);
			
			v.put(new Scalar(-visibilityfactor, -visibilityfactor, -visibilityfactor, -visibilityfactor));
			
			submat = mat.colRange(0, width2).rowRange(width2, width);
			opencv_core.add(submat, v, submat);
			
			submat = mat.colRange(width2, width).rowRange(0, width2);
			opencv_core.add(submat, v, submat);
		}
	}

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[1];
		Mat matSource = primeCoverMessage.getMat();
		double factor = visibilityfactor * (4 << ((getLevels(mat.rows()) - 1) << 1)); //Mat.pow(4, level1)* .25;
		factor *= .3;
		MatOperations oMat = new MatOperations(mat);
		MatOperations oMatSource = new MatOperations(matSource);
		double[] pixel = oMat.getPixel(1, 1);
		double[] source = oMatSource.getPixel(1, 1);
		int value = 0;
		for (int k = 0; k < source.length; k++) {
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
//			System.out.println("Cover");
//			Arrays2d.print(mat);
			transform.transform(mat, getLevels(mat.rows()));
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
//		System.out.println("Stego");
//		Arrays2d.print(mat);
		int levels = getLevels(mat.rows());
		transform.transform(mat, levels);
		boolean[] result = inverse(mat);
		transform.inverse(mat, levels);
		return Converter.toShrinkArrayofByte(result);
	}

	protected int getLevels(int width) {
		return (int)(Math.log(width)/Math.log(2));
	}

}
