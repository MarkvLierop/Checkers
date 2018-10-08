package network.commands;

import classes.Player;
import network.command_types.Command;

public class NewGame extends Command{
    private Player player;
    public NewGame(Player player)
    {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


    @Override
    public void execute() {

    }
}
