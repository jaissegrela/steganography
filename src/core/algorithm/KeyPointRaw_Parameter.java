package core.algorithm;

import core.message.MatImage;

public class KeyPointRaw_Parameter {

	private MatImage coverMessage;

	public KeyPointRaw_Parameter(MatImage coverMessage) {
		this.coverMessage = coverMessage;
	}

	public int getKeyPointSize() {
		return coverMessage.getMat().size().width() > 3000 && coverMessage.getMat().size().height() > 2000 ? 32 : 16 ;
	}

	public int getPointsByBit() {
		return coverMessage.getMat().size().width() > 3000 && coverMessage.getMat().size().height() > 2000 ? 9 : 7 ;
	}

	public double getVisibilityfactor() {
		return coverMessage.getMat().depth() != 2 ? 3 : 50;
	}

}
