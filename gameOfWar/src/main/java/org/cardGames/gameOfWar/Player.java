package org.cardGames.gameOfWar;

import java.util.LinkedList;

/**
 * Players for card games
 * @author jeremy
 *
 */
public class Player {
	private LinkedList<Card> ownPile, cardsInJeopardy, cardsInPlay;
	private Card cardInBattle;
	private int playerNumber = 0;
	
	/**
	 * 
	 * @return player number
	 */
	public int getPlayerNumber() {
		return this.playerNumber;
	}

	/**
	 * 
	 * @param playerNumber setter
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	/**
	 * Constructor just creates empty, content-specific LinkedLists
	 */
	public Player() {
		ownPile = new LinkedList<Card>();
		cardsInJeopardy = new LinkedList<Card>();
		cardsInPlay = new LinkedList<Card>();
	}

	/**
	 * 
	 * @return player's own pile (cards in hand)
	 */
	public LinkedList<Card> getOwnPile() {
		return ownPile;
	}
	
	/**
	 * Set player's own pile (cards in hand)
	 * @param ownPile
	 */
	public void setOwnPile(LinkedList<Card> ownPile) {
		this.ownPile = ownPile;
	}

	/**
	 * appends cards to player's play pile
	 * @param cards
	 */
	void addToOwnPile(LinkedList<Card> cards){
		this.ownPile.addAll(cards);
	}

	/**
	 * Cards in jeopardy are threatened by the outcome of the round
	 * @return
	 */
	public LinkedList<Card> getCardsInJeopardy() {
		return cardsInJeopardy;
	}

	/**
	 * Cards in jeopardy are threatened by the outcome of the round
	 * @param cardsInJeopardy
	 */
	public void setCardsInJeopardy(LinkedList<Card> cardsInJeopardy) {
		this.cardsInJeopardy = cardsInJeopardy;
	}

	/**
	 * Cards currently in play (i.e. face-down in war)
	 * @return
	 */
	public LinkedList<Card> getCardsInPlay() {
		return cardsInPlay;
	}

	/**
	 * Cards currently in play (i.e. face-down in war)
	 * @param cardsInPlay
	 */
	public void setCardsInPlay(LinkedList<Card> cardsInPlay) {
		this.cardsInPlay = cardsInPlay;
	}

	/**
	 * A (randomized) card from the cards in play
	 * @return
	 */
	public Card getCardInBattle() {
		return cardInBattle;
	}

	/**
	 * A (randomized) card from the cards in play
	 * @param cardInBattle
	 */
	public void setCardInBattle(Card cardInBattle) {
		this.cardInBattle = cardInBattle;
	}

}
