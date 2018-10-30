package classes;

import enums.Operator;
import enums.PlayerNumber;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Serializable, Comparable
{
    private Player playerOne;
    private Player playerTwo;
    private PlayerNumber currentTurn;
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
    public PlayerNumber getCurrentTurn() { return currentTurn; }
    public boolean isGameStarted() { return gameStarted; }

    public Game() {
        gameStarted = false;
    }

    public String getPlayerNameByCurrentTurn()
    {
        switch (currentTurn)
        {
            case ONE:
                return playerOne.getUsername();
            case TWO:
                return playerTwo.getUsername();
            default:
                return null;

        }
    }

    public Player getPlayerByPlayerNumber(PlayerNumber playerNumber)
    {
        if (playerOne.getPlayerNumber() == playerNumber)
            return playerOne;
        return playerTwo;
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

        setAvailableMoves();
        gameStarted = true;

        System.out.println("Game started.");
    }

    public void moveChecker(PlayerNumber playerNumber, int from, int to)
    {
        switch (playerNumber)
        {
            case ONE:
                playerOne.moveChecker(from, to);
                playerTwo.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.TWO;
                break;
            case TWO:
                playerTwo.moveChecker(from, to);
                playerOne.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.ONE;
                break;
        }

        setAvailableMoves();
    }

    private void lookForPossibleHits(Player currentPlayer, Player opponent)
    {
        Map<Checker, Set<Integer>> checkerWithHits = new HashMap<>();
        int highestHitCount = 0;

        for (Checker checker : currentPlayer.getCheckers())
        {
            Set<Integer> possibleHits = new HashSet<>();

            if (checker.getLocation() % 10 != 0)
                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.ADDITION, NINE);
            if (checker.getLocation() % 10 != 9)
                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.SUBTRACTION, NINE);
            if (checker.getLocation() % 10 != 9)
                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.ADDITION, ELEVEN);
            if (checker.getLocation() % 10 != 0)
                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.SUBTRACTION, ELEVEN);

            // Meerslag gaat voor. Alleen hoogste of gelijkwaardige slag bewaren.
            if (!possibleHits.isEmpty())
            {
                if (possibleHits.size() > highestHitCount)
                {
                    highestHitCount = possibleHits.size();
                    checkerWithHits = new HashMap<>();
                }

                if (possibleHits.size() >= highestHitCount)
                {
                    checkerWithHits.put(checker, possibleHits);
                    currentPlayer.setAvailableMoves(checkerWithHits);
                }

            }
        }

    }

    private void lookForChecker(Set<Integer> possibleHits, Player currentPlayer, Player opponent, int checkerLocation, Operator op, int possibleTileNr)
    {
        if (opponent.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr))){
            checkForTile(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), op, possibleTileNr);
        }
    }

    private void checkForTile(Set<Integer> possibleHits, Player currentPlayer,  Player opponent, int checkerLocation, Operator op, int possibleTileNr)
    {
        if (!opponent.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr)) &&
                !currentPlayer.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr)) &&
                !possibleHits.contains(op.apply(checkerLocation, possibleTileNr)))
        {
            possibleHits.add(op.apply(checkerLocation, possibleTileNr));
            if (checkerLocation % 10 != 0)
                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.ADDITION, NINE);
            if (checkerLocation % 10 != 9)
                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.SUBTRACTION, NINE);
            if (checkerLocation % 10 != 9)
                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.ADDITION, ELEVEN);
            if (checkerLocation % 10 != 0)
                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.SUBTRACTION, ELEVEN);
        }
    }

    private void setAvailableMoves()
    {
        playerOne.getAvailableMoves().clear();
        playerTwo.getAvailableMoves().clear();

        lookForPossibleHits(playerOne, playerTwo);
        lookForPossibleHits(playerTwo, playerOne);

        if (playerOne.getAvailableMoves().isEmpty())
        {
            checkForAvailableMoves(playerOne, playerTwo, Operator.ADDITION);
        }

        if (playerTwo.getAvailableMoves().isEmpty())
        {
            checkForAvailableMoves(playerTwo, playerOne, Operator.SUBTRACTION);
        }
    }

    private void checkForAvailableMoves(Player currentPlayer, Player opponent, Operator op)
    {
        Map<Checker, Set<Integer>> availableMoves = new HashMap<>();

        for (Checker checker : currentPlayer.getCheckers())
        {
            Set<Integer> moves = new HashSet<>();

            if (!isCheckerLocatedOn(currentPlayer, opponent, op.apply(checker.getLocation(), NINE)))
            {
                if ((checker.getLocation() % 10 != 9 && op == Operator.SUBTRACTION)
                        || (checker.getLocation() % 10 != 0 && op == Operator.ADDITION))
                {
                    moves.add(op.apply(checker.getLocation(), NINE));
                    availableMoves.put(checker, moves);
                }
            }
            if (!isCheckerLocatedOn(currentPlayer, opponent, op.apply(checker.getLocation(), ELEVEN)))
            {
                if (checker.getLocation() % 10 != 9 && op == Operator.ADDITION
                        || (checker.getLocation() % 10 != 0 && op == Operator.SUBTRACTION))
                {
                    moves.add(op.apply(checker.getLocation(), ELEVEN));
                    availableMoves.put(checker, moves);
                }
            }
        }

        currentPlayer.setAvailableMoves(availableMoves);
    }

    private boolean isCheckerLocatedOn(Player currentPlayer, Player opponent, int tileNumber)
    {
        return opponent.hasCheckerWithLocation(tileNumber) || currentPlayer.hasCheckerWithLocation(tileNumber);
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
