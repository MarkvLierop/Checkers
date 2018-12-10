package domain.game;

import com.google.gson.Gson;
import domain.game.checkers.AbstractChecker;
import domain.game.checkers.Checker;
import domain.enums.PlayerNumber;
import domain.game.checkers.King;
import domain.interfaces.IToJson;

import java.util.*;

public class Player implements IToJson
{
    private String username;
    private int wins;
    private int losses;
    private Set<AbstractChecker> checkers;
    private PlayerNumber playerNumber;

    public Player(String username)
    {
        this.username = username;
    }

    public Set<AbstractChecker> getCheckers()
    {
        return checkers;
    }
    public PlayerNumber getPlayerNumber()
    {
        return playerNumber;
    }

    public void moveChecker(int from, int to)
    {
        for (AbstractChecker checker : checkers)
        {
            if (checker.getLocation() == from)
            {
                checker.setLocation(to);
                checker = upgradeChecker(checker);
                break;
            }
        }
    }

    public void calculateAvailableMoves(Set<AbstractChecker> checkersOpponent)
    {
        for (AbstractChecker checker : checkers)
        {
            checker.calculateAvailableMoves(checkers, checkersOpponent);
        }

        keepHighestHits();
    }

    public void addCheckers(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
        checkers = new HashSet<>();

        for (int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if (playerNumber == PlayerNumber.ONE && x <= 3)
                {
                    addChecker(x, y);
                }
                else if (playerNumber == PlayerNumber.TWO && x >= 6)
                {
                    addChecker(x, y);
                }
            }
        }
    }

    private void addChecker(int x, int y)
    {
        for (int i = x; i < x + 4; i++)
        {
            if (x == i && Integer.parseInt(x + "" + y) % 2 != i % 2)
            {
                AbstractChecker checker = new Checker(Integer.parseInt(x + "" + y));
                checkers.add(checker);
            }
        }
    }

    private void keepHighestHits()
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

    private AbstractChecker upgradeChecker(AbstractChecker checker)
    {
        if ((checker.getLocation() < 10 && playerNumber == PlayerNumber.TWO) ||
                (checker.getLocation() > 90 && playerNumber == PlayerNumber.ONE) && checker instanceof Checker)
            return new King(checker.getLocation());
        return checker;
    }
    public void removeCheckerIfExists(int location) {
        AbstractChecker c = null;
        for (AbstractChecker checker : checkers)
        {
            if (checker.getLocation() == location)
            {
                c = checker;
                break;
            }
        }

        if (c != null)
            checkers.remove(c);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
