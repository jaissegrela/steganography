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
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import kanzi.ColorModelType;
import kanzi.IndexedIntArray;
import kanzi.transform.DWT_CDF_9_7;
import kanzi.util.color.ColorModelConverter;
import kanzi.util.color.YSbSrColorModelConverter;
import core.utils.Converter;

public class TestMatDWT2 {
	public static void main(String[] args) throws IOException {
		String fileName = (args.length > 0) ? args[0]
				: "D:\\Projects\\workspace\\Steganography\\input\\lena.bmp";
		
		File imgPath = new File(fileName);
		BufferedImage img = ImageIO.read(imgPath);
		WritableRaster raster = img.getRaster();
		byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();

		// Non square image
		int w = img.getWidth();
		int h = img.getHeight();

		GraphicsDevice gs = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices()[0];
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		BufferedImage img2 = gc
				.createCompatibleImage(w, h, Transparency.OPAQUE);
		BufferedImage img3 = gc
				.createCompatibleImage(w, h, Transparency.OPAQUE);
		int[] source = Converter.fromByteBGRtoIntRGB(data);
		int[] destination = new int[source.length];

		int dim = w;
		IndexedIntArray iia2 = new IndexedIntArray(source, 0);
		IndexedIntArray iia3 = new IndexedIntArray(destination, 0);
		
		ColorModelConverter cvt = new YSbSrColorModelConverter(w, h);// ,
																		// (y*w)+x,
																		// ww);
		process(dim, w, h, cvt, iia2, iia3);
		
		byte[] fromIntRGBtoByteBGR = Converter.fromIntRGBtoByteBGR(destination);
		
		img = ImageIO.read(imgPath);
		raster = img.getRaster();
		data = ((DataBufferByte) raster.getDataBuffer()).getData();
		System.arraycopy(fromIntRGBtoByteBGR, 0, data, 0, data.length);
		FileOutputStream stream = new FileOutputStream("output\\temp.bmp");
		ImageIO.write(img, "bmp", stream);
		stream.close();
		
		img2.getRaster().setDataElements(0, 0, w, h, destination);
		JFrame frame2 = new JFrame("Reverse");
		frame2.setBounds(720, 100, w, h);
		ImageIcon newIcon2 = new ImageIcon(img2);
		frame2.add(new JLabel(newIcon2));
		frame2.setVisible(true);

		img3.getRaster().setDataElements(0, 0, w, h, source);
		JFrame frame3 = new JFrame("Original");
		frame3.setBounds(100, 100, w, h);
		ImageIcon newIcon3 = new ImageIcon(img3);
		frame3.add(new JLabel(newIcon3));
		frame3.setVisible(true);

		try {
			Thread.sleep(10000);
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
//		iia1.array = y;
//		iia1.index = 0;
//		yDWT.inverse(iia1, iia1);
//		iia1.array = u;
//		iia1.index = 0;
//		uvDWT.inverse(iia1, iia1);
//		iia1.array = v;
//		iia1.index = 0;
//		uvDWT.inverse(iia1, iia1);

		cvt.convertYUVtoRGB(y, u, v, iia2.array, colorModelType);
		
		int[] v1 = new int[v.length];
		int[] u1 = new int[u.length];
		int[] y1 = new int[y.length];
		cvt.convertRGBtoYUV(iia2.array, y1, u1, v1, colorModelType);
		
		System.out.println(String.format("y %b", Arrays.equals(y1, y)));
		System.out.println(String.format("u %b", Arrays.equals(u1, u)));
		System.out.println(String.format("v %b", Arrays.equals(v1, v)));
		

		System.out.println("s");
	}

	@SuppressWarnings("unused")
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