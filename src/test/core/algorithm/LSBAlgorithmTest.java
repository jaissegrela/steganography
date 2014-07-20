package test.core.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import test.core.message.DummyMessage;

import core.algorithm.ISteganographyAlgorithm;
import core.algorithm.LSBAlgorithm;
import core.message.ICoverMessage;
import core.message.IMessage;

public class LSBAlgorithmTest{

	private LSBAlgorithm lsbAlgorithm;
	private DummyMessage dummyMessage;

	@Before
	public void setUp() throws Exception {
		byte[] values = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				16, 17, 18, 19, 20 };
		byte[] cover_message = new byte[32 + values.length];
		for (int i = 0; i < values.length; i++) {
			cover_message[i + 32] = values[i];
		}
		dummyMessage = new DummyMessage(cover_message);
		lsbAlgorithm = new LSBAlgorithm(dummyMessage);
	}
	
	@Test
	public void testStegoObjectRate() {
		byte[] values = { 1, 0};
		DummyMessage parameter = new DummyMessage(values);
	
		double actual = lsbAlgorithm.getStegoObjectRate(parameter);
		double expected = ((double)values.length + 4) * 8 / dummyMessage.bytes();
		assertEquals(expected, actual, 0.01);
	}

	@Test
	public void testhasHiddenMessage() {
		byte[] values = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 4, 4, 6,
				6, 8, 9, 10, 10, 12, 12, 14, 14, 16, 17, 18, 19, 20 };

		DummyMessage dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new LSBAlgorithm(dummyMessage);
		assertTrue(lsbAlgorithm.hasHiddenMessage());
	}

	@Test
	public void testhasNotHiddenMessageLength0() {
		byte[] values = new byte[0];
		DummyMessage dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new LSBAlgorithm(dummyMessage);
		assertFalse(lsbAlgorithm.hasHiddenMessage());
	}

	@Test
	public void testhasNotHiddenMessageLengthTooBig() {
		byte[] values = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 4, 4, 6,
				6, 8, 9, 10, 10, 12, 12, 14, 14, 16, 17, 18, 19, 20 };

		DummyMessage dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new LSBAlgorithm(dummyMessage);
		assertFalse(lsbAlgorithm.hasHiddenMessage());
	}

	@Test
	public void testhasNotHiddenMessageLengthTooSmall() {
		byte[] values = { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 4, 4, 6,
				6, 8, 9, 10, 10, 12, 12, 14, 14, 16, 17, 18, 19, 20 };

		DummyMessage dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new LSBAlgorithm(dummyMessage);
		assertFalse(lsbAlgorithm.hasHiddenMessage());
	}

	@Test
	public void testGetStegoObject() {
		byte[] values = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2, 4, 4, 6,
				6, 8, 9, 10, 10, 12, 12, 14, 14, 16, 17, 18, 19, 20 };
		DummyMessage expected = new DummyMessage(values);

		DummyMessage embeddedData = new DummyMessage(new byte[] { 1, 2 });
		IMessage actual = lsbAlgorithm.getStegoObject(embeddedData);

		assertEquals(expected, actual);
	}

	@Test
	public void testGetEmbeddedData() {
		byte[] expected = { 1, 2 };
		ICoverMessage temp = lsbAlgorithm.getStegoObject(new DummyMessage(
				expected));
		ISteganographyAlgorithm lsb = new LSBAlgorithm(temp);
		byte[] actual = lsb.getEmbeddedData();

		assertTrue((Arrays.equals(expected, actual)));
	}

}
