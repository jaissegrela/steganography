package test.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import core.message.CacheMessage;
import core.utils.MessageComparatorByPosition;

public class MessageComparatorByPositionTest {
	
	private MessageComparatorByPosition message;

	@Before
	public void setUp() throws Exception {
		message = new MessageComparatorByPosition(new CacheMessage("ABC".getBytes()));
	}

	@Test
	public void testSimilarity_EqualMessage() {
		double actual = message.similarity(new CacheMessage("ABC".getBytes()));
		double expected = 100;
		assertEquals(expected, actual, .001);
	}
	
	@Test
	public void testSimilarity_DiferentMessage() {
		double actual = message.similarity(new CacheMessage(new byte[]{0,0,0}));
		double expected = 49.11688262920872;
		assertEquals(expected, actual, .001);
	}

}
