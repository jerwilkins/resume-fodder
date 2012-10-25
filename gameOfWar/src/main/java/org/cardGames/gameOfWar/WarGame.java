package org.cardGames.gameOfWar;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;

/** WarGame applies the rules of the game of War for 
 * an n-suits, n-ranks, n-player game!
 *  
 * @author jeremy
 * 
 */
public class WarGame{
	
	/** warDeck Incorporates the standard playing deck */
	private DeckImpl warDeck;

	/** Cards that have been lost by a player, but not yet picked up
	 * by another player
	 */
	private LinkedList<Card> cardsInLimbo = new LinkedList<Card>();
	/** All players in game */
	private LinkedList<Player> players;
	private LinkedList<Player> removedPlayers = new LinkedList<Player>();
	/** Winning player */
	private Player gameWinningPlayer = null;
	
	/** minimum number of cards/player for a valid game */
	private final int cardsNeededPerPlayer = 4;
	/** number of cards used when war is engaged */
	private final int warHandSize = 3;	
	/** Number of one-card battles fought */
	private int battlesFought = 0;
	/** Number of n-card Wars fought where n = warHandSize */
	private int warsFought = 0;
	 
	/** The lowest card for empty-filling and battle-outcome purposes */
	private Card floorCard = new Card(0,0);
	
	/** game fits criteria of deck size, etc. */
	private boolean validGame = true;
	/** game is resolved when one player has collected all cards */
	private boolean gameResolved = false;
	private int numberOfPlayers;
	

	/** Constructor incorporates some basic validation, sets up players & shuffles deck */
	WarGame( int numberOfSuits, int numberOfRanks, int numberOfPlayers ) {
	
		warDeck = new DeckImpl(numberOfSuits, numberOfRanks);
		this.numberOfPlayers = numberOfPlayers;
		
		// cards must be divisible by players
		if(warDeck.getCards().size() % numberOfPlayers != 0){
			System.out
					.println("Cards must be equally divisible by number of players.");
			this.validGame = false;
		}
		else if(warDeck.getCards().size() / numberOfPlayers < 4) {
			System.out.println("A deck must have at least "
					+ cardsNeededPerPlayer + " to play a round of War.");
			this.validGame = false;
		}
		else { // Setup game after ensuring valid for play
			this.warDeck.shuffle();
			this.setupPlayers();
			this.setupPiles();
		}
	}

	
	/** Doles out an equal pile for each player */
	private void setupPiles() {
		int initialPileSize = this.warDeck.getCards().size() / this.numberOfPlayers;
		
		/* I considered doing this with subList() (more efficient), but realized
		 * that's not really how a dealer deals, and that might lead
		 * to some weird bugs if the classes were re-purposed for another game
		 * or, you know, card tricks or something 
		 */
		for(int i = initialPileSize; i > 0; i--){
			Iterator<Player> playerIterator = this.players.iterator();
			while(playerIterator.hasNext()){
				LinkedList<Card> card = new LinkedList<Card>();
				card.add(this.warDeck.deal());
				playerIterator.next().addToOwnPile(card);
			}
		}
	}

	/** tiny method to incorporate requisite interface 
	 * can be called statically to create and play 
	 * a game of War
	 */
	public static void play( int numberOfSuits, int numberOfRanks, int numberOfPlayers ) {
		WarGame warGame = new WarGame(numberOfSuits, numberOfRanks, numberOfPlayers);
		warGame.runWarGame();
    }
	
	/**
	 * Method runs war battles, provided the game is valid
	 * and there are still more than one players with cards
	 */
	public void runWarGame(){
		if(this.isValidGame()){
			while(!this.isGameResolved()){
				this.battle(this.players, 1);
				this.battlesFought++;
			}
			System.out.println("Game complete.");
			resolveGameWinningPlayer();
			if(this.gameWinningPlayer == null){
				System.out.println("No winning player; weird!");
				System.out.println("Game ended with "
						+ this.getAllCardsInGame().size());
			}
			else {
			System.out.println("Player " + this.gameWinningPlayer.getPlayerNumber()
					+ " won the game.");
			System.out.println(this.battlesFought + " battles were fought; "
					+ this.warsFought + " rounds of War were fought. \nDeck was " 
					+ this.warDeck.getInitialSize() + " cards.");
			}
		} else {
			System.out.println("Exiting; invalid game.");
		}
	}
	
	/**
	 * At the end of the day, only the winning player will have all cards
	 * @return winningPlayer
	 */
	private void resolveGameWinningPlayer() {
		Iterator<Player> playerIterator = this.players.iterator();
		while(playerIterator.hasNext()){
			Player player = playerIterator.next();
			if(player.getOwnPile().size() == this.warDeck.getInitialSize()){
				this.gameWinningPlayer = player;
			}
		}
	}

	
	/**
	 * @return - LinkedList<Player> with all players
	 */
	public LinkedList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Allows setting players for test coverage
	 * @param players
	 */
	public void setPlayers(LinkedList<Player> players) {
		this.players = players;
	}

