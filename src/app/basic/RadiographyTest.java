package app.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import core.attack.RadiographyAlgorithm;

public class RadiographyTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws IOException {
		RadiographyAlgorithm radiography = new RadiographyAlgorithm();
		String input = "output\\message_lena.bmp";
		String output = "output\\radiography_message_lena.bmp";
		
		input = "output\\lena.gif";
		output = "output\\radiography_lena_gif.bmp";
		
		radiography.getRadiographyImage(new File(input),
				new FileOutputStream(output));
	}

}
