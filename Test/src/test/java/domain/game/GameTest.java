package domain.game;

import domain.game.checkers.Checker;
import domain.network.SocketConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private GameContainer gc;
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        gc = new GameContainer();
        player1 = new Player("speler 1");
        player2 = new Player("speler 2");
    }

    @Test
    void isGameStarted() {
        game = gc.findGame(player1, new SocketConnection(new Socket(), gc));
        game = gc.findGame(player2, new SocketConnection(new Socket(), gc));

        assertTrue(game.isGameStarted());
    }

    @Test
    void startGame() {
        game = gc.findGame(player1, new SocketConnection(new Socket(), gc));
        game = gc.findGame(player2, new SocketConnection(new Socket(), gc));

        assertTrue(game.isGameStarted());
    }

    @Test
    void moveChecker() {
        game = gc.findGame(player1, new SocketConnection(new Socket(), gc));
        game = gc.findGame(player2, new SocketConnection(new Socket(), gc));

        assertFalse(game.moveChecker(player1, 0, 1, 1, 1), "player is not the owner");
        assertFalse(game.moveChecker(player1, 9, 8, 6, 5), "destination location is not valid");
        assertTrue(game.moveChecker(player1, 6, 5, 5,4), "valid move");
    }

    @Test
    void addPlayer() {
        game = new Game();

        assertTrue(game.addPlayer(player1));
        assertTrue(game.addPlayer(player2));
        assertFalse(game.addPlayer(player1));
        assertFalse(game.addPlayer(player2));
    }

    @Test
    void getWinner() {
    }

    @Test
    void gameHasEnded() {
        game = gc.findGame(player1, new SocketConnection(new Socket(), gc));
        game = gc.findGame(player2, new SocketConnection(new Socket(), gc));

        assertFalse(game.gameHasEnded());
    }

    @Test
    void compareTo() {
        game = new Game();
        assertEquals(game.compareTo(new Object()), 0);
    }
}