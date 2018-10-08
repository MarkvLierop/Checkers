import classes.Game;
import network.SocketConnection;
import network.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Main
{
    public static void main(String[] args) throws IOException {
        new SocketServer();
    }
}
