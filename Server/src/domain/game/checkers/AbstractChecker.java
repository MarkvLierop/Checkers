package domain.game.checkers;

import domain.enums.Operator;
import domain.game.Player;

import java.util.*;

public abstract class AbstractChecker {
    protected Set<List<Integer>> availableHits;
    protected Set<List<Integer>> availableMoves;
    private Player owner;
    protected int x;
    protected int y;

    protected AbstractChecker(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        availableHits = new HashSet<>();
        availableMoves = new HashSet<>();
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public Player getOwner()
    {
        return owner;
    }
    public boolean hasCheckersToHit()
    {
        return !availableHits.isEmpty();
    }

    protected boolean opponentHasChecker(AbstractChecker[][] gameBoard, int x, int y) {
        if (locationIsEmpty(gameBoard, x, y))
            return false;

        return gameBoard[x][y].getOwner() != owner;
    }
    protected boolean currentPlayerHasChecker(AbstractChecker[][] gameBoard, int x, int y) {
        if (locationIsEmpty(gameBoard, x, y))
            return false;

        return gameBoard[x][y].getOwner().getPlayerNumber() == owner.getPlayerNumber();
    }
    protected boolean locationIsEmpty(AbstractChecker[][] gameBoard, int x, int y)
    {
        return gameBoard[x][y] == null;
    }
    protected boolean locationIsValid(int x, int y)
    {
        return x <= 9 && y <= 9 && x >= 0 && y >= 0;
    }
    protected void clearLists()
    {
        availableMoves.clear();
        availableHits.clear();
    }

    public abstract void lookforCheckersToHit(AbstractChecker[][] gameBoard);
    public abstract void calculateAvailableMoves(AbstractChecker[][] gameBoard, Operator direction);

    public int getHighestHit()
    {
        int highestHit = 0;
        for (List<Integer> moves : availableHits)
        {
            if (moves.size() > highestHit)
            {
                highestHit = moves.size();
            }
        }

        return highestHit;
    }

    public void removeLowHits(int highestHit)
    {
        Set<List<Integer>> tempList = new HashSet<>(availableHits);

        for (List<Integer> moves : tempList)
        {
            if (moves.size() < highestHit)
                availableHits.remove(moves);
        }
    }
}
