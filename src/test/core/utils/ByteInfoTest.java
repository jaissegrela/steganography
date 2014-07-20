package test.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.utils.ByteInfo;

public class ByteInfoTest{

	@Test
	public void testModifyLSB_ToActiveAnActivePosition() {
		byte expected = 3; // 00000011
		byte actual = ByteInfo.modifyLSB(expected, true);
		assertEquals("00000011", expected, actual);
	}

	@Test
	public void testModifyLSB_ToActiveAnInactivePosition() {
		byte value = 2; // 00000010
		byte expected = 3; // 00000011
		byte actual = ByteInfo.modifyLSB(value, true);
		assertEquals(expected, actual);
	}

	@Test
	public void testModifyLSB_ToDesactiveAnActivePosition() {
		byte value = 3; // 00011111
		byte expected = 2; // 00000010
		byte actual = ByteInfo.modifyLSB(value, false);
		assertEquals(expected, actual);
	}

	@Test
	public void testModifyLSB_ToDesactiveAnInactivePosition() {
		byte expected = 2; // 00000010
		byte actual = ByteInfo.modifyLSB(expected, false);
		assertEquals(expected, actual);
	}

	@Test
	public void testIsActive_FirstPositionActive() {
		byte value = 31; // 00011111
		assertTrue(ByteInfo.isActive(value, 0));
	}

	@Test
	public void testIsActive_FirstPositionInactive() {
		byte value = 6; // 00000110
		assertFalse(ByteInfo.isActive(value, 0));
	}

	@Test
	public void testIsActive_FourthPositionActive() {
		byte value = 10; // 00001010
		assertTrue(ByteInfo.isActive(value, 3));
	}

	@Test
	public void testIsActive_FourthPositionInactive() {
		byte value = 23; // 00010111
		assertFalse(ByteInfo.isActive(value, 3));
	}

	@Test
	public void testIsActive_FullActive() {
		byte value = -1; // 11111111
		assertTrue(ByteInfo.isActive(value, 0));
		assertTrue(ByteInfo.isActive(value, 1));
		assertTrue(ByteInfo.isActive(value, 2));
		assertTrue(ByteInfo.isActive(value, 3));
		assertTrue(ByteInfo.isActive(value, 4));
		assertTrue(ByteInfo.isActive(value, 5));
		assertTrue(ByteInfo.isActive(value, 6));
		assertTrue(ByteInfo.isActive(value, 7));
	}

}
