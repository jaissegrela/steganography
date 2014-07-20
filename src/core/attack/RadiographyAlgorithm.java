package core.attack;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import core.algorithm.RawLSBAlgorithm;
import core.message.BasicImageMessage;

public class RadiographyAlgorithm{

	public void getRadiographyImage(File image, OutputStream stream) throws IOException{
		
		BufferedImage img = ImageIO.read(image);
		
		BasicImageMessage message = new BasicImageMessage(image);
		RawLSBAlgorithm algorithm = new RawLSBAlgorithm(message);
		byte[] embeddedData = algorithm.getEmbeddedData();
		
		ColorModel colorModel = img.getColorModel();
		int width = colorModel.getPixelSize() >> 3;
		width *= img.getWidth();
		
		BufferedImage result = new BufferedImage(width, img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		final byte[] targetPixels = ((DataBufferByte) result.getRaster().getDataBuffer()).getData();
		System.arraycopy(embeddedData, 0, targetPixels, 0, embeddedData.length);  
		ImageIO.write(result, "bmp", stream);
	}
}
