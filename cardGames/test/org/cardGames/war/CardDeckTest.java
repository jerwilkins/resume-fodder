package org.cardGames.war;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardDeckTest {
	CardDeck deck1;
	
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
		this.deck1 = new CardDeck(4,13);
	}
	

}
