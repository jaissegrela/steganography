package app.basic;

import java.io.FileOutputStream;
import java.io.IOException;

import core.algorithm.LSBAlgorithm;
import core.message.BasicImageMessage;
import core.message.TextFileMessage;

public class Console_MessageToImageToMessage {

	public static void main(String[] args) throws IOException {
		String filename = "input\\scene.bmp";
		BasicImageMessage cover_message = new BasicImageMessage(filename);
		LSBAlgorithm lsb = new LSBAlgorithm(cover_message);
		TextFileMessage message = new TextFileMessage("input\\message.txt");
		BasicImageMessage stegoObject = (BasicImageMessage) lsb
				.getStegoObject(message);
		stegoObject.save(new FileOutputStream("output\\scene_message_test.bmp"));
		lsb = new LSBAlgorithm(stegoObject);
		byte[] embedded_data = lsb.getEmbeddedData();
		String result = new String(embedded_data);
		System.out.println(result);
	}

}
