package domain.network;


import domain.Packet;
import domain.game.ClientGame;
import domain.game.ClientGameContainer;
import domain.enums.PlayerNumber;
import domain.network.models.Score;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    public void sendScore(Score score){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8080/api/score/save";
        HttpEntity<Score> httpEntity = new HttpEntity<>(score);

        ResponseEntity<Score> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Score.class);

        if (responseEntity.hasBody()) {
            Score result = responseEntity.getBody();

            if (result == null) return; // TODO: show error
        }
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
