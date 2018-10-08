package classes;

import enums.PlayerNumber;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Player
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

    public void moveChecker(int from, int to)
    {
        checkers.put(to, checkers.get(from));
        checkers.remove(from);
        System.out.println("moved");
    }

    public void addCheckers(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
        checkers = new TreeMap<>();

        for (int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if (playerNumber == PlayerNumber.ONE && x <= 4)
                {
                    // TODO: REFACTORING
                    if (x == 0 && Integer.parseInt(x + "" + y) % 2 == 1)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 1 && Integer.parseInt(x + "" + y) % 2 == 0)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 2 && Integer.parseInt(x + "" + y) % 2 == 1)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 3 && Integer.parseInt(x + "" + y) % 2 == 0)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                }
                else if (playerNumber == PlayerNumber.TWO && x >= 6)
                {
                    if (x == 6 && Integer.parseInt(x + "" + y) % 2 == 1)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 7 && Integer.parseInt(x + "" + y) % 2 == 0)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 8 && Integer.parseInt(x + "" + y) % 2 == 1)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                    if (x == 9 && Integer.parseInt(x + "" + y) % 2 == 0)
                    {
                        checkers.put(Integer.parseInt(x + "" + y), new Checker());
                        System.out.println(username + " - "+ x + "" + y);
                    }
                }
            }
        }
    }
}
