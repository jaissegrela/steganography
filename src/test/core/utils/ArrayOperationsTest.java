package test.core.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import core.utils.ArrayOperations;

public class ArrayOperationsTest {

	@Test
	public void testCompact_AllValueAreEquals() {
		List<byte[]> temp = new ArrayList<byte[]>();
		temp.add(new byte[]{16,63});
		temp.add(new byte[]{16,63});
		temp.add(new byte[]{16,63});
		byte[] actual = ArrayOperations.compact(temp);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals(16, actual[0]);
		Assert.assertEquals(63, actual[1]);
	}
	
	@Test
	public void testCompact1() {
		List<byte[]> temp = new ArrayList<byte[]>();
		temp.add(new byte[]{16,63});
		temp.add(new byte[]{17,63});
		temp.add(new byte[]{17,64});
		byte[] actual = ArrayOperations.compact(temp);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals(17, actual[0]);
		Assert.assertEquals(63, actual[1]);
	}
	
	@Test
	public void testCompact2() {
		List<byte[]> temp = new ArrayList<byte[]>();
		temp.add(new byte[]{16,63});
		temp.add(new byte[]{1,112});
		temp.add(new byte[]{19,3});
		byte[] actual = ArrayOperations.compact(temp);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals(17, actual[0]);
		Assert.assertEquals(51, actual[1]);
	}
	
	@Test
	public void testCompact3() {
		List<byte[]> temp = new ArrayList<byte[]>();
		temp.add(new byte[]{-128, 38});
		temp.add(new byte[]{85, 41});
		temp.add(new byte[]{95, 17});
		temp.add(new byte[]{84, 11});
		byte[] actual = ArrayOperations.compact(temp);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals(85, actual[0]);
		Assert.assertEquals(43, actual[1]);
	}

}
