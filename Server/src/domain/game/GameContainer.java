package domain.game;

import domain.network.SocketConnection;

import java.util.*;

public class GameContainer {
    private Map<Game, List<SocketConnection>> games;

    public GameContainer()
    {
        games = new TreeMap<>();
    }

    public Game newGame(Player player, SocketConnection connection)
    {
        for (Map.Entry<Game, List<SocketConnection>> pair : games.entrySet())
        {
            if (pair.getKey().addPlayer(player))
            {
                // TODO: Refactor
                pair.getValue().get(0).setOpponent(connection); // dit
                connection.setOpponent(pair.getValue().get(0));
                pair.getValue().add(connection);
                System.out.println("gamefound");
                pair.getKey().startGame();

                return pair.getKey();
            }
        }

        System.out.println("new game created");
        Game game = new Game();
        game.addPlayer(player);
        games.put(game, new ArrayList<SocketConnection>() {{
            add(connection);
        }});
        return game;
    }
}