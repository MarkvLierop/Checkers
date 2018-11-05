package classes;

import com.sun.javafx.image.IntPixelGetter;
import enums.Operator;
import enums.PlayerNumber;
import interfaces.IGame;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements IGame, Comparable
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
//                playerTwo.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.TWO;
                break;
            case TWO:
                playerTwo.moveChecker(from, to);
//                playerOne.removeCheckerIfExists(from + Math.abs(from - to) / 2);
                currentTurn = PlayerNumber.ONE;
                break;
        }

        setAvailableMoves();
    }

//    private void lookForPossibleHits(Player currentPlayer, Player opponent)
//    {
//        Map<Checker, Set<Integer>> checkerWithHits = new HashMap<>();
//        int highestHitCount = 0;
//
//        for (Checker checker : currentPlayer.getCheckers())
//        {
//            Set<Integer> possibleHits = new HashSet<>();

//            if (checker.getLocation() % 10 != 0)
//                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.ADDITION, NINE);
//            if (checker.getLocation() % 10 != 9)
//                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.SUBTRACTION, NINE);
//            if (checker.getLocation() % 10 != 9)
//                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.ADDITION, ELEVEN);
//            if (checker.getLocation() % 10 != 0)
//                lookForChecker(possibleHits, currentPlayer, opponent, checker.getLocation(), Operator.SUBTRACTION, ELEVEN);

            // Meerslag gaat voor. Alleen hoogste of gelijkwaardige slag bewaren.
//            if (!possibleHits.isEmpty())
//            {
//                if (possibleHits.size() > highestHitCount)
//                {
//                    highestHitCount = possibleHits.size();
//                    checkerWithHits = new HashMap<>();
//                }
//
//                if (possibleHits.size() >= highestHitCount)
//                {
//                    System.out.println(checker.getLocation() + " " + possibleHits);
//                    checkerWithHits.put(checker, possibleHits);
//                    currentPlayer.setAvailableMoves(checkerWithHits);
//                }
//
//            }
//        }
//        System.out.println(checkerWithHits.size());
//    }

//    private void lookForChecker(Set<Integer> possibleHits, Player currentPlayer, Player opponent, int checkerLocation, Operator op, int possibleTileNr)
//    {
//        if (opponent.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr))){
//            checkForTile(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), op, possibleTileNr);
//        }
//    }

