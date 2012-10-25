package org.cardGames.gameOfWar;
import java.util.Collections;
import java.util.LinkedList;

public class DeckImpl implements Deck{

	private LinkedList<Card> cards;
	/** initial size of deck for reference after deck is dealt, etc. */
	private int initialDeckSize;
	
	/**
	 * 
	 * @param numberOfSuits
	 * @param numberOfRanks
	 */
	DeckImpl(int numberOfSuits, int numberOfRanks) {
		this.cards = new LinkedList<Card>();
		create(numberOfSuits, numberOfRanks);
		this.initialDeckSize = cards.size();
	}
	
	/**
	 * randomize our deck
	 */
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	/**
	 * simply iterates through ranks/suits to populate cards
	 */
	public void create(int numberOfSuits, int numberOfRanks) {
		for (int i = numberOfSuits; i>0; i--) {
			for (int j = numberOfRanks; j>0; j--) {
				Card card1 = new Card(i,j); 
				this.addCard(card1);
			}
		}
	}
	
	/**
	 * For testing decks; reset initial deck size with supplied integer
	 */
	public void setInitialDeckSize(int newInitialSize) {
		this.initialDeckSize = newInitialSize;
	}
	
	/**
	 * fetch card from top of stack; remove card from the deck
	 */
	public Card deal() {
		Card card = this.cards.get(0);
		this.cards.remove(card);
		return card;
	}

	/**
	 * 
	 * @return cards field
	 */
	public LinkedList<Card> getCards() {
		return cards;
	}
	
	/**
	 * 
	 * @param card1 card to add to the deck
	 */
	public void addCard(Card card1) {
		this.cards.add(card1);
	}

	/**
	 * 
	 * @param cards setter
	 */
	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}

	/**
	 * 
	 * @return initial size of card deck
	 */
	public int getInitialSize() {
		return this.initialDeckSize;
	}
	
	
	
}
