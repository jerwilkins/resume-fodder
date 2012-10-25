package org.cardGames.war;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class WarGameTest {
	LinkedList<Player> players;
	LinkedList<LinkedList<Card>> piles;
	
	// Create some invalid games
	@Test 
	public void testInvalidGameDecks() {
		// deck unevenly divisible by players
		WarGame invalidWar1 = new WarGame(4, 13, 3);
		assertFalse(invalidWar1.isValidGame());
		
		// deck of insufficient size for a single war round
		WarGame invalidWar2 = new WarGame(4, 3, 4);
		assertFalse(invalidWar2.isValidGame());
	}
	
	// Ensure that WarDecks are set up predictably
	@Test
	public void testDeckSetup(){
		WarGame warTest1 = new WarGame(4, 13, 2);
		// testing of deck size is taken care of in CardDeckTest
		assertTrue(warTest1.isValidGame());
		
		// each player should have 26 cards
		assertEquals(2, warTest1.getPlayers().size());
		for(Player aPlayer : warTest1.getPlayers()){
			assertEquals(26, aPlayer.getOwnPile().size());
		}
	}

	// Test the game runner for a reduced, predictable deck
	@Test
	public void testWarGame(){
		int[][][] cardsToAdd = {
				{ {3,4}, {1,1}, {2,1}, {3,1}, {1,1}, {2,1}, {3,1}, {1,2}, {1,8}, {2,11} }, 
				{ {2,4}, {1,1}, {2,1}, {3,1}, {1,2}, {2,2}, {3,2}, {2,3}, {1,9}, {2,12} }
		};
		setupPlayersHands(cardsToAdd);
		WarGame testWar = new WarGame(4, 13, 2);
		testWar.setPlayers(this.players);
		testWar.getWarDeck().setInitialDeckSize(20);
		testWar.runWarGame();
		assertEquals(0, testWar.getPlayers().get(0).getOwnPile().size());
		assertEquals(20, testWar.getPlayers().get(1).getOwnPile().size());
		
	}
	
	// This deck will require some more iterations
	@Test
	public void testALessConvenientDeck(){
		int[][][] cardsToAdd = {
				{ {3,4}, {1,3}, {2,5}, {3,1}, {1,8} }, 
				{ {2,4}, {1,1}, {2,1}, {3,1}, {1,2} }
		};
		setupPlayersHands(cardsToAdd);
		WarGame testWar = new WarGame(4, 13, 2);
		testWar.setPlayers(this.players);
		testWar.getWarDeck().setInitialDeckSize(10);
		testWar.runWarGame();
		
		int deck1Size = testWar.getPlayers().get(0).getOwnPile().size();
		int deck2Size = testWar.getPlayers().get(1).getOwnPile().size();
		
		if(deck1Size > deck2Size) {
			assertEquals(10, deck1Size);
			assertEquals(0, deck2Size);
		} 
		else if(deck1Size < deck2Size){
			assertEquals(10, deck2Size);
			assertEquals(0, deck1Size);
		}
		else{
			fail("Deck was not fully allocated to one player.");
		}
	}
	
	// Let's see how we do with a three-person game
	@Test
	public void testThreePersonGame(){
		int[][][] cardsToAdd = {
				{ {3,4}, {1,4}, {2,4}, {3,4}, {1,12} }, 
				{ {2,4}, {1,8}, {2,8}, {3,8}, {1,10} }, 
				{ {2,4}, {1,12}, {2,12}, {3,12}, {1,8} }
		};
		setupPlayersHands(cardsToAdd);
		WarGame testWar = new WarGame(3, 13, 3);
		testWar.setPlayers(this.players);
		testWar.getWarDeck().setInitialDeckSize(15);
		testWar.runWarGame();
		
		// All games should resolve down to two players at the end
		int deck1Size = testWar.getPlayers().get(0).getOwnPile().size();
		int deck2Size = testWar.getPlayers().get(1).getOwnPile().size();
		if(deck1Size > deck2Size){
			assertEquals(15, deck1Size);
			assertEquals(0, deck2Size);
		}
		else if(deck2Size > deck1Size){
			assertEquals(15, deck2Size);
			assertEquals(0, deck1Size);
		}
		else {
			fail("Game did not complete properly.");
		}
	}

	/* Create a deck where one battle and two wars will occur; 
	 * cards from first battle & wars will go to player 2;
	 * cards from last battle will go to player 1
	 */
	@Test
	public void testBattle(){
		int[][][] cardsToAdd = {
				{ {3,4}, {1,1}, {2,1}, {3,1}, {1,1}, {2,1}, {3,1}, {1,2}, {1,8}, {2,11} }, 
				{ {2,4}, {1,1}, {2,1}, {3,1}, {1,2}, {2,2}, {3,2}, {2,3}, {1,9}, {2,12} }
		};
		setupPlayersHands(cardsToAdd);
		
		assertEquals(10, this.players.get(0)
				.getOwnPile().size());
		assertEquals(10, this.players.get(1)
				.getOwnPile().size());
		
		WarGame testWar = new WarGame(4, 13, 2);
		testWar.getWarDeck().setInitialDeckSize(20);
		testWar.setPlayers(this.players);
		// test cards possessed after one round of play
		testWar.battle(testWar.getPlayers(), 1);
		assertEquals(17, testWar.getPlayers().get(1).getOwnPile().size());
		assertEquals(3, testWar.getPlayers().get(0).getOwnPile().size());
	}
	
	
	// Method for creating throw-away piles for game testing
	public void setupPlayersHands(int[][][] cardsToAdd){
		this.players = new LinkedList<Player>();
		this.piles = new LinkedList<LinkedList<Card>>();
		
		/* Traverse the cardsToAdd array to create players, 
		 * their respective piles and cards {suit,rank}
		 */
		for(int i = 0; i < cardsToAdd.length; i++){
			this.players.add(new Player());
			this.piles.add(new LinkedList<Card>());
			for(int j = 0; j < cardsToAdd[0].length; j++){
				this.piles.get(i)
				.add(new Card(cardsToAdd[i][j][0], 
						cardsToAdd[i][j][1])); // rank and suit respectively 
			}
			this.players.get(i).addToOwnPile(this.piles.get(i));
		}
	}

	// test this problematic four-person game
	@Test
	public void testFourPersonGame(){
		WarGame testWar = new WarGame(4, 13, 4);
		testWar.runWarGame();
		
		assertTrue(testWar.isGameResolved() == true);
		assertTrue(testWar.getGameWinningPlayer() != null);
	}
	// And what the heck... :]
	@Test
	public void testElevenPersonGame(){
		WarGame testWar = new WarGame(8, 11, 11);
		testWar.runWarGame();
		
		assertTrue(testWar.isGameResolved() == true);
		assertTrue(testWar.getGameWinningPlayer() != null);
	}	
	
}
