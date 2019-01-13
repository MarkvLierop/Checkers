package domain.game;

import domain.network.SocketConnection;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class GameContainerTest {

    @Test
    void findGame() {
        GameContainer gc = new GameContainer();
        Player player1 = new Player("Speler 1");
        Game game1 = gc.findGame(player1, new SocketConnection(new Socket(), gc));

        Assert.assertNotNull("game 1 not null", game1);
        Assert.assertFalse("game 1 is started is false", game1.isGameStarted());

        Player player2 = new Player("Speler 2");
        Game game2 = gc.findGame(player2, new SocketConnection(new Socket(), gc));

        Assert.assertNotNull("game 2 not null", game2);
        Assert.assertTrue("game 2 is started is false",game2.isGameStarted());
    }
}