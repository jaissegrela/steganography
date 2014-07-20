package test.core.attack;

import static org.junit.Assert.*;

import org.junit.Test;

import test.core.message.DummyMessage;

import core.attack.ChiSquare;

public class ChiSquareTest {

	@Test
	public void testCalculate_ExpecetedAndObservedAreEquals() {
		byte[] values = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				16, 17, 18, 19, 20 };
		ChiSquare attack = new ChiSquare();
		double actual = attack.calculate(new DummyMessage(values));
		// double expected = 0;
		double expected = 1;
		assertEquals(expected, actual, .01);
	}

	@Test
	public void testCalculate_ExpecetedAndObservedAreTotalyDifferent() {
		byte[] values = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		ChiSquare attack = new ChiSquare();
		double actual = attack.calculate(new DummyMessage(values));
		// double expected = 8.634;
		double expected = 1;
		assertEquals(expected, actual, .01);
	}

}
