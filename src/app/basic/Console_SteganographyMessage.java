package app.basic;

import java.io.FileOutputStream;
import java.io.IOException;

import core.algorithm.LSBAlgorithm;
import core.message.BasicImageMessage;
import core.message.ICoverMessage;
import core.message.TextFileMessage;

public class Console_SteganographyMessage {

	public static void main(String[] args) throws IOException {
		BasicImageMessage cover_message = new BasicImageMessage(
				"input\\scene.bmp");
		LSBAlgorithm lsb = new LSBAlgorithm(cover_message);
		TextFileMessage message = new TextFileMessage("input\\message.txt");

		ICoverMessage stegoObject = lsb.getStegoObject(message);

		stegoObject.save(new FileOutputStream("output\\scene_message.bmp"));
		System.out.println("Done!");
	}

}
