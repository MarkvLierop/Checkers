package domain.game;

import domain.enums.PlayerNumber;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @Before
    void before()
    {
        player = new Player("speler 1");
    }

    @Test
    void setPlayerNumber() {
        player = new Player("speler 1");
        player.setPlayerNumber(PlayerNumber.ONE);
        assertEquals(player.getPlayerNumber(), PlayerNumber.ONE);

        player.setPlayerNumber(PlayerNumber.TWO);
        assertEquals(player.getPlayerNumber(), PlayerNumber.TWO);
    }

    @Test
    void getUsername() {
        String name = "speler 1";
        player = new Player(name);
        assertEquals("speler 1", name);
    }

    @Test
    void getPlayerNumber() {
        player = new Player("speler 1");
        player.setPlayerNumber(PlayerNumber.ONE);
        assertEquals(player.getPlayerNumber(), PlayerNumber.ONE);
    }

    @Test
    void calculateAvailableMoves() {
    }
}