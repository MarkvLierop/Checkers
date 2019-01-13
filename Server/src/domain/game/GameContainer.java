package domain.game;

import domain.network.SocketConnection;

import java.util.*;

public class GameContainer {
    private Map<Game, List<SocketConnection>> games;

    public GameContainer()
    {
        games = new TreeMap<>();
    }

    public Game findGame(Player player, SocketConnection connection)
    {
        Game game = joinGame(player, connection);
        if (game != null)
            return game;

        return newGame(player, connection);
    }

//    public boolean removePlayer(Player player)
//    {
//        for (Map.Entry<Game, List<SocketConnection>> pair : games.entrySet())
//        {
//            if (pair.getKey().removePlayer(player))
//            {
//                return pair.getKey().playerHasLeftActiveGame();
//            }
//        }
//
//        return false;
//    }

    private Game newGame(Player player, SocketConnection connection){
        Game game = new Game();
        game.addPlayer(player);
        games.put(game, new ArrayList<SocketConnection>() {{
            add(connection);
        }});

        return game;
    }

    private Game joinGame(Player player, SocketConnection connection)
    {
        for (Map.Entry<Game, List<SocketConnection>> pair : games.entrySet())
        {
            if (pair.getKey().addPlayer(player))
            {
                // TODO: Refactor
                pair.getValue().get(0).setOpponent(connection); // dit
                connection.setOpponent(pair.getValue().get(0));
                pair.getValue().add(connection);
                pair.getKey().startGame();

                return pair.getKey();
            }
        }

        return null;
    }
}
