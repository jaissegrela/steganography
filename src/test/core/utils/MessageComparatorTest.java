package test.core.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.message.CacheMessage;
import core.utils.IMessageComparator;
import core.utils.MessageComparator;

public class MessageComparatorTest {
	
	private IMessageComparator message;

	@Before
	public void setUp() throws Exception {
		message = new MessageComparator(new CacheMessage("ABC".getBytes()));
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
		double expected = (24 - 7) * 100d / 24;
		assertEquals(expected, actual, .001);
	}

}
