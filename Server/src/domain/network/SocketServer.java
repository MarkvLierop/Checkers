package domain.network;

import domain.classes.game.GameContainer;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketServer {

    public SocketServer()
    {
        startListening();
    }

    private void startListening()
    {
        GameContainer gameContainer = new GameContainer();
        System.out.println("Server running...");

        try (ServerSocket listener = new ServerSocket(5555))
        {
            while (true)
            {
                SocketConnection sc = new SocketConnection(listener.accept(), gameContainer);
                Thread t = new Thread(sc);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
