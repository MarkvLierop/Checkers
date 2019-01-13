package domain.network;

import domain.Packet;
import domain.game.GameContainer;
import domain.network.models.Score;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    public void sendScore(Score score){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8080/api/score/save";
        try{
            HttpEntity<Score> httpEntity = new HttpEntity<>(score);
            restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Score.class);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection()
    {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
