package network;

import classes.Game;
import classes.Player;
import enums.PlayerNumber;

import java.io.Serializable;

public class InitialData implements Serializable {

    private Game game;
    private PlayerNumber player;

    public InitialData(Game game, PlayerNumber player)
    {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public PlayerNumber getPlayerNumber() {
        return player;
    }
}
