package test.core.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import core.utils.Utils;

public class UtilsTest {

	@Test
	public void testGetExtensionWithOnlyDotCharacter() {
		String expected = "extension";
		File file = new File("filename." + expected);
		String actual = Utils.getExtension(file);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetExtensionWithTwoDotCharacter() {
		String expected = "extension";
		File file = new File("file.name." + expected);
		String actual = Utils.getExtension(file);
		assertEquals(expected, actual);
	}

}
