package domain.game.checkers;

import domain.enums.Operator;
import domain.game.Player;

import java.util.*;

public class Checker extends AbstractChecker
{
    public Checker(int x, int y, Player owner){
        super(x, y, owner);
    }

    @Override
    public void lookforCheckersToHit(AbstractChecker[][] gameBoard) {
        clearLists();
        lookForCheckersInAllDirections(gameBoard, new ArrayList<>(), x, y);
    }

    @Override
    public void calculateAvailableMoves(AbstractChecker[][] gameBoard, Operator xDirection) {
        clearLists();
        calculateDirection(gameBoard, xDirection, Operator.ADDITION);
        calculateDirection(gameBoard, xDirection, Operator.SUBTRACTION);
    }

    private void calculateDirection(AbstractChecker[][] gameBoard, Operator xDirection, Operator yDirection)
    {
        if (!locationIsValid(xDirection.apply(x, 1),yDirection.apply(y, 1)))
            return;
        if (!locationIsEmpty(gameBoard, xDirection.apply(x, 1), yDirection.apply(y, 1)))
            return;

        List<Integer> moveList = new ArrayList<>();
        moveList.add(Integer.parseInt(xDirection.apply(x, 1) + "" + yDirection.apply(y, 1)));

        if (!moveList.isEmpty())
            availableMoves.add(moveList);
    }

    private void lookForCheckersInAllDirections(AbstractChecker[][] gameBoard, List<Integer> checkerHitList, int x, int y)
    {
        lookForChecker(checkerHitList, gameBoard, x + 1, y - 1, Operator.ADDITION, Operator.SUBTRACTION); // right up
        lookForChecker(checkerHitList, gameBoard, x - 1, y - 1, Operator.SUBTRACTION, Operator.SUBTRACTION ); // left up
        lookForChecker(checkerHitList, gameBoard, x + 1, y + 1, Operator.ADDITION, Operator.ADDITION);// right down
        lookForChecker(checkerHitList, gameBoard, x - 1, y + 1, Operator.SUBTRACTION, Operator.ADDITION); // left down
    }

    private void lookForChecker(List<Integer> checkerHitList, AbstractChecker[][] gameBoard, int x, int y, Operator xOp, Operator yOp){
        if (!locationIsValid(x,y))
            return;
        if (locationIsEmpty(gameBoard, x, y))
            return;
        if (currentPlayerHasChecker(gameBoard, x, y))
            return;

        if (opponentHasChecker(gameBoard, x, y))
        {
            List<Integer> newHitList = new ArrayList<>(checkerHitList);
            lookForOpenSpot(newHitList, gameBoard, xOp.apply(x, 1), yOp.apply(y, 1));

            if (!newHitList.isEmpty())
                availableHits.add(newHitList);
        }
    }

    private void lookForOpenSpot(List<Integer> checkerHitList, AbstractChecker[][] gameBoard, int x, int y){
        if (!locationIsValid(x,y))
            return;
        if (!locationIsEmpty(gameBoard, x, y))
            return;
        if (goingBackToOriginalLocation(checkerHitList, x, y))
            return;

        checkerHitList.add(Integer.parseInt(x + "" + y));
        lookForCheckersInAllDirections(gameBoard, checkerHitList, x, y);
    }

    private boolean goingBackToOriginalLocation(List<Integer> checkerHitList, int x, int y)
    {
        if (checkerHitList.size() > 0)
            return checkerHitList.contains(Integer.parseInt(x + "" + y));
        return false;
    }
}
