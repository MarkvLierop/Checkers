package domain.classes.game.checkers;

import domain.classes.enums.Operator;
import domain.classes.game.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Checker extends AbstractChecker
{
    public Checker(int location){
        super(location);
    }

    public void calculateAvailableMoves(Set<AbstractChecker> checkersCurrentPlayer, Set<AbstractChecker> checkersOpponent)
    {
        availableMoves = new HashSet<>();
        lookForCheckersInAllDirections(checkersCurrentPlayer, checkersOpponent);
    }

    private void lookForCheckersInAllDirections(Set<AbstractChecker> checkersCurrentPlayer,
                                                Set<AbstractChecker> checkersOpponent)
    {
        Move move = new Move(location);

        if (location - NINE > 0)
        {
            lookForCheckerToHit(checkersCurrentPlayer, checkersOpponent, availableMoves, move, NINE);
        }
        if (location + NINE < 100)
        {
            lookForCheckerToHit(checkersCurrentPlayer, checkersOpponent, availableMoves, move, NINE);
        }
        if (location - ELEVEN > 0)
        {
            lookForCheckerToHit(checkersCurrentPlayer, checkersOpponent, availableMoves, move, ELEVEN);
        }
        if (location + ELEVEN > 100)
        {
            lookForCheckerToHit(checkersCurrentPlayer, checkersOpponent, availableMoves, move, ELEVEN);
        }
    }

    private void lookForCheckerToHit(Set<AbstractChecker> checkersCurrentPlayer,
                                     Set<AbstractChecker> checkersOpponent,
                                     Set<Move> movesList, Move move, int value)
    {
        int locationToCheck = location - value;
        if (currentPlayerHasChecker(checkersCurrentPlayer, locationToCheck))
            return;

        AbstractChecker checkerToHit = opponentHasChecker(checkersOpponent, locationToCheck);
        if (checkerToHit != null)
        {
            locationToCheck -= value;
            if (currentPlayerHasChecker(checkersCurrentPlayer, locationToCheck))
                return;
            if (opponentHasChecker(checkersOpponent, locationToCheck) != null)
                return;

            move.setTo(locationToCheck);
            move.setCheckerHit(checkerToHit);
            movesList.add(move);
        }
    }

    private boolean currentPlayerHasChecker(Set<AbstractChecker> checkersCurrentPlayer, int locationToCheck)
    {
        for (AbstractChecker checker : checkersCurrentPlayer)
        {
            if (checker.getLocation() == locationToCheck)
            {
                return true;
            }
        }
        return false;
    }

    private AbstractChecker opponentHasChecker(Set<AbstractChecker> opponentCheckers, int locationToCheck)
    {
        for (AbstractChecker checker : opponentCheckers)
        {
            if (checker.getLocation() == locationToCheck)
            {
                return checker;
            }
        }
        return null;
    }
}
