package classes;

import enums.Operator;
import enums.PlayerNumber;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Serializable, Comparable
{
    private Player playerOne;
    private Player playerTwo;
    private String currentTurn;
    private boolean gameStarted;

    private final int NINE = 9;
    private final int ELEVEN = 11;

    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }
    public void setPlayerTwo(Player playerTwo) { this.playerTwo = playerTwo; }
    public String getCurrentTurn() { return currentTurn; }
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
            currentTurn = playerOne.getUsername();
        else
            currentTurn = playerTwo.getUsername();

        gameStarted = true;

        System.out.println("Game started.");
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

        if (playerOne.getPlayerNumber() != playerNumber)
            currentTurn = playerOne.getUsername();
        else
            currentTurn = playerTwo.getUsername();
    }

    public Set<Integer> getAvailableMoves(PlayerNumber playerNumber)
    {
        Set<Integer> availableMoves = new HashSet<>();

        switch (playerNumber)
        {
            case ONE:
                for (Map.Entry<Integer, Checker> pair : playerOne.getCheckers().entrySet())
                {
                    if (!playerTwo.getCheckers().containsKey(pair.getKey() + NINE) && pair.getKey() % 10 != 0){
                        availableMoves.add(pair.getKey() + NINE);
                    }
                    if (!playerTwo.getCheckers().containsKey(pair.getKey() + ELEVEN) && pair.getKey() % 10 != 9){
                        availableMoves.add(pair.getKey() + ELEVEN);
                    }
                }
                break;
            case TWO:
                for (Map.Entry<Integer, Checker> pair : playerTwo.getCheckers().entrySet())
                {
                    if (!playerOne.getCheckers().containsKey(pair.getKey() - NINE)&& pair.getKey() % 10 != 9){
                        availableMoves.add(pair.getKey() - NINE);
                    }
                    if (!playerOne.getCheckers().containsKey(pair.getKey() - ELEVEN)&& pair.getKey() % 10 != 0){
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
                    checkForCheckers(playerTwo, availableHitsSet, pair.getKey());
                }
                break;
            case TWO:
                for (Map.Entry<Integer, Checker> pair : playerTwo.getCheckers().entrySet())
                {
                    checkForCheckers(playerOne, availableHitsSet, pair.getKey());
                }
                break;
        }

        return availableHitsSet;
    }

    private void checkForCheckers(Player player, Set<Set<Integer>> availableHitsSet, int checkerLocation)
    {
        Set<Integer> availableHits = new HashSet<>();

        if (checkerLocation % 10 != 0)
            checkForChecker(availableHits, player, checkerLocation, Operator.ADDITION, NINE);
        if (checkerLocation % 10 != 9)
            checkForChecker(availableHits, player, checkerLocation, Operator.SUBTRACTION, NINE);
        if (checkerLocation % 10 != 9)
            checkForChecker(availableHits, player, checkerLocation, Operator.ADDITION, ELEVEN);
        if (checkerLocation % 10 != 0)
            checkForChecker(availableHits, player, checkerLocation, Operator.SUBTRACTION, ELEVEN);

        if (!availableHits.isEmpty())
            availableHitsSet.add(availableHits);
    }

    private void checkForChecker(Set<Integer> availableMoves, Player player, int checkerLocation, Operator op, int possibleTileNr)
    {
        if (player.getCheckers().containsKey(op.apply(checkerLocation, possibleTileNr))){
            checkForTile(availableMoves, player, op.apply(checkerLocation, possibleTileNr), op, possibleTileNr);
        }
    }

    private void checkForTile(Set<Integer> availableMoves, Player player, int tileNr, Operator op, int possibleTileNr)
    {
        if (!player.getCheckers().containsKey(op.apply(tileNr, possibleTileNr))){
            availableMoves.add(op.apply(tileNr, possibleTileNr));
            checkForChecker(availableMoves, player, tileNr, Operator.ADDITION, NINE);
            checkForChecker(availableMoves, player, tileNr, Operator.SUBTRACTION, NINE);
            checkForChecker(availableMoves, player, tileNr, Operator.ADDITION, ELEVEN);
            checkForChecker(availableMoves, player, tileNr, Operator.SUBTRACTION, ELEVEN);
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
