package network;

import classes.Game;
import classes.game.GameContainer;
import enums.PlayerNumber;
import network.command_types.Command;
import network.commands.NewGame;
import network.commands.StartGame;

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

            executeIncommingCommand();
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
    }

    private void sendInitData(InitialData initialData) throws IOException {
        out.writeObject(initialData);
    }
    private void executeIncommingCommand() throws IOException, ClassNotFoundException
    {
        while (true)
        {
            Object object = in.readObject();
            if (object instanceof NewGame)
            {
                newGame(object);
            }
            else {
                Command  inObject = (Command) object;
                inObject.setGame(game);
                inObject.execute();
                out.writeObject(inObject);

                if (opponent != null)
                    opponent.sendCommand(inObject);
            }
        }
    }

    private void newGame(Object object) throws IOException {
        game = gameContainer.newGame(((NewGame) object).getPlayer(), this);

        if (game.getPlayerTwo() != null)
        {
            game.startGame();
            out.writeObject(new InitialData(game, PlayerNumber.ONE));
            out.writeObject(new StartGame());
            opponent.sendInitData(new InitialData(game, PlayerNumber.TWO));
            opponent.sendCommand(new StartGame());
        }
    }
}
