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
        List<Integer> currentMoves;
        if (location - NINE > 0 && (location - NINE) % 10 != 9)
        {
            currentMoves = new ArrayList<>();
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, location, NINE, Operator.SUBTRACTION);
            if (currentMoves.size() > 0)
                availableMoves.add(currentMoves);
        }
        if (location + NINE < 100 && (location + NINE) % 10 != 0)
        {
            currentMoves = new ArrayList<>();
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, location, NINE, Operator.ADDITION);
            if (currentMoves.size() > 0)
                availableMoves.add(currentMoves);
        }
        if (location - ELEVEN > 0 && (location - ELEVEN) % 10 != 0)
        {
            currentMoves = new ArrayList<>();
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, location, ELEVEN, Operator.SUBTRACTION);
            if (currentMoves.size() > 0)
                availableMoves.add(currentMoves);
        }
        if (location + ELEVEN < 100 && (location + ELEVEN) % 10 != 9)
        {
            currentMoves = new ArrayList<>();
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, location, ELEVEN, Operator.ADDITION);
            if (currentMoves.size() > 0)
                availableMoves.add(currentMoves);
        }
    }
    private void lookForCheckersInAllDirections(Set<AbstractChecker> checkersCurrentPlayer,
                                                Set<AbstractChecker> checkersOpponent,
                                                List<Integer> currentMoves)
    {
        int originalAmountOfMoves = currentMoves.size();
        if (location - NINE > 0 && (location - NINE) % 10 != 9)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), NINE, Operator.SUBTRACTION);
        }
        if (location + NINE < 100 && (location + NINE) % 10 != 0)
        {
            List<Integer> newCurrentMoves;
            if (currentMoves.size() != originalAmountOfMoves)
            {
                newCurrentMoves = currentMoves;
                lookForChecker(checkersCurrentPlayer, checkersOpponent, newCurrentMoves, currentMoves.get(newCurrentMoves.size() - 1), NINE, Operator.ADDITION);
                if (newCurrentMoves.size() > 0)
                    availableMoves.add(newCurrentMoves);
            }
            else
                lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), NINE, Operator.ADDITION);
        }
        if (location - ELEVEN > 0 && (location - ELEVEN) % 10 != 0)
        {
            List<Integer> newCurrentMoves;
            if (currentMoves.size() != originalAmountOfMoves)
            {
                newCurrentMoves = currentMoves;
                lookForChecker(checkersCurrentPlayer, checkersOpponent, newCurrentMoves, newCurrentMoves.get(currentMoves.size() - 1), ELEVEN, Operator.SUBTRACTION);
                if (newCurrentMoves.size() > 0)
                    availableMoves.add(newCurrentMoves);
            }
            else
                lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), ELEVEN, Operator.SUBTRACTION);
        }
        if (location + ELEVEN < 100 && (location + ELEVEN) % 10 != 9)
        {
            List<Integer> newCurrentMoves;
            if (currentMoves.size() != originalAmountOfMoves)
            {
                newCurrentMoves = currentMoves;
                lookForChecker(checkersCurrentPlayer, checkersOpponent, newCurrentMoves, newCurrentMoves.get(currentMoves.size() - 1), ELEVEN, Operator.ADDITION);
                if (newCurrentMoves.size() > 0)
                    availableMoves.add(newCurrentMoves);
            }
            else
                lookForChecker(checkersCurrentPlayer, checkersOpponent, currentMoves, currentMoves.get(currentMoves.size() - 1), ELEVEN, Operator.ADDITION);

        }
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
}
