package network;

import classes.Game;
import classes.Player;

public class InitialData {

    private Game game;
    private Player player;

    public InitialData(Game game, Player player)
    {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
