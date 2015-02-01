package core.algorithm;

import java.util.Enumeration;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.transform.DiscreteHaarWavelet;
import core.transform.Transform2dBasic;
import core.utils.MatOperations;

public class DWT2D_HH_Bit_Algorithm extends DWT2D_Algorithm{

	public DWT2D_HH_Bit_Algorithm(ICoverMessage coverMessage,
			double visibilityfactor) {
		this(coverMessage, null, visibilityfactor);
	}
	
	public DWT2D_HH_Bit_Algorithm(double visibilityfactor){
		this(null, null, visibilityfactor);
	}
	
	public DWT2D_HH_Bit_Algorithm(ICoverMessage coverMessage, ICoverMessage stegoMessage,
			double visibilityfactor) {
		super(coverMessage, stegoMessage, new Transform2dBasic(new DiscreteHaarWavelet()), visibilityfactor, 1);
	}
	
	protected void transform(Mat mat, Enumeration<Boolean> enumeration) {
		Boolean value = enumeration.hasMoreElements() ? enumeration.nextElement() : false;
		if(value){
			int width = mat.cols();
			int width2 = width >> 1;
			Mat v = new Mat(width2, width2, mat.type(), new Scalar(getVisibilityfactor(), getVisibilityfactor(), getVisibilityfactor(), getVisibilityfactor()));
			
			Mat submat = mat.colRange(0, width2).rowRange(0, width2);
			opencv_core.add(submat, v, submat);
			
			submat = mat.colRange(width2, width).rowRange(width2, width);
			opencv_core.add(submat, v, submat);
			
			v.put(new Scalar(-getVisibilityfactor(), -getVisibilityfactor(), -getVisibilityfactor(), -getVisibilityfactor()));
			
			submat = mat.colRange(0, width2).rowRange(width2, width);
			opencv_core.add(submat, v, submat);
			
			submat = mat.colRange(width2, width).rowRange(0, width2);
			opencv_core.add(submat, v, submat);
		}
	}

	protected boolean[] inverse(Mat stegomessage, Mat covermessage, int size) {
		boolean[] result = new boolean[1];
		
		double factor = getVisibilityfactor() * (4 << ((getLevels() - 1) << 1)); //Mat.pow(4, level1)* .25;
		factor *= .3;
		MatOperations oMat = new MatOperations(stegomessage);
		MatOperations oMatCoverMessage = new MatOperations(covermessage);
		double[] pixel = oMat.getPixel(1, 1);
		double[] source = oMatCoverMessage.getPixel(1, 1);
		int value = 0;
		for (int k = 0; k < source.length; k++) {
			double abs = Math.abs(pixel[k] - source[k]);
			if(abs > factor)
				value++;
		}
		result[0] = value >= ((double)pixel.length / 2);
		return result;
	}

	
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		ICoverMessage result = getCoverMessage().duplicateMessage();
		transform(result.getMat(), embeddedData.getEnumeration());
		return result;
	}

	@Override
	public int getLevels() {
		return (int)(Math.log(getCoverMessage().getMat().rows())/Math.log(2));
	}

}
