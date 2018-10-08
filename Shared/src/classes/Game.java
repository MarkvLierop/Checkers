package classes;

import enums.PlayerNumber;
import java.io.Serializable;

public class Game implements Serializable
{
    private Player playerOne;
    private Player playerTwo;

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public boolean startGame()
    {
        if (playerOne == null)
            return false;
        if (playerTwo == null)
            return false;

        playerOne.addCheckers(PlayerNumber.ONE);
        playerTwo.addCheckers(PlayerNumber.TWO);

        return true;
    }

    public void moveChecker(PlayerNumber playerNumber, int from, int to)
    {
        switch (playerNumber)
        {
            case ONE:
                playerOne.moveChecker(from, to);
                break;
            case TWO:
                playerTwo.moveChecker(from, to);
                break;
        }
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
}