//    private void checkForTile(Set<Integer> possibleHits, Player currentPlayer,  Player opponent, int checkerLocation, Operator op, int possibleTileNr)
//    {
//        if (!opponent.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr)) &&
//                !currentPlayer.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr)) &&
//                !possibleHits.contains(op.apply(checkerLocation, possibleTileNr)))
//        {
//            possibleHits.add(op.apply(checkerLocation, possibleTileNr));
//            if (checkerLocation % 10 != 0 && !(op.apply(checkerLocation, possibleTileNr) > 99))
//                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.ADDITION, NINE);
//            if (checkerLocation % 10 != 9 && !(op.apply(checkerLocation, possibleTileNr) < 0))
//                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.SUBTRACTION, NINE);
//            if (checkerLocation % 10 != 9 && !(op.apply(checkerLocation, possibleTileNr) > 99))
//                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.ADDITION, ELEVEN);
//            if (checkerLocation % 10 != 0 && !(op.apply(checkerLocation, possibleTileNr) < 0))
//                lookForChecker(possibleHits, currentPlayer, opponent, op.apply(checkerLocation, possibleTileNr), Operator.SUBTRACTION, ELEVEN);
//        }
//    }

    private void setAvailableMoves()
    {
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

    private void lookForPossibleHits(Player currentPlayer, Player opponent)
    {
        Map<Checker, List<Integer>> checkersAndPossiblePositions = new HashMap<>();

        for (Checker checker : currentPlayer.getCheckers())
        {
            List<Integer> possiblePositions = new ArrayList<>();
            lookForCheckersInAllDirections(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                                            checker, checker.getLocation());
        }

//        keepHighestHitsOnly(checkersAndPossiblePositions);
        currentPlayer.setAvailableMoves(checkersAndPossiblePositions);
    }

    private void lookForCheckersInAllDirections(Map<Checker, List<Integer>> checkersAndPossiblePositions, List<Integer> possiblePositions,
                                                Player currentPlayer, Player opponent, Checker checker, int checkerLocation)
    {
        checkIfOpponentHasCheckerLocatedOn(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                checker,checkerLocation - NINE, NINE, Operator.SUBTRACTION);
        checkIfOpponentHasCheckerLocatedOn(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                checker,checkerLocation + NINE, NINE, Operator.ADDITION);
        checkIfOpponentHasCheckerLocatedOn(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                checker,checkerLocation - ELEVEN, ELEVEN, Operator.SUBTRACTION);
        checkIfOpponentHasCheckerLocatedOn(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                checker,checkerLocation + ELEVEN, ELEVEN, Operator.ADDITION);
    }
    private void checkIfOpponentHasCheckerLocatedOn(Map<Checker, List<Integer>> checkersAndPossiblePositions, List<Integer> possiblePositions,
                                             Player currentPlayer, Player opponent, Checker checker, int locationToCheck, int value, Operator operator)
    {
        if (opponent.hasCheckerWithLocation(locationToCheck) && (locationToCheck % 10 != 0 && locationToCheck % 10 != 9))
        {
            checkForOpenSpot(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent, checker, locationToCheck, value, operator);
        }
    }

    private void checkForOpenSpot(Map<Checker, List<Integer>> checkersAndPossiblePositions, List<Integer> possiblePositions,
                                   Player currentPlayer, Player opponent, Checker checker, int locationToCheck, int value, Operator operator)
    {
        if (!isCheckerLocatedOn(currentPlayer, opponent, operator.apply(locationToCheck, value))
                && !possiblePositions.contains(operator.apply(locationToCheck, value)))
        {
            possiblePositions.add(operator.apply(locationToCheck, value));

            if (checkersAndPossiblePositions.get(checker) == null)
            {
                checkersAndPossiblePositions.put(checker, possiblePositions);
            }
            else
            {
                checkersAndPossiblePositions.get(checker).clear();
                checkersAndPossiblePositions.get(checker).addAll(possiblePositions);
            }

            System.out.println(checkersAndPossiblePositions.get(checker));
            lookForCheckersInAllDirections(checkersAndPossiblePositions, possiblePositions, currentPlayer, opponent,
                                            checker, operator.apply(locationToCheck, value));
        }
    }

    private void checkForAvailableMoves(Player currentPlayer, Player opponent, Operator op)
    {
        Map<Checker, List<Integer>> availableMoves = new HashMap<>();

        for (Checker checker : currentPlayer.getCheckers())
        {
            List<Integer> moves = new ArrayList<>();

            checkForOpenSpot(currentPlayer, opponent, checker, availableMoves, moves, NINE,
                    op, Operator.SUBTRACTION, Operator.ADDITION);
            checkForOpenSpot(currentPlayer, opponent, checker, availableMoves, moves, ELEVEN,
                    op, Operator.ADDITION, Operator.SUBTRACTION);
        }

        currentPlayer.setAvailableMoves(availableMoves);
    }

    private void checkForOpenSpot(Player currentPlayer, Player opponent, Checker checker,
                                  Map<Checker, List<Integer>> availableMoves, List<Integer> moves,
                                  int value, Operator directionOp, Operator variableOp1, Operator variableOp2)
    {
        if (!isCheckerLocatedOn(currentPlayer, opponent, directionOp.apply(checker.getLocation(), value)))
        {
            if ((checker.getLocation() % 10 != 9 && directionOp == variableOp1)
                    || (checker.getLocation() % 10 != 0 && directionOp == variableOp2))
            {
                moves.add(directionOp.apply(checker.getLocation(), value));
                availableMoves.put(checker, moves);
            }
        }
    }

    private void keepHighestHitsOnly(Map<Checker, List<Integer>> checkersAndPossiblePositions)
    {
        Map<Checker, List<Integer>> tempCheckersAndPossiblePositions = checkersAndPossiblePositions;
        int highestHit = 0;

        for (Map.Entry<Checker, List<Integer>> entry : tempCheckersAndPossiblePositions.entrySet())
        {
            if (highestHit < entry.getValue().size())
            {
                highestHit = entry.getValue().size();
            }
            else
                checkersAndPossiblePositions.remove(entry.getKey());
        }

        for (Map.Entry<Checker, List<Integer>> entry : tempCheckersAndPossiblePositions.entrySet())
        {
            if (highestHit > entry.getValue().size())
            {
                checkersAndPossiblePositions.remove(entry.getKey());
            }
        }
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
