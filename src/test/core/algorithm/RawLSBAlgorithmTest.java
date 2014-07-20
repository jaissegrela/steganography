package test.core.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import test.core.message.DummyMessage;

import core.algorithm.ISteganographyAlgorithm;
import core.algorithm.RawLSBAlgorithm;
import core.message.ICoverMessage;
import core.message.IMessage;

public class RawLSBAlgorithmTest {

	private ISteganographyAlgorithm lsbAlgorithm;
	private DummyMessage dummyMessage;

	@Before
	public void setUp() throws Exception {
		byte[] values = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				16, 17, 18, 19, 20 };
		dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new RawLSBAlgorithm(dummyMessage);
	}
	
	@Test
	public void testStegoObjectRate() {
		byte[] values = { 1, 0};
		DummyMessage parameter = new DummyMessage(values);
	
		double actual = lsbAlgorithm.getStegoObjectRate(parameter);
		double expected = ((double)values.length * 8) / dummyMessage.bytes();
		assertEquals(expected, actual, 0.01);
	}

	@Test
	public void testGetStegoObject() {
		byte[] values = { 1, 0, 2, 2, 4, 4, 6, 6, 8, 9, 10, 10, 12, 12, 14, 14,
				16, 17, 18, 19, 20 };
		DummyMessage expected = new DummyMessage(values);

		DummyMessage embedded_data = new DummyMessage(new byte[] { 1, 2 });
		IMessage actual = lsbAlgorithm.getStegoObject(embedded_data);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetEmbeddedData() {
		DummyMessage parameter = new DummyMessage(new byte[] { -1, 2 });
		IMessage temp = lsbAlgorithm.getStegoObject(parameter);
		ISteganographyAlgorithm lsb = new RawLSBAlgorithm((ICoverMessage) temp);
		byte[] actual = lsb.getEmbeddedData();
		byte[] expected = { -1, 2, 10 };

		assertTrue((Arrays.equals(expected, actual)));
	}
	
	@Test
	public void testhasHiddenMessage() {
		assertTrue(lsbAlgorithm.hasHiddenMessage());
	}

	@Test
	public void testhasNotHiddenMessage() {
		byte[] values = new byte[0];
		DummyMessage dummyMessage = new DummyMessage(values);
		lsbAlgorithm = new RawLSBAlgorithm(dummyMessage);
		assertFalse(lsbAlgorithm.hasHiddenMessage());
	}

}
