package classes.game;

import classes.Game;
import classes.Player;
import network.SocketConnection;

import java.util.*;

public class GameContainer {
    private Map<Game, List<SocketConnection>> games;

    public GameContainer()
    {
        games = new TreeMap<>();
    }

    public void newGame(Player player, SocketConnection connection)
    {
        boolean gameFound = false;

        for (Map.Entry<Game, List<SocketConnection>> pair : games.entrySet())
        {
            if (pair.getKey().addPlayer(player))
            {
                gameFound = true;
                // TODO: Refactor
                pair.getValue().get(0).setOpponent(connection);
                connection.setOpponent(pair.getValue().get(0));
                pair.getValue().add(connection);
                break;
            }
        }

        if (!gameFound)
        {
            Game game = new Game();
            game.addPlayer(player);
            games.put(game, new ArrayList<SocketConnection>() {{
                add(connection);
            }});
        }
    }
}
