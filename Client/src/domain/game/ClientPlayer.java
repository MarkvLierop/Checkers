package domain.game;


import com.google.gson.Gson;
import domain.game.checkers.ClientChecker;
import domain.enums.PlayerNumber;
import domain.interfaces.IToJson;

import java.util.List;
import java.util.Set;

public class ClientPlayer implements IToJson {
    private String username;
    private PlayerNumber playerNumber;
    private Set<ClientChecker> checkers;

    public Set<ClientChecker> getCheckers() {
        return checkers;
    }
    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }
    public void setPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean availablMovesContains(int tileLocation)
    {
        for (ClientChecker checker : checkers)
        {
            for (List<Integer> move : checker.getAvailableMoves())
            {
                if (move.contains(tileLocation))
                    return true;
            }
        }

        return false;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
