package classes;

import enums.CheckerType;
import enums.PlayerNumber;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable
{
    private String username;
    private int wins;
    private int losses;
    private List<Checker> checkers;
    private Map<Checker, List<Integer>> availableMoves;
    private PlayerNumber playerNumber;

    public List<Checker> getCheckers()
    {
        return Collections.unmodifiableList(checkers);
    }
    public PlayerNumber getPlayerNumber()
    {
        return playerNumber;
    }
    public void setPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }
    public String getUsername() { return username; }
    public void setAvailableMoves(Map<Checker, List<Integer>> availableMoves)
    {
        this.availableMoves = availableMoves;
    }
    public Map<Checker, List<Integer>> getAvailableMoves()
    {
        return availableMoves;
    }

    public Player(String username)
    {
        this.username = username;

        availableMoves = new HashMap<>();
    }

    public void moveChecker(int from, int to)
    {
        for (Checker checker : checkers)
        {
            if (checker.getLocation() == from)
            {
                checker.setLocation(to);
                break;
            }
        }
//        System.out.println(playerNumber.toString() + " checker moved from " + from + " to " + to);
    }

    public boolean hasCheckerWithLocation(int location)
    {
        for (Checker checker : checkers)
        {
            if (checker.getLocation() == location)
            {
                return true;
            }
        }

        return false;
    }

    public void addCheckers(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
        checkers = new ArrayList<>();

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

    public boolean availablMovesContainsInt(int value)
    {
        boolean returnValue = false;

        for (List<Integer> set : availableMoves.values())
        {
            if (set.contains(value))
                returnValue = true;
        }

        return returnValue;
    }

    public void removeCheckerIfExists(int location) {
        Checker c = null;
        for (Checker checker : checkers)
        {
            if (checker.getLocation() == location)
            {
                c = checker;
            }
        }

        if (c != null)
            checkers.remove(c);
    }
    private void addChecker(int x, int y)
    {
        for (int i = x; i < x + 4; i++)
        {
            if (x == i && Integer.parseInt(x + "" + y) % 2 != i % 2)
            {
                Checker checker = new Checker();
                checker.setCheckerType(CheckerType.CHECKER);
                checker.setLocation(Integer.parseInt(x + "" + y));
                checkers.add(checker);
            }
        }
    }
}