	/**
	 * @return true if there are fewer than two players with cards
	 */
	public boolean isGameResolved() {
		int activePlayers = 0;
		for (Player player : this.players){
			if (player.getOwnPile().size() > 0){
				activePlayers++;
			}
		}
		gameResolved = activePlayers < 2 ? true : false;
		return gameResolved;
	}

	/**
	 * 
	 * @return true if game is set up according to validation rules
	 */
	public boolean isValidGame() {
		return validGame;
	}
	
	/**
	 * Creates designated number of player objects
	 */
	private void setupPlayers() {
		this.players = new LinkedList<Player>();
		for(int i = 0; i < this.numberOfPlayers; i++){
			Player player = new Player();
			player.setPlayerNumber(i+1);
			this.players.add(player);
		}
	}

	/**
	 * Executes either one-card battles or full warHandSized wars.
	 * Called recursively when ties are encountered until a winner is determined
	 * at which point all cards in dispute will be moved to the winner's hand
	 * 
	 * @param playersThisBattle LinkedList of Players participating in this battle iteration
	 * @param battleSize Integer determining number of cards to lay out against one another
	 */
	void battle(LinkedList<Player> playersThisBattle, int battleSize) {
		// sets up and populates players with appropriate card hands
		designateAppropriatePlayers(playersThisBattle, battleSize);
		Player winningPlayer = null;
		// players tied for round
		LinkedList<Player> tiedPlayers = new LinkedList<Player>();
		
		winningPlayer = determineBattleWinner(playersThisBattle, tiedPlayers);
		if(winningPlayer != null) {
			giveWinnerCards(playersThisBattle, winningPlayer);
		}
		// if there is a tie, battle is called recursively
		// unless one of the players has no remaining cards
		// in which case, those cards will go to the other player
		else if(tiedPlayers.size() > 0){
			manageTie(tiedPlayers);
		}
	}

	
	/**
	 * 
	 * @param playersThisBattle
	 * @param battleSize
	 */
	private void designateAppropriatePlayers(LinkedList<Player> playersThisBattle,
			int battleSize) {
		Iterator<Player> playerIterator = playersThisBattle.iterator();
		while(playerIterator.hasNext()){
			Player player = playerIterator.next();
			// remove players with no remaining cards; 
			// add them to the removedPlayers collection for reference
			if(player.getOwnPile().size() == 0){
				this.cardsInLimbo.addAll(player.getCardsInJeopardy());
				player.getCardsInJeopardy().clear();
				this.removedPlayers.add(player);
				playerIterator.remove();
				continue;
			}
			assignPlayerBattleCards(player, battleSize);
		}
	}
	
	

	/**
	 * 
	 * @param player
	 * @param battleSize
	 */
	private void assignPlayerBattleCards(Player player, int battleSize) {
		LinkedList<Card> cardsInPlay, cardsInJeopardy;
		player.setCardInBattle(floorCard);
		// standard battle yields size-one elements
		if(battleSize == 1){
			cardsInJeopardy = new LinkedList<Card>();
			cardsInPlay = new LinkedList<Card>();
			if(!player.getOwnPile().isEmpty()){
				cardsInJeopardy.add(player.getOwnPile().get(0));
				cardsInPlay.add(player.getOwnPile().get(0));
			}
		} else {
			/* 
			 * in the case of player card piles less than 
			 * the War battle size, just use their full pile
			 * 
			 */
			if(player.getOwnPile().size() < battleSize) {
				cardsInJeopardy = new LinkedList<Card>(player.getOwnPile());
				cardsInPlay =  new LinkedList<Card>(player.getOwnPile());
			}
			else {
				cardsInJeopardy = new LinkedList<Card>(player.getOwnPile().subList(0, battleSize));
				cardsInPlay =  new LinkedList<Card>(player.getOwnPile().subList(0, battleSize));
			}
		}
		player.getCardsInJeopardy().addAll(cardsInJeopardy);
		// turn over a random card of the three
		Collections.shuffle(cardsInPlay);
		player.setCardsInPlay(cardsInPlay);
		// Cards in jeopardy are no longer in player's pile
		player.getOwnPile().removeAll(cardsInJeopardy);
	}
	
	
	
