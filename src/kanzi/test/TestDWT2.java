/*
Copyright 2011-2013 Frederic Langlet
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
you may obtain a copy of the License at

                http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package kanzi.test;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import kanzi.ColorModelType;
import kanzi.IndexedIntArray;
import kanzi.transform.DWT_CDF_9_7;
import kanzi.util.ImageQualityMonitor;
import kanzi.util.color.ColorModelConverter;
import kanzi.util.color.YSbSrColorModelConverter;

public class TestDWT2 {
	public static void main(String[] args) throws IOException {
		String fileName = (args.length > 0) ? args[0]
				: "D:\\Projects\\workspace\\Steganography\\input\\lena.bmp";
		ImageIcon icon = new ImageIcon(fileName);
		Image image = icon.getImage();

		// Non square image
		int w = image.getWidth(null);
		int h = image.getHeight(null);

		if (image.getWidth(null) <= 0) {
			System.out.println("Cannot find file " + fileName);
			System.exit(1);
		}

		GraphicsDevice gs = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices()[0];
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		BufferedImage img = gc.createCompatibleImage(w, h, Transparency.OPAQUE);
		BufferedImage img2 = gc
				.createCompatibleImage(w, h, Transparency.OPAQUE);
		BufferedImage img3 = gc
				.createCompatibleImage(w, h, Transparency.OPAQUE);
		img.getGraphics().drawImage(image, 0, 0, null);
		int[] source = new int[w * h];
		int[] destination = new int[w * h];
		img.getRaster().getDataElements(0, 0, w, h, source);

		int dim = w;
		IndexedIntArray iia2 = new IndexedIntArray(source, 0);
		IndexedIntArray iia3 = new IndexedIntArray(destination, 0);
		long before = System.nanoTime();

		// ColorModelConverter cvt = new YCbCrColorModelConverter(w, h);//,
		// (y*w)+x, ww);
		ColorModelConverter cvt = new YSbSrColorModelConverter(w, h);// ,
																		// (y*w)+x,
																		// ww);
		process(dim, w, h, cvt, iia2, iia3);
		long after = System.nanoTime();
		System.out.println("Time elapsed [ms]: " + (after - before) / 1000000);

		ImageQualityMonitor monitor = new ImageQualityMonitor(w, h);
		int psnr1024 = monitor.computePSNR(source, destination);
		System.out.println("PSNR: " + (float) psnr1024 / 1024);
		int ssim1024 = monitor.computeSSIM(source, destination);
		System.out.println("SSIM: " + (float) ssim1024 / 1024);

		img2.getRaster().setDataElements(0, 0, w, h, destination);
		JFrame frame2 = new JFrame("Reverse");
		frame2.setBounds(720, 100, w, h);
		ImageIcon newIcon2 = new ImageIcon(img2);
		frame2.add(new JLabel(newIcon2));
		frame2.setVisible(true);

//		byte[] fromIntRGBtoByteBGR = Converter.fromIntRGBtoByteBGR(destination);
//		
//		File imgPath = new File(fileName);
//		BufferedImage img10 = ImageIO.read(imgPath);
//		WritableRaster raster = img10.getRaster();
//		byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();
//		System.arraycopy(fromIntRGBtoByteBGR, 0, data, 0, data.length);
//		FileOutputStream stream = new FileOutputStream("output\\temp.jpg");
//		ImageIO.write(img10, "jpg", stream);
//		stream.close();
//		
//		File imgPath1 = new File("output\\temp.jpg");
//		BufferedImage img101 = ImageIO.read(imgPath1);
//		WritableRaster raster1 = img101.getRaster();
//		byte[] data1 = ((DataBufferByte) raster1.getDataBuffer()).getData();
//	
//		
//		System.out.println(String.format("Test %b", Arrays.equals(data, data1)));

		img3.getRaster().setDataElements(0, 0, w, h, source);
		JFrame frame3 = new JFrame("Original");
		frame3.setBounds(100, 100, w, h);
		ImageIcon newIcon3 = new ImageIcon(img3);
		frame3.add(new JLabel(newIcon3));
		frame3.setVisible(true);

		try {
			Thread.sleep(40000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	private static void process(int dim, int w, int h, ColorModelConverter cvt,
			IndexedIntArray iia1, IndexedIntArray iia2) {
		int[] y = new int[w * h];
		int[] u = new int[w * h / 4];
		int[] v = new int[w * h / 4];
		ColorModelType colorModelType = ColorModelType.YUV420;

		cvt.convertRGBtoYUV(iia1.array, y, u, v, colorModelType);
		DWT_CDF_9_7 yDWT = new DWT_CDF_9_7(w, h, 1);
		DWT_CDF_9_7 uvDWT = new DWT_CDF_9_7(w / 2, h / 2, 1);

		iia1.array = y;
		iia1.index = 0;
		yDWT.forward(iia1, iia1);
		iia1.array = u;
		iia1.index = 0;
		uvDWT.forward(iia1, iia1);
		iia1.array = v;
		iia1.index = 0;
		uvDWT.forward(iia1, iia1);
		
		// Inverse
		iia1.array = y;
		iia1.index = 0;
		yDWT.inverse(iia1, iia1);
		iia1.array = u;
		iia1.index = 0;
		uvDWT.inverse(iia1, iia1);
		iia1.array = v;
		iia1.index = 0;
		uvDWT.inverse(iia1, iia1);

		cvt.convertYUVtoRGB(y, u, v, iia2.array, colorModelType);
	}

	private static void processInverse(int dim, int w, int h,
			ColorModelConverter cvt, IndexedIntArray iia1, IndexedIntArray iia2) {
		int[] y = new int[w * h];
		int[] u = new int[w * h / 4];
		int[] v = new int[w * h / 4];
		ColorModelType colorModelType = ColorModelType.YUV420;

		cvt.convertRGBtoYUV(iia1.array, y, u, v, colorModelType);
		DWT_CDF_9_7 yDWT = new DWT_CDF_9_7(w, h, 1);
		DWT_CDF_9_7 uvDWT = new DWT_CDF_9_7(w / 2, h / 2, 1);

		// Inverse
		iia1.array = y;
		iia1.index = 0;
		yDWT.inverse(iia1, iia1);
		iia1.array = u;
		iia1.index = 0;
		uvDWT.inverse(iia1, iia1);
		iia1.array = v;
		iia1.index = 0;
		uvDWT.inverse(iia1, iia1);

		cvt.convertYUVtoRGB(y, u, v, iia2.array, colorModelType);
	}
}