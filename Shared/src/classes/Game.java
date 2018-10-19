package classes;

import enums.Operator;
import enums.PlayerNumber;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game implements Serializable, Comparable
{
    private Player playerOne;
    private Player playerTwo;

    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }


    private final int NINE = 9;
    private final int ELEVEN = 11;

    public Game() {

    }

    public boolean startGame()
    {
        if (playerOne == null)
            return false;
        if (playerTwo == null)
            return false;

//        playerOne.addCheckers(PlayerNumber.ONE);
//        playerTwo.addCheckers(PlayerNumber.TWO);

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

    public Set<Integer> getAvailableMoves(PlayerNumber playerNumber)
    {
        Set<Integer> availableMoves = new HashSet<>();

        switch (playerNumber)
        {
            case ONE:
                for (Map.Entry<Integer, Checker> pair : playerOne.getCheckers().entrySet())
                {
                    if (!playerTwo.getCheckers().containsKey(pair.getKey() + NINE)){
                        availableMoves.add(pair.getKey() + NINE);
                    }
                    if (!playerTwo.getCheckers().containsKey(pair.getKey() + ELEVEN)){
                        availableMoves.add(pair.getKey() + ELEVEN);
                    }
                }
                break;
            case TWO:
                for (Map.Entry<Integer, Checker> pair : playerTwo.getCheckers().entrySet())
                {
                    if (!playerOne.getCheckers().containsKey(pair.getKey() - NINE)){
                        availableMoves.add(pair.getKey() - NINE);
                    }
                    if (!playerOne.getCheckers().containsKey(pair.getKey() - ELEVEN)){
                        availableMoves.add(pair.getKey() - ELEVEN);
                    }
                }
                break;
        }

        return availableMoves;
    }

    public Set<Set<Integer>> getPossibleHits(PlayerNumber playerNumber)
    {
        Set<Set<Integer>> availableHitsSet = new HashSet<>();

        switch (playerNumber)
        {
            case ONE:
                for (Map.Entry<Integer, Checker> pair : playerOne.getCheckers().entrySet())
                {
                    Set<Integer> availableHits = new HashSet<>();
                    checkForChecker(availableHits, playerTwo, pair.getKey(), Operator.ADDITION, NINE);
                    checkForChecker(availableHits, playerTwo, pair.getKey(), Operator.SUBTRACTION, NINE);
                    checkForChecker(availableHits, playerTwo, pair.getKey(), Operator.ADDITION, ELEVEN);
                    checkForChecker(availableHits, playerTwo, pair.getKey(), Operator.SUBTRACTION, ELEVEN);

                    if (!availableHits.isEmpty())
                        availableHitsSet.add(availableHits);
                }
                break;
            case TWO:
                for (Map.Entry<Integer, Checker> pair : playerTwo.getCheckers().entrySet())
                {
                    Set<Integer> availableHits = new HashSet<>();
                    checkForChecker(availableHits, playerOne, pair.getKey(), Operator.ADDITION, NINE);
                    checkForChecker(availableHits, playerOne, pair.getKey(), Operator.SUBTRACTION, NINE);
                    checkForChecker(availableHits, playerOne, pair.getKey(), Operator.ADDITION, ELEVEN);
                    checkForChecker(availableHits, playerOne, pair.getKey(), Operator.SUBTRACTION, ELEVEN);

                    if (!availableHits.isEmpty())
                        availableHitsSet.add(availableHits);
                }
                break;
        }

        return availableHitsSet;
    }

    private void checkForChecker(Set<Integer> availableMoves, Player player, int tileNr, Operator op, int possibleTileNr)
    {
        if (player.getCheckers().containsKey(op.apply(tileNr, possibleTileNr))){
            checkForTile(availableMoves, player, op.apply(tileNr, possibleTileNr), op, possibleTileNr);
        }
    }
    private void checkForTile(Set<Integer> availableMoves, Player player, int tileNr, Operator op, int possibleTileNr)
    {
        if (!player.getCheckers().containsKey(op.apply(tileNr, possibleTileNr))){
            availableMoves.add(op.apply(tileNr, possibleTileNr));
            checkForChecker(availableMoves, playerTwo, tileNr, Operator.ADDITION, NINE);
            checkForChecker(availableMoves, playerTwo, tileNr, Operator.SUBTRACTION, NINE);
            checkForChecker(availableMoves, playerTwo, tileNr, Operator.ADDITION, ELEVEN);
            checkForChecker(availableMoves, playerTwo, tileNr, Operator.SUBTRACTION, ELEVEN);
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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
