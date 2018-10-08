package network;

import classes.Game;
import network.command_types.CommandGame;
import network.command_types.CommandPlayer;
import ui.GameScreen;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private static SocketClient socketClient;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Game game;

    public static SocketClient GetInstance()
    {
        if (socketClient == null)
        {
            socketClient = new SocketClient();
        }

        return socketClient;
    }

    public Game getGame()
    {
        return game;
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public SocketClient()
    {
        try
        {
            game = new Game();
            setupConnection();
            executeIncommingCommands();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupConnection() throws IOException {
        String serverAddress = "localhost";
        Socket socket = new Socket(serverAddress, 5555);

        OutputStream outStream = socket.getOutputStream();
        InputStream inStream = socket.getInputStream();
        out = new ObjectOutputStream(outStream);
        in = new ObjectInputStream(inStream);
    }

    private void executeIncommingCommands() {
        new Thread(() -> {
            Object inObject = null;
            try {
                inObject = in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (inObject instanceof CommandGame)
            {
                CommandGame cg = (CommandGame) inObject;
                cg.setGame(game);
                cg.execute();
            }
            else if (inObject instanceof CommandPlayer)
            {
                CommandPlayer cp = (CommandPlayer) inObject;
            }
        });
    }
}
