package network.command_types;

import classes.Game;

public abstract class CommandGame extends Command {
    protected Game game;

    @Override
    public void setGame(Game game)
    {
        this.game = game;
    }
}
