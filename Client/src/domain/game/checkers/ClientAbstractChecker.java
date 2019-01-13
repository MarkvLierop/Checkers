package domain.game.checkers;

import domain.game.ClientPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientAbstractChecker {

    protected Set<List<Integer>> availableHits;
    protected Set<List<Integer>> availableMoves;
    protected int x;
    protected int y;
    private ClientPlayer owner;

    public ClientAbstractChecker()
    {
        availableHits = new HashSet<>();
    }

    public int getLocation ()
    {
        return Integer.parseInt(x + "" + y);
    }
    public ClientPlayer getOwner() {
        return owner;
    }
    public Set<List<Integer>> getAvailableHits()
    {
        return Collections.unmodifiableSet(availableHits);
    }
    public Set<List<Integer>> getAvailableMoves()
    {
        return Collections.unmodifiableSet(availableMoves);
    }

    public boolean hasAvailableMoves()
    {
        for (List<Integer> location : availableMoves)
        {
            if (!location.isEmpty())
                return true;
        }

        return false;
    }

}
