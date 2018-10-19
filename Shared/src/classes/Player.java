package classes;

import enums.PlayerNumber;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Player implements Serializable
{
    private String username;
    private int wins;
    private int losses;
    private Map<Integer, Checker> checkers;
    private PlayerNumber playerNumber;

    public Player(String username)
    {
        this.username = username;
    }

    public Map<Integer, Checker> getCheckers()
    {
        return Collections.unmodifiableMap(checkers);
    }

    public PlayerNumber getPlayerNumber()
    {
        return playerNumber;
    }
    public void setPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }

    public synchronized void moveChecker(int from, int to)
    {
        checkers.put(to, checkers.get(from));
        checkers.remove(from);
        System.out.println("moved from " + from + " to " + to);
    }

    public void addCheckers(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
        checkers = new TreeMap<>();

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
                checkers.put(Integer.parseInt(x + "" + y), new Checker());
                System.out.println(username + " - "+ x + "" + y);
            }
        }
    }
}
