package network;

import classes.Game;
import classes.game.GameContainer;
import network.command_types.Command;
import network.commands.NewGame;

import java.io.*;
import java.net.Socket;

public class SocketConnection extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private SocketConnection opponent;
    private GameContainer gameContainer;
    private Game game;

    public void setOpponent(SocketConnection opponent)
    {
        this.opponent = opponent;
    }

    public SocketConnection(Socket socket, Game game)
    {
        this.socket = socket;
        this.game = game;
    }

    public void run() {
        try {
            OutputStream outStream = socket.getOutputStream();
            InputStream inStream = socket.getInputStream();

            in = new ObjectInputStream(inStream);
            out = new ObjectOutputStream(outStream);

            while (true)
            {
                executeIncommingCommand();
            }
        }
        catch (IOException | ClassNotFoundException ignored) {
            ignored.printStackTrace();
        } finally
        {
            try {
                socket.close();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }


    private void executeIncommingCommand() throws IOException, ClassNotFoundException
    {
        Command  inObject = (Command) in.readObject();
        inObject.setGame(game);
        inObject.execute();
    }
}
