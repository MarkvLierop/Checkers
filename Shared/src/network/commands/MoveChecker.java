package network.commands;

import classes.Player;
import enums.PlayerNumber;
import network.command_types.CommandGame;
import network.command_types.CommandPlayer;

public class MoveChecker extends CommandGame
{
    private PlayerNumber playerNumber;
    private int from, to;

    public MoveChecker(PlayerNumber playerNumber, int from, int to)
    {
        this.playerNumber = playerNumber;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        game.moveChecker(playerNumber, from, to);
    }

}
