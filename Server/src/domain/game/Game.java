package domain.game;

import com.google.gson.Gson;
import domain.enums.PlayerNumber;
import domain.interfaces.IToJson;

import java.util.concurrent.ThreadLocalRandom;

public class Game implements IToJson, Comparable
{
    private Player playerOne;
    private Player playerTwo;
    private PlayerNumber currentTurn;
    private boolean gameStarted;


    public boolean isGameStarted() { return gameStarted; }

    public Game() {
        gameStarted = false;
    }

    public void startGame()
    {
        playerOne.addCheckers(PlayerNumber.ONE);
        playerTwo.addCheckers(PlayerNumber.TWO);

        int randomNum = ThreadLocalRandom.current().nextInt(0,1);
        if (randomNum == 0)
            currentTurn = playerOne.getPlayerNumber();
        else
            currentTurn = playerTwo.getPlayerNumber();

        gameStarted = true;

        System.out.println("Game started.");
    }

    public boolean moveChecker(PlayerNumber playerNumber, int from, int to)
    {
        if (playerNumber != currentTurn)
            return false;

        switch (playerNumber)
        {
            case ONE:
                playerOne.moveChecker(from, to);
//                playerTwo.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.TWO;
                break;
            case TWO:
                playerTwo.moveChecker(from, to);
//                playerOne.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.ONE;
                break;
        }

        calculateAvailableMoves();

        return true;
    }

    private void calculateAvailableMoves()
    {
        playerOne.calculateAvailableMoves(playerTwo.getCheckers());
        playerTwo.calculateAvailableMoves(playerOne.getCheckers());
    }

    public boolean addPlayer(Player player) {
        if (playerOne == null)
        {
            playerOne = player;
            return true;
        }
        else if (playerTwo == null)
        {
            playerTwo = player;
            return true;
        }
        return false;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
