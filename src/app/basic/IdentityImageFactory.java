package app.basic;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import core.utils.ImageFactory;

public class IdentityImageFactory {
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public static void main(String[] args) throws IOException {
	    String identity = "abc";
	    ImageFactory algorithm = new ImageFactory();
	    
	    save(algorithm.createIdentityImage(identity, 256, 256), new FileOutputStream("input\\identity256.bmp"));
	    
//	    for(int i = 10; i < 100; i+=2)
//	    	save(algorithm.createIdentityImage(identity, i, i), new FileOutputStream("output\\test\\identity" + i + ".bmp"));
//	   
	    System.out.print("Done!"); 
	}
	
	public static void save(BufferedImage image, OutputStream stream) throws IOException{
		ImageIO.write(image, "bmp", stream);
	}

}
