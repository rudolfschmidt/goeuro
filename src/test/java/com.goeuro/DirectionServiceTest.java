package com.goeuro;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DirectionServiceTest {

	private final DirectionService directionService = new DirectionService();

	@Test
	public void yes() {
		assertTrue(directionService.isConnected("1 2 3 4", 2, 3));
		assertTrue(directionService.isConnected("1 2 3 4", 3, 4));
		assertTrue(directionService.isConnected("1 2 3 4", 2, 4));
	}

	@Test
	public void no() {
		assertFalse(directionService.isConnected("1 2 3 4", 1, 2));
		assertFalse(directionService.isConnected("1 2 3 4", 1, 4));
		assertFalse(directionService.isConnected("1 2 3 4", 2, 1));
		assertFalse(directionService.isConnected("1 2 3 4", 3, 1));
		assertFalse(directionService.isConnected("1 2 3 4", 3, 2));
		assertFalse(directionService.isConnected("1 2 3 4", 4, 3));
		assertFalse(directionService.isConnected("1 2 3 4", 4, 2));
		assertFalse(directionService.isConnected("1 2 3 4", 4, 1));
	}
}
