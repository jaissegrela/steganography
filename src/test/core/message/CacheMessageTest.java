package test.core.message;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.message.CacheMessage;

public class CacheMessageTest {

	private CacheMessage cacheMessage;
	private byte[] cache;

	@Before
	public void setUp() throws Exception {
		cache = new byte[]{0, 1, 2, 3};
		cacheMessage = new CacheMessage(cache);
	}

	@Test
	public void testBytes() {
		int actual = cacheMessage.bytes();
		int expected = cache.length;
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllBytes() {
		byte[] actual = cacheMessage.getAllBytes();
		assertArrayEquals(cache, actual);
	}

	@Test
	public void testGetByte() {
		for (int i = 0; i < cache.length; i++) {
			int actual = cacheMessage.getByte(i);
			int expected = cache[i];
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = IndexOutOfBoundsException.class) 
	public void testGetByteOutBound() {
		cacheMessage.getByte(cache.length);
	}

}
