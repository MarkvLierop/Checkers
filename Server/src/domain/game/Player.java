package domain.game;

import com.google.gson.Gson;
import domain.enums.Operator;
import domain.game.checkers.AbstractChecker;
import domain.enums.PlayerNumber;

import java.util.*;

public class Player
{
    private String username;
    private PlayerNumber playerNumber;

    public Player(String username)
    {
        this.username = username;
    }

    public void setPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }
    public String getUsername()
    {
        return username;
    }
    public PlayerNumber getPlayerNumber()
    {
        return playerNumber;
    }

    public void calculateMoves(AbstractChecker[][] gameBoard)
    {
        Set<AbstractChecker> checkers = new HashSet<>();
        boolean obligatedToHit = calculateHits(gameBoard, checkers);

        if (obligatedToHit)
            keepHighestHits(checkers);
        else {
            for (AbstractChecker checker : checkers)
            {
                checker.calculateAvailableMoves(gameBoard, playerNumber == PlayerNumber.ONE ? Operator.SUBTRACTION : Operator.ADDITION);
            }
        }
    }

    private boolean calculateHits(AbstractChecker[][] gameBoard, Set<AbstractChecker> checkers)
    {
        for (AbstractChecker[] line : gameBoard)
        {
            for (AbstractChecker checker : line)
            {
                if (checker != null && checker.getOwner() == this){
                    checker.lookforCheckersToHit(gameBoard);
                    checkers.add(checker);

                    if (checker.hasCheckersToHit())
                        return true;
                }
            }
        }

        return false;
    }

    private void keepHighestHits(Set<AbstractChecker> checkers)
    {
        int highestHit = 0;

        for (AbstractChecker checker : checkers)
        {
            int hit = checker.getHighestHit();
            if (hit > highestHit)
                highestHit = hit;
        }

        for (AbstractChecker checker : checkers)
        {
            checker.removeLowHits(highestHit);
        }
    }
}
