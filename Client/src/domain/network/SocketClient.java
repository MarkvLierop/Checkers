package domain.network;


import domain.Packet;
import domain.game.ClientGame;
import domain.game.ClientGameContainer;
import domain.enums.PlayerNumber;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientGameContainer gameContainer;
    private boolean waitingForServer;

    public SocketClient() {
        try
        {
            setupConnection();
            startPacketListener();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ClientGame getGame()
    {
        return gameContainer.getGame();
    }
    public ClientGameContainer getGameContainer() { return gameContainer; }
    public PlayerNumber getPlayerNumber()
    {
        return gameContainer.getPlayerNumber();
    }
    public void setWaitingForServer()
    {
        this.waitingForServer = true;
    }
    public boolean isWaitingForServerResponse()
    {
        return waitingForServer;
    }

    private void setupConnection() throws IOException {
        String serverAddress = "localhost";
        Socket socket = new Socket(serverAddress, 5555);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        gameContainer = new ClientGameContainer();
    }

    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
    }

    private void startPacketListener()
    {
        new Thread(() ->
        {
            PacketManager jsonManager = new PacketManager(gameContainer);

            while (true)
            {
                try {
                    Packet packet = (Packet) in.readObject();
                    jsonManager.handle(packet);
                    waitingForServer = false;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
