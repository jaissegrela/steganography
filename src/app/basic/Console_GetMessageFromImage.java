package app.basic;

import java.io.FileOutputStream;
import java.io.IOException;

import core.algorithm.LSBAlgorithm;
import core.message.BasicImageMessage;

public class Console_GetMessageFromImage {

	public static void main(String[] args) throws IOException {
		String filename = "output\\scene_message.bmp";
		BasicImageMessage cover_message = new BasicImageMessage(filename);
		LSBAlgorithm lsb = new LSBAlgorithm(cover_message);
		byte[] embeddedData = lsb.getEmbeddedData();
		String message = new String(embeddedData);
		FileOutputStream file = new FileOutputStream("output\\file.txt");
		file.write(embeddedData);
		file.close();
		System.out.println(message);
	}

}
