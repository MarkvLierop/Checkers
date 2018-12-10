package domain.game.checkers;

import domain.enums.Operator;

import java.util.*;

public class Checker extends AbstractChecker
{
    public Checker(int location){
        super(location);
    }

    @Override
    public void calculateAvailableMoves(Set<AbstractChecker> checkersCurrentPlayer, Set<AbstractChecker> checkersOpponent)
    {
        availableMoves = new HashSet<>();
        lookForCheckersInAllDirections(checkersCurrentPlayer, checkersOpponent);
    }

    private void lookForCheckersInAllDirections(Set<AbstractChecker> checkersCurrentPlayer,
                                                Set<AbstractChecker> checkersOpponent)
    {
        // Right Top
        lookInDirection(checkersCurrentPlayer, checkersOpponent, location, NINE, 0, 9, Operator.SUBTRACTION, Operator.BIGGER_THAN);
        // Left Bottom
        lookInDirection(checkersCurrentPlayer, checkersOpponent, location, NINE, 100, 0, Operator.ADDITION, Operator.SMALLER_THAN);
        // Left Top
        lookInDirection(checkersCurrentPlayer, checkersOpponent, location, ELEVEN, 0, 0, Operator.SUBTRACTION, Operator.BIGGER_THAN);
        // Right Bottom
        lookInDirection(checkersCurrentPlayer, checkersOpponent, location, ELEVEN, 100, 9, Operator.ADDITION, Operator.SMALLER_THAN);
    }

    private void lookInDirection(Set<AbstractChecker> checkersCurrentPlayer,
                                 Set<AbstractChecker> checkersOpponent,
                                 int location, int directionNr, int bottomTopNr, int edgeNr, Operator op, Operator opCompare)
    {
        if (!isWithinGameBoardBoundaries(location, directionNr, bottomTopNr, edgeNr, op, opCompare))
            return;

        List<Integer> currentMoves = new ArrayList<>();
        lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, location, directionNr, op);
        addToMoves(currentMoves);
    }

    private void lookInDirection(Set<AbstractChecker> checkersCurrentPlayer,
                                 Set<AbstractChecker> checkersOpponent,
                                 List<Integer> currentMoves,
                                 int directionNr, int bottomTopNr, int edgeNr, Operator op, Operator opCompare, int originalAmountOfMoves)
    {
        if (!isWithinGameBoardBoundaries(currentMoves.get(currentMoves.size() - 1), directionNr, bottomTopNr, edgeNr, op, opCompare))
            return;

        if (currentMoves.size() == originalAmountOfMoves)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), directionNr, op);
            return;
        }

        List<Integer> newCurrentMoves = new ArrayList<>(currentMoves);
        lookForChecker(checkersCurrentPlayer, checkersOpponent, newCurrentMoves, currentMoves.get(currentMoves.size() - 1), directionNr, op);
        addToMoves(newCurrentMoves);
    }

    private void lookForCheckersInAllDirections(Set<AbstractChecker> checkersCurrentPlayer,
                                                Set<AbstractChecker> checkersOpponent,
                                                List<Integer> currentMoves)
    {
        int originalAmountOfMoves = currentMoves.size();
        if (isWithinGameBoardBoundaries(currentMoves.get(currentMoves.size() -1), NINE, 0, 9, Operator.SUBTRACTION, Operator.BIGGER_THAN))
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), NINE, Operator.SUBTRACTION);

        lookInDirection(checkersCurrentPlayer, checkersOpponent, currentMoves, NINE, 100, 0, Operator.ADDITION, Operator.SMALLER_THAN, originalAmountOfMoves);
        lookInDirection(checkersCurrentPlayer, checkersOpponent, currentMoves, ELEVEN, 0, 0, Operator.SUBTRACTION, Operator.BIGGER_THAN, originalAmountOfMoves);
        lookInDirection(checkersCurrentPlayer, checkersOpponent, currentMoves, ELEVEN, 100, 9, Operator.ADDITION, Operator.SMALLER_THAN, originalAmountOfMoves);
    }

    private void lookForChecker(Set<AbstractChecker> checkersCurrentPlayer,
                                Set<AbstractChecker> checkersOpponent,
                                List<Integer> currentMoves,
                                int currentLocation, int value, Operator operator)
    {
        int locationToCheck = operator.apply(currentLocation, value);

        if (playerHasChecker(checkersOpponent, locationToCheck))
            lookForOpenSpot(checkersCurrentPlayer, checkersOpponent, currentMoves, locationToCheck, value, operator);
    }

    private void lookForOpenSpot(Set<AbstractChecker> checkersCurrentPlayer,
                                 Set<AbstractChecker> checkersOpponent,
                                 List<Integer> currentMoves, int locationToCheck,
                                 int value, Operator operator)
    {
        int openSpotLocation = operator.apply(locationToCheck, value);

        if (playerHasChecker(checkersCurrentPlayer, openSpotLocation))
            return;
        if (playerHasChecker(checkersOpponent, openSpotLocation))
            return;

        if (!currentMoves.contains(openSpotLocation))
        {
            currentMoves.add(openSpotLocation);
            lookForCheckersInAllDirections(checkersCurrentPlayer, checkersOpponent, currentMoves);
        }
    }

    private boolean playerHasChecker(Set<AbstractChecker> checkers, int locationToCheck)
    {
        for (AbstractChecker checker : checkers)
        {
            if (checker.getLocation() == locationToCheck)
            {
                return true;
            }
        }
        return false;
    }

    private boolean isWithinGameBoardBoundaries(int location, int directionNr, int bottomTopNr, int edgeNr, Operator op, Operator opCompare)
    {
        return opCompare.apply(op.apply(location, directionNr), bottomTopNr) == 1
                && op.apply(location, directionNr) % 10 != edgeNr;
    }

    private void addToMoves(List<Integer> currentMoves)
    {
        if (currentMoves.size() > 0)
            availableMoves.add(currentMoves);
    }
}
