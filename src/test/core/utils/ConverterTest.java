package test.core.utils;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import core.utils.Converter;

public class ConverterTest {

	@Test
	public void testToArrayOfByte() {
		byte[] expected = { 0, 0, 4, -86 };
		byte[] actual = Converter.toArrayOfByte(1194);
		assertTrue(Arrays.equals(expected, actual));
	}

	@Test
	public void testToInt() {
		int expected = 1194;
		int actual = Converter.toInt(Converter.toArrayOfByte(1194));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToBooleanToByte(){
		for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
			System.out.println((byte)i);
			boolean[] arrayofBoolean = Converter.toArrayofBoolean((byte)i);
			byte[] actual = Converter.toShrinkArrayofByte(arrayofBoolean);
			assertTrue(actual.length == 1);
			assertEquals("" + i, (byte)i, actual[0]);
		}
	}
	
	@Test
	public void testToBooleanToByteMultiple(){
		boolean[] arrayofBoolean = {false, false, false, false, false, false, true, false, true, false, false};
			byte[] actual = Converter.toShrinkArrayofByte(arrayofBoolean);
			assertTrue(actual.length == 2);
			assertEquals(64, actual[0]);
			assertEquals(1, actual[1]);
	}
}
