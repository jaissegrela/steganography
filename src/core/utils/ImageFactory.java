package core.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageFactory {

	public BufferedImage createIdentityImage(String identity, int width, int height){
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D graphics = result.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK);
		graphics.fillOval(0, 0, width, height);
		graphics.setColor(Color.WHITE);
		height >>= 1;
		Font font = new Font("Courier", Font.BOLD, height);
		graphics.setFont(font);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int strWidth = fontMetrics.stringWidth(identity);
		graphics.drawString(identity, (width - strWidth) >> 1, height + (height >> 2));
		return result;
	}
	
	public byte[] createIdentityImageInBytes(String identity, int width, int height){
		return getBytes(createIdentityImage(identity, width, height));
	}
	
	public byte[] getBytes(BufferedImage image){
		WritableRaster raster = image.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		return data.getData();
	}
	
	public BufferedImage createImage(int width, int height, byte[] data){
		return createImage(width, height, data, BufferedImage.TYPE_BYTE_BINARY);
	}
	
	public BufferedImage createImage(int width, int height, byte[] data, int imageType){
		BufferedImage result = new BufferedImage(width, height, imageType);
		final byte[] targetPixels = ((DataBufferByte) result.getRaster().getDataBuffer()).getData();
		System.arraycopy(data, 0, targetPixels, 0, data.length);
		return result;
	}

	public static BufferedImage createMergeImage(List<BufferedImage> images) {
		if(images.isEmpty())
			return null;
		BufferedImage bufferedImage = images.get(0);
		BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		int[][] count = new int[result.getHeight()][result.getWidth()];
		for (BufferedImage image : images) {
			for (int i = 0; i < count.length; i++) {
				for (int j = 0; j < count[0].length; j++) {
					int rgb = image.getRGB(i, j);
					if(rgb == -1)
						count[i][j]++;
				}
			}
		}
		int limite = 2 * (images.size() + 1) / 3;
		for (int i = 0; i < count.length; i++) {
			for (int j = 0; j < count[0].length; j++) {
				if(count[i][j] >= limite)
					result.setRGB(i, j, -1);
			}
		}
		return result;
	}
	
	public static Mat resizeImage(Mat src, double hZoom, double wZoom){
		Size size = src.size();
		Mat resizeimage = new Mat();
		Size sz = new Size(size.height * hZoom, size.width * wZoom);
		Imgproc.resize( src, resizeimage, sz );
		return resizeimage;
	}
	
	public static BufferedImage filter(BufferedImage image) {
		if(image == null)
			return null;
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		int[][] count = new int[result.getHeight()][result.getWidth()];
		for (int i = 1; i < count.length - 1; i++) {
			for (int j = 1; j < count[0].length - 1; j++) {
				int count1 = count(i, j, image);
				int value = image.getRGB(i, j);
				if(count1 >= 2)
					result.setRGB(i, j, value == -1 ? -1 : -16777216);
				else
					result.setRGB(i, j, value != -1 ? -1 : -16777216);
			}
		}
		return result;
	}

	private static int count(int row, int col, BufferedImage image) {
		int value = image.getRGB(row, col);
		int count = -1;
		for(int i = - 1; i < 2; i++)
			for (int j = -1; j < 2; j++) {
				if(image.getRGB(row + i, col + j) == value)
					count++;
			}
		return count;
	}
}
