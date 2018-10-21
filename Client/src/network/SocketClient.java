package network;

import classes.Game;
import enums.PlayerNumber;
import network.command_types.CommandGame;
import network.command_types.CommandPlayer;
import ui.GameScreen;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Game game;
    public PlayerNumber playerNumber;

    public Game getGame()
    {
        return game;
    }
    public PlayerNumber getPlayerNumber() { return playerNumber; }
    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public SocketClient()
    {
        try
        {
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

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void executeIncommingCommands()
    {
        new Thread(() -> {
            while (true)
            {
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
                else if (inObject instanceof InitialData)
                {
                    InitialData initialData = ((InitialData)inObject);
                    game = initialData.getGame();
                    playerNumber = initialData.getPlayerNumber();
                    System.out.println("Game received");
                }
            }
        }).start();
    }
}
