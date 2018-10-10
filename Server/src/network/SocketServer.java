package network;

import classes.Game;
import classes.Player;
import classes.game.GameContainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class SocketServer {
    private GameContainer gameContainer;

    public SocketServer()
    {
        startListening();
    }

    private void startListening()
    {
        // TODO: DIT +IS TIJDELIJK
        Game game = new Game();
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        game.startGame();

        System.out.println("Server running...");

        try (ServerSocket listener = new ServerSocket(5555))
        {
            while (true)
            {
                SocketConnection sc = new SocketConnection(listener.accept(), game);
                sc.run();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
