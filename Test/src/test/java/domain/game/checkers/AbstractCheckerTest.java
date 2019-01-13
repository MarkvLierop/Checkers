package domain.game.checkers;

import domain.enums.PlayerNumber;
import domain.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCheckerTest {
    private AbstractChecker checker;
    private Player player;
    private AbstractChecker[][] gameBoard;

    @BeforeEach
    void setUp() {
        player = new Player("speler 1");
        checker = new King(0,0, player);
        gameBoard = new AbstractChecker[10][10];
    }

    @Test
    void setX() {
        checker.setX(10);
        assertEquals(checker.getX(), 10);
    }

    @Test
    void setY() {
        checker.setY(10);
        assertEquals(checker.getY(), 10);
    }

    @Test
    void getOwner() {
        assertEquals(checker.getOwner(), player);
    }

    @Test
    void hasCheckersToHit() {
        checker.lookforCheckersToHit(gameBoard);
        assertFalse(checker.hasCheckersToHit());
    }

    @Test
    void opponentHasChecker() {
        Player player2 = new Player("speler 2");
        AbstractChecker checker2 = new Checker(1, 1, player2);
        gameBoard[1][1] = checker2;
        gameBoard[0][0] = checker;

        assertTrue(checker.opponentHasChecker(gameBoard, 1, 1));
        assertFalse(checker.opponentHasChecker(gameBoard, 1, 5));
        assertFalse(checker.opponentHasChecker(gameBoard, 0, 0));
    }

    @Test
    void currentPlayerHasChecker() {
        Player player2 = new Player("speler 2");
        player.setPlayerNumber(PlayerNumber.ONE);
        player2.setPlayerNumber(PlayerNumber.TWO);
        AbstractChecker checker2 = new Checker(1, 1, player2);
        gameBoard[1][1] = checker2;
        gameBoard[0][0] = checker;

        assertFalse(checker.currentPlayerHasChecker(gameBoard, 1, 1));
        assertFalse(checker.currentPlayerHasChecker(gameBoard, 1, 5));
        assertTrue(checker.currentPlayerHasChecker(gameBoard, 0, 0));
    }

    @Test
    void locationIsEmpty() {
        gameBoard[0][0] = checker;
        assertFalse(checker.locationIsEmpty(gameBoard, 0, 0));
        assertTrue(checker.locationIsEmpty(gameBoard, 0, 1));
    }

    @Test
    void locationIsValid() {
        assertTrue(checker.locationIsValid(0, 0));
        assertFalse(checker.locationIsValid(10, 9));
        assertFalse(checker.locationIsValid(10, 10));
        assertFalse(checker.locationIsValid(5, 10));
    }

    @Test
    void lookforCheckersToHit() {
    }

    @Test
    void calculateAvailableMoves() {
    }

    @Test
    void getHighestHit() {
    }

    @Test
    void removeLowHits() {
    }
}