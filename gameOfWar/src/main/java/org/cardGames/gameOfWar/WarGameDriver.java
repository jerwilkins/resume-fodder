package org.cardGames.gameOfWar;

/**
 * Created by IntelliJ IDEA.
 * User: jeremy
 * Date: 10/24/12
 * Time: 10:27 PM
 * Extracted this class from the WarGame class
 */
public class WarGameDriver {
	/**
	 * So we can do some War goodness from the command line
	 * @param args will be parsed, error-checked, and
	 * dry-cleaned
	 */
	public static void main(String[] args) {
		int suits, ranks, players;
		if (args.length != 3) {
			System.out
					.println("War should be played with the following options: <ranks> <suits> <players>. E.g.:");
			System.out.println("java -jar <War-Binary>.jar 4 13 2");
			return;
		}
		try{
			suits = Integer.parseInt(args[0]);
			ranks = Integer.parseInt(args[1]);
			players = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException e) {
			System.out.println("Arguments must be integers (and not insanely huge).");
			System.out.println("Usage example: java -jar war.jar 4 13 2");
			return;
		}
		WarGame.play(suits, ranks, players);
	}
}
