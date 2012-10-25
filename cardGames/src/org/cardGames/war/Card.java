package org.cardGames.war;

/**
 * The playing card class
 * @author jeremy
 *
 */
public class Card implements Comparable<Card> {
	private int suit, rank;
	
	/**
	 * Constructor takes arbitrary number of suits and ranks
	 * @param suit
	 * @param rank
	 */
	public Card(int suit, int rank){
		this.suit = suit;
		this.rank = rank;
	}
	public int getSuit() {
		return suit;
	}
	public int getRank() {
		return rank;
	}
	
	/** Suit-neutral implementation of compareTo() 
	 * would want to override for games where suit
	 * is significant 
	 */
	public int compareTo(Card card1){
		return this.getRank() - card1.getRank();
	}

}
