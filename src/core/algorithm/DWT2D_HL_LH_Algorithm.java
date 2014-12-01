package core.algorithm;

import org.bytedeco.javacpp.opencv_core.Mat;

import core.message.ICoverMessage;
import core.transform.Transform2d;
import core.utils.MatOperations;
import core.utils.enumerations.BitEnumeration;

public class DWT2D_HL_LH_Algorithm extends DWT2D_Algorithm{

	public DWT2D_HL_LH_Algorithm(ICoverMessage coverMessage, Transform2d transform, double visibilityfactor, int levels) {
		super(coverMessage, transform, visibilityfactor, levels);
	}

	public DWT2D_HL_LH_Algorithm(ICoverMessage coverMessage, Transform2d transform) {
		this(coverMessage, transform, 1, 1);
	}

	protected void transform(Mat result, BitEnumeration enumerator) {
		MatOperations oMat = new MatOperations(result);
		for (int i = 0; i < (result.rows() >> levels); i++) {
			for (int j = result.cols() >> levels; j < result.cols() >> (levels - 1); j++) {
				Boolean value = enumerator.hasMoreElements() ? enumerator.nextElement() : false;
				double[] result_i_j = oMat.getPixel(i, j);
				double[] result_j_i = oMat.getPixel(j, i);
				for (int k = 0; k < result_j_i.length; k++) {
					if(value){
						double d1 = result_i_j[k] - result_j_i[k];
						if(d1 < visibilityfactor){
							result_i_j[k] += (visibilityfactor - d1) / 2;
							result_j_i[k] -= (visibilityfactor - d1) / 2;
						}
					}else{
						double d2 = result_j_i[k] - result_i_j[k];
						if(d2 < visibilityfactor){
							result_i_j[k] -= (visibilityfactor - d2) / 2;
							result_j_i[k] += (visibilityfactor - d2) / 2;
						}
					}	
				}
				oMat.setPixel(j, i, result_j_i);
				oMat.setPixel(i, j, result_i_j);
			}
		}
	}

	protected boolean[] inverse(Mat mat) {
		boolean[] result = new boolean[getMaxSizeMessageToHide() >> ((levels - 1) << 1)];
		MatOperations oMat = new MatOperations(mat);
		for (int i = 0, index = 0; i < mat.rows() >> levels; i++) {
			for (int j = mat.cols() >> levels; j < mat.cols() >> (levels - 1) && index < result.length; j++) {
				double[] result_i_j = oMat.getPixel(i, j);
				double[] result_j_i = oMat.getPixel(j, i);
				int value = 0;
				for (int k = 0; k < result_j_i.length; k++) {
					if(result_i_j[k] > result_j_i[k])
						value++;
				}
				result[index] = value >= ((double)result_i_j.length / 2);
				index++;
			}
		}
		return result;
	}

}
