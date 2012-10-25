package org.cardGames.gameOfWar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DeckImplTest.class, CardTest.class, WarGameTest.class, PlayerTest.class })
public class AllTests {

}