	/**
	 * 
	 * @param playersThisBattle
	 * @param tiedPlayers
	 * @return
	 */
	private Player determineBattleWinner(LinkedList<Player> playersThisBattle, LinkedList<Player> tiedPlayers) {
		// The current strongest card in an n-way battle
		Card battleWinningCard = floorCard;
		Player winningPlayer = null;
		Iterator<Player> playerIterator = playersThisBattle.iterator();
		while(playerIterator.hasNext()){
			Player player = playerIterator.next();

			player.setCardInBattle(player.getCardsInPlay().get(0));
			
			// if player's cardInBattle exceeds current highest card
			// player is set winner
			if(player.getCardInBattle().compareTo(battleWinningCard) > 0){
				winningPlayer = player;
				if(winningPlayer != null){
					winningPlayer = null;
				}
				winningPlayer = player;
				battleWinningCard = player.getCardInBattle();
				Iterator<Player> tiedPlayersIterator = tiedPlayers.iterator();
				while(tiedPlayersIterator.hasNext()){
					Player tiedPlayer = tiedPlayersIterator.next();
					this.cardsInLimbo.addAll(tiedPlayer.getCardsInJeopardy());
					tiedPlayer.getCardsInJeopardy().clear();
					tiedPlayersIterator.remove();
				}
			}
			// establish a tie; unwin our presumptive players, if any
			else if(player.getCardInBattle().compareTo(battleWinningCard) == 0){
				if(winningPlayer != null){
					tiedPlayers.add(winningPlayer);
					winningPlayer = null;
				}
				winningPlayer = null;
				tiedPlayers.add(player);
				Iterator<Player> tiedPlayersIterator = tiedPlayers.iterator();
				// Ensure that if there are tied players from before,
				// they remain tied against the new battleWinningCard
				while(tiedPlayersIterator.hasNext()){
					Player tiedPlayer = tiedPlayersIterator.next();
					if(tiedPlayer.getCardInBattle().compareTo(battleWinningCard) < 0){
						this.cardsInLimbo.addAll(tiedPlayer.getCardsInJeopardy());
						tiedPlayer.getCardsInJeopardy().clear();
						tiedPlayersIterator.remove();
					}
				}
				
			} 
			// if player loses, send all their cards to limbo
			else {
				this.cardsInLimbo.addAll(player.getCardsInJeopardy());
				player.getCardsInJeopardy().clear();
				player.getCardsInPlay().clear();
			}
		}
		return winningPlayer;
	}

	/**
	 * Debugging & testing method
	 * @return all cards currently in game
	 */
	public LinkedList<Card> getAllCardsInGame() { 
		LinkedList<Card> allCards = new LinkedList<Card>();
		for(Player onePlayer : this.players){
			allCards.addAll(onePlayer.getOwnPile());
			allCards.addAll(onePlayer.getCardsInJeopardy());
		}
		allCards.addAll(this.cardsInLimbo);
		return allCards;
	}
	
	
	/**
	 * 
	 * @param playersThisBattle
	 * @param winningPlayer
	 */
	private void giveWinnerCards(LinkedList<Player> playersThisBattle, Player winningPlayer) {
		// if there is a winning player, battle is resolved
		if(winningPlayer != null){
			if(this.cardsInLimbo != null){
				winningPlayer.addToOwnPile(this.cardsInLimbo);
				this.cardsInLimbo.clear();
			}
			Iterator<Player> playerIterator = playersThisBattle.iterator();
			// Give the winning player the cards from all losing players this battle/war
			while(playerIterator.hasNext()){
				LinkedList<Card> giveToWinner = new LinkedList<Card>();
				Player player = playerIterator.next();
				giveToWinner = player.getCardsInJeopardy();
				winningPlayer.addToOwnPile(giveToWinner);
				player.getCardsInJeopardy().clear();
				player.getCardsInPlay().clear();
			}
		} 
	}


	/**
	 * 
	 * @param tiedPlayers
	 */
	private void manageTie(LinkedList<Player> tiedPlayers) {
		Iterator<Player> playerIterator = tiedPlayers.iterator();
		while(playerIterator.hasNext()){
			Player tiedPlayer = playerIterator.next();
			this.cardsInLimbo.addAll(tiedPlayer.getCardsInJeopardy()); 
			tiedPlayer.getCardsInJeopardy().clear();
			tiedPlayer.getCardsInPlay().clear();
			// ensures that ties only execute when all tied players
			// have additional cards to play; otherwise, the player
			// with cards takes all cards in the limbo stack
			if(tiedPlayer.getOwnPile().size() == 0){
				playerIterator.remove();
			}
		}
		if(tiedPlayers.size() > 1){
			battle(tiedPlayers, this.warHandSize);
			this.warsFought++;
		}
		else if(tiedPlayers.size() == 1){
			tiedPlayers.get(0).addToOwnPile(this.cardsInLimbo);
			this.cardsInLimbo.clear();
		}
		// A peculiar instance where no tied player has any further cards
		// at this point, we allow those cards to continue living in limbo
		// TODO: There is the remote possibility that *all* cards will be
		// tied up in a tied battle between the last two players
		// at this point, the game will resolve, but note that there was
		// no winning player
	}

	/**
	 * 
	 * @return number of 1-card battles fought
	 */
	public int getBattlesFought() {
		return battlesFought;
	}

	/**
	 * 
	 * @return number of n-card War-rounds fought
	 */
	public int getWarsFought() {
		return warsFought;
	}
	
	

	/**
	 * another method wholly for test coverage
	 * @param cards
	 */
	public void setWarTestDeck(LinkedList<Card> cards){
		this.warDeck.setCards(cards);
	}

	/**
	 * Getter for testing
	 * @return
	 */
	public DeckImpl getWarDeck() {
		return warDeck;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * 
	 * @param numberOfPlayers
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * 
	 * @return
	 */
	public Player getGameWinningPlayer() {
		return gameWinningPlayer;
	}

	
}
