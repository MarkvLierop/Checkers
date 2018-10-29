package network.commands;

import classes.Player;
import enums.PlayerNumber;
import network.command_types.CommandGame;

public class SetPlayerNumber {

    private PlayerNumber playerNumber;
    public SetPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }
    public PlayerNumber getPlayerNumber() { return playerNumber; }
}
