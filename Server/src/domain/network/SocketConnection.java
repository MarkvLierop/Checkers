package domain.network;

import domain.Packet;
import domain.game.GameContainer;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private SocketConnection opponent;

    private GameContainer gameContainer;

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

            startPacketListener();
        }
        catch (IOException | ClassNotFoundException ignored) {
            ignored.printStackTrace();
            try {
                socket.close();
            } catch (IOException j) {
                j.printStackTrace();
            }
        }
    }

    public void setOpponent(SocketConnection opponent)
    {
        this.opponent = opponent;
    }

    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
    }
    public void sendPacketToOpponent(Packet packet) throws IOException {
        opponent.sendPacket(packet);
    }

    private void startPacketListener() throws IOException, ClassNotFoundException
    {
        PacketManager packetManager = new PacketManager(this, gameContainer);

        while (true)
        {
            Packet packet = (Packet) in.readObject();
            packetManager.handle(packet);
        }
    }
}
