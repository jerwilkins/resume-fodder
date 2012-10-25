package org.cardGames.war;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

	@Test
	public void testCard() {
		// Testing constructor for new card (suit, rank)
		Card card1 = new Card(1,4);
		assertEquals(1, card1.getSuit());
		assertEquals(4, card1.getRank());

		// Implementation of Comparable
		Card card2 = new Card(2,12);
		assertTrue(card1.compareTo(card2) == -8);
	}

}
