package domain.game.checkers;

import com.google.gson.Gson;
import domain.interfaces.IToJson;

import java.util.*;

public abstract class AbstractChecker implements IToJson {
    protected Set<List<Integer>> availableMoves;
    protected int location;

    protected transient final int NINE = 9;
    protected transient final int ELEVEN = 11;

    protected AbstractChecker(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }
    public void setLocation(int location) {
        this.location = location;
    }

    public abstract void calculateAvailableMoves(Set<AbstractChecker> checkersCurrentPlayer, Set<AbstractChecker> checkersOpponent);

    public int getHighestHit()
    {
        int highestHit = 0;
        for (List<Integer> moves : availableMoves)
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
        Set<List<Integer>> tempList = availableMoves;

        for (List<Integer> moves : tempList)
        {
            if (moves.size() < highestHit)
                availableMoves.remove(moves);
        }
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
