package domain.game.checkers;

import com.google.gson.Gson;
import domain.game.Move;
import domain.interfaces.IToJson;

import java.util.Set;

public abstract class AbstractChecker implements IToJson {
    protected Set<Move> availableMoves;
    protected int location;

    protected transient Set<Move> uniqueMoves;
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

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
