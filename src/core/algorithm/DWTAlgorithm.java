package core.algorithm;

import core.message.ICoverMessage;
import core.message.IMessage;
import core.utils.ByteInfo;

public class DWTAlgorithm implements ISteganographyAlgorithm {
	
	protected ICoverMessage coverMessage;
	protected double factor;
	protected ByteInfo info;

	/**
	 * @param coverMessage
	 *            the cover medium for hide message
	 */
	public DWTAlgorithm(ICoverMessage coverMessage, double factor) {
		this.coverMessage = coverMessage;
		this.factor = factor;
		info = new ByteInfo();
	}
	
	public DWTAlgorithm(double factor) {
		this(null, factor);
	}

	@Override
	public double getStegoObjectRate(IMessage embeddedData) {
		return ((double) embeddedData.bytes()) * ByteInfo.BYTE_SIZE
				/ (coverMessage.bytes() << 2);
	}

	@Override
	public ICoverMessage getStegoObject(IMessage embeddedData) {
		throw new UnsupportedOperationException("Not implemented for new version");
		/*
		ICoverMessage result = coverMessage.duplicateMessage();
		FastDiscreteBiorthogonal_CDF_9_7 alg = new FastDiscreteBiorthogonal_CDF_9_7();
		Mat mat = result.getMat();
		mat.convertTo(mat, opencv_core.CV_64FC1);
		Size size = mat.size();
		int length = (int)mat.elemSize();//(int)size.area() * mat.channels();
		double[] pixels = new double[length];
		mat.getDoubleBuffer().get(pixels);
		alg.transform(pixels, pixels.length);
		mat.getDoubleBuffer().put(pixels);
		
		length >>= 2;
		
		double[] h = new double[length];
		mat.get(0, size.width() >> 1, h);
		
		double[] v = new double[length];
		mat.get(size.height() >> 1, 0, v);
		
		int image_index = 0;
		for (int i = 0; i < embeddedData.bytes(); i++) {
			byte data = embeddedData.getByte(i);
			for (int j = 0; j < ByteInfo.BYTE_SIZE; j++) {
				byte bit = ByteInfo.getLSB(data);
				data >>= 1;
				if(bit > 0){
					double d1 = h[image_index] - v[image_index];
					if(d1 < factor){
						h[image_index] += (factor - d1) / 2;
						v[image_index] -= (factor - d1) / 2;
					}
				}else{
					double d2 = v[image_index] - h[image_index];
					if(d2 >= factor){
						h[image_index] -= (factor - d2) / 2;
						v[image_index] += (factor - d2) / 2;
					}
				}
			}
		}
		
		mat.put(0, (int)size.width >> 1, h);
		mat.put((int)size.height >> 1, 0, v);
		mat.get(0, 0, pixels);
		alg.inverse(pixels, pixels.length);
		
		
		for (int i = 0; i < pixels.length; i++) {
			result.setByte(i, (byte)pixels[i]);
		}
		return result;*/
	}

	@Override
	public boolean hasHiddenMessage() {
		return coverMessage.bytes() > 0;
	}

	@Override
	public byte[] getTypeMessage() {
		return new byte[0];
	}

	@Override
	public byte[] getEmbeddedData() {
		throw new UnsupportedOperationException("Not implemented for new version");
		/*
		byte[] result = new byte[getMaxSizeMessageToHide() / ByteInfo.BYTE_SIZE];
		
		Mat mat = coverMessage.getMat();
		mat.convertTo(mat, CvType.CV_64FC1);
		Size size = mat.size();
		int length = (int)size.area() * mat.channels();
		double[] pixels = new double[length];
		mat.get(0, 0, pixels);
		
		FastDiscreteBiorthogonal_CDF_9_7 alg = new FastDiscreteBiorthogonal_CDF_9_7();
		alg.transform(pixels, pixels.length);
		mat.put(0, 0, pixels);
		
		length >>= 2;
		
		double[] h = new double[length];
		mat.get(0, (int)size.width >> 1, h);
		
		double[] v = new double[length];
		mat.get((int)size.height >> 1, 0, v);
		
		int result_index = 0;
		byte temp = 0;
		int move = ByteInfo.BYTE_SIZE - 1;
		for (int i = 0; i < v.length; i++) {
			byte lsb = (byte) (h[i] > v[i] ? 1 : 0);
			temp |= (lsb << move);
			move--;
			if (move < 0) {
				result[result_index] = temp;
				result_index++;
				move = ByteInfo.BYTE_SIZE - 1;
				temp = 0;
			}
		}
		
		return result;
		*/
	}

	@Override
	public ICoverMessage getCoverMessage() {
		return this.coverMessage;
	}

	@Override
	public void setCoverMessage(ICoverMessage coverMessage) {
		this.coverMessage = coverMessage;
	}

	@Override
	public int getMaxSizeMessageToHide() {
		return coverMessage.bytes() >> 2;
	}

}
