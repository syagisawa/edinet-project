package com.edinet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.edinet.app.controllers.CalculatorController;

public class CalculatorTest {

	@Test
	public void plusTest() {
		CalculatorController calc = new CalculatorController();
		int result = calc.plus(100, 1);
		assertEquals(result, 101);
	}
}
