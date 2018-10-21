package network.commands;

import classes.Player;
import network.command_types.CommandGame;

public class AddPlayerTwo extends CommandGame {

    private Player playerTwo;
    public AddPlayerTwo(Player playerTwo)
    {
        this.playerTwo = playerTwo;
    }
    @Override
    public void execute() {
        game.setPlayerTwo(playerTwo);
    }
}
