package network.commands;

import network.command_types.CommandGame;

public class StartGame extends CommandGame {

    public StartGame()
    {

    }

    @Override
    public void execute() {
        game.startGame();
    }
}
