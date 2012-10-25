package org.cardGames.gameOfWar;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeckImplTest {
	DeckImpl deck1;
	
	@Test // Constructor tests the addCard method inline
	public void testConstructDeck() {
		setup();
		assertEquals(52, this.deck1.getCards().size());
	}
	
	@Test // Deal should give us a single card
	public void testDeal(){
		setup();
		assertTrue(this.deck1.deal() instanceof Card);
	}
	
	public void setup(){
		this.deck1 = new DeckImpl(4,13);
	}
	

}
