package network;

import classes.Game;
import classes.Player;
import classes.game.GameContainer;
import enums.PlayerNumber;
import network.command_types.Command;
import network.commands.NewGame;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Runnable {
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

    public SocketConnection(Socket socket, GameContainer gc)
    {
        gameContainer = gc;
        this.socket = socket;
    }

    public void run() {
        try {
            OutputStream outStream = socket.getOutputStream();
            InputStream inStream = socket.getInputStream();

            out = new ObjectOutputStream(outStream);
            in = new ObjectInputStream(inStream);

//            game = gameContainer.newGame(new Player("username"), this);
//            out.writeObject(game);

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

    private void sendCommand(Command command) throws IOException {
        out.writeObject(command);
        System.out.println("Command sent");
    }

    private void executeIncommingCommand() throws IOException, ClassNotFoundException
    {
        Object object = in.readObject();
        if (object instanceof NewGame)
        {
            game = gameContainer.newGame(((NewGame) object).getPlayer(), this);
            out.writeObject(game);
            System.out.println("Game sent");
        }
        else {
            Command  inObject = (Command) in.readObject();
            inObject.setGame(game);
            inObject.execute();
            out.writeObject(inObject);

            if (opponent != null)
                opponent.sendCommand(inObject);
        }
    }
}
