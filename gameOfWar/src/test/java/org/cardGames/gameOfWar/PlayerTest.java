package org.cardGames.gameOfWar;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

	// test the constructor; most player methods
	// are tested by way of WarGameTest.java
	@Test
	public void testPlayerConstructor() {
		Player testPlayer = new Player();
		assertTrue(testPlayer instanceof Player);
	}
}
