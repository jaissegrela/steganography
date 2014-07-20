package app.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import core.algorithm.ISteganographyAlgorithm;
import core.algorithm.RawLSBAlgorithm;
import core.attack.RadiographyAlgorithm;
import core.message.BasicImageMessage;
import core.message.FileMessage;

public class Console_ImageMessageToImageToMessage {

	public static void main(String[] args) throws IOException {
		String filename = "input\\scene.bmp";
		BasicImageMessage cover_message = new BasicImageMessage(filename);
		ISteganographyAlgorithm lsb = new RawLSBAlgorithm(cover_message);
		FileMessage message = new FileMessage("input\\Steganography.bmp");
		//FileMessage message = new FileMessage("input\\message.txt");
		BasicImageMessage stegoObject = (BasicImageMessage) lsb
				.getStegoObject(message);
		System.out.println("Rate: " + lsb.getStegoObjectRate(message));
		stegoObject.save(new FileOutputStream("output\\escene_steganography_test.bmp"));
		lsb = new RawLSBAlgorithm(stegoObject);
		byte[] embedded_data = lsb.getEmbeddedData();
		byte[] typeMessage = lsb.getTypeMessage();
		String name = "output\\Steganography." + new String(typeMessage);
		write(embedded_data, name);
		
		/*Analysis*/
		RadiographyAlgorithm radiography = new RadiographyAlgorithm();
		radiography.getRadiographyImage(new File("output\\escene_steganography_test.bmp"),
				new FileOutputStream("output\\radiography.bmp"));
		
		
		System.out.println("Done!");
	}

	private static void write(byte[] embedded_data, String name)
			throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(name);
		file.write(embedded_data);
		file.close();
	}

}
