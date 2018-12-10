package domain.game.checkers;

import com.google.gson.Gson;
import domain.interfaces.IToJson;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientAbstractChecker implements IToJson {

    protected Set<List<Integer>> availableMoves;
    protected int location;

    public ClientAbstractChecker()
    {
        availableMoves = new HashSet<>();
    }

    public int getLocation() {
        return location;
    }
    public Set<List<Integer>> getAvailableMoves()
    {
        return Collections.unmodifiableSet(availableMoves);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
