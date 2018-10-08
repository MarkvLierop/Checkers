package network.command_types;

import classes.Game;
import classes.Player;

import java.io.Serializable;

public abstract class Command implements Serializable {
    public abstract void execute();
    public void setGame(Game game){};
    public void setPlayer(Player player){};
}
