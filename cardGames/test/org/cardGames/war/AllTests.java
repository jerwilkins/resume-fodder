package org.cardGames.war;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CardDeckTest.class, CardTest.class, WarGameTest.class, PlayerTest.class })
public class AllTests {

}
