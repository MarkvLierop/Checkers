package network.command_types;

import classes.Player;

public abstract class CommandPlayer extends Command {
    protected Player player;
    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
