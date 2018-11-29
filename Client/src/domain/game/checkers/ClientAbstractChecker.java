package domain.game.checkers;

import com.google.gson.Gson;
import domain.game.ClientMove;
import domain.interfaces.IToJson;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientAbstractChecker implements IToJson {

    protected Set<ClientMove> availableMoves;
    protected int location;

    public ClientAbstractChecker()
    {
        availableMoves = new HashSet<>();
    }

    public int getLocation() {
        return location;
    }
    public Set<ClientMove> getAvailableMoves()
    {
        return Collections.unmodifiableSet(availableMoves);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
