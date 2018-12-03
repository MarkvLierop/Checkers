package domain.game.checkers;

import domain.enums.Operator;
import domain.game.Move;

import java.util.HashSet;
import java.util.Set;

public class Checker extends AbstractChecker
{
    public Checker(int location){
        super(location);
    }

    public void calculateAvailableMoves(Set<AbstractChecker> checkersCurrentPlayer, Set<AbstractChecker> checkersOpponent)
    {
        availableMoves = new HashSet<>();
        uniqueMoves = new HashSet<>();
        lookForCheckersInAllDirections(checkersCurrentPlayer, checkersOpponent, availableMoves, location);
    }

    private void lookForCheckersInAllDirections(Set<AbstractChecker> checkersCurrentPlayer,
                                                Set<AbstractChecker> checkersOpponent,
                                                Set<Move> movesList, int checkerLocation)
    {
        if (location - NINE > 0 && location - NINE % 10 != 9)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, movesList, checkerLocation, NINE, Operator.SUBTRACTION);
        }
        if (location + NINE < 100 && location + NINE % 10 != 0)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, movesList, checkerLocation, NINE, Operator.ADDITION);
        }
        if (location - ELEVEN > 0 && location - ELEVEN % 10 != 0)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, movesList, checkerLocation, ELEVEN, Operator.SUBTRACTION);
        }
        if (location + ELEVEN < 100 && location + ELEVEN % 10 != 9)
        {
            lookForChecker(checkersCurrentPlayer, checkersOpponent, movesList, checkerLocation, ELEVEN, Operator.ADDITION);
        }
    }

    private void lookForChecker(Set<AbstractChecker> checkersCurrentPlayer,
                                Set<AbstractChecker> checkersOpponent,
                                Set<Move> movesList, int checkerLocation, int value, Operator operator)
    {
        Move move = new Move(checkerLocation);
        int locationToCheck = operator.apply(move.getFrom(), value);

        if (playerHasChecker(checkersCurrentPlayer, locationToCheck))
            return;

        if (playerHasChecker(checkersOpponent, locationToCheck))
            lookForOpenSpot(checkersCurrentPlayer, checkersOpponent, movesList, move, locationToCheck, value, operator);
    }

    private void lookForOpenSpot(Set<AbstractChecker> checkersCurrentPlayer,
                                 Set<AbstractChecker> checkersOpponent,
                                 Set<Move> movesList, Move move, int locationToCheck,
                                 int value, Operator operator)
    {
        int openSpotLocation = operator.apply(locationToCheck, value);

        if (moveIsNotUnique(move.getFrom(), openSpotLocation))
            return;
        if (playerHasChecker(checkersCurrentPlayer, openSpotLocation))
            return;
        if (playerHasChecker(checkersOpponent, openSpotLocation))
            return;

        move.setTo(openSpotLocation);
        move.setCheckerHit(locationToCheck);
        movesList.add(move);
        uniqueMoves.add(move);

        lookForCheckersInAllDirections(checkersCurrentPlayer, checkersOpponent, move.getMoves(), move.getTo());
    }
    private boolean moveIsNotUnique(int from, int to)
    {
        for (Move move : uniqueMoves)
        {
            if (move.getFrom() == from && move.getTo() == to)
                return true;
        }

        return false;
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
