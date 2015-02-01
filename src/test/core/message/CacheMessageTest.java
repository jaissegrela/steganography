package test.core.message;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

import core.message.CacheMessage;
import core.utils.Converter;

public class CacheMessageTest {

	private CacheMessage cacheMessage;
	private byte[] cache;

	@Before
	public void setUp() throws Exception {
		cache = new byte[] { 0, 1, 2, 3 };
		cacheMessage = new CacheMessage(cache);
	}

	@Test
	public void testBytes() {
		int actual = cacheMessage.size();
		int expected = cache.length * 8;
		assertEquals(expected, actual);
	}

	@Test
	public void testEnumerator() {
		Enumeration<Boolean> enumeration = cacheMessage.getEnumeration();
		boolean[] input = new boolean[cacheMessage.size()];
		int i = 0;
		while (enumeration.hasMoreElements()) {
			input[i++] = enumeration.nextElement();
		}
		byte[] actual = Converter.toShrinkArrayofByte(input);
		assertArrayEquals(cache, actual);
	}

}
