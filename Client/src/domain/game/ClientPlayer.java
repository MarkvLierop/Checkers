package domain.game;


import com.google.gson.Gson;
import domain.enums.PlayerNumber;

public class ClientPlayer {
    private String username;
    private PlayerNumber playerNumber;

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }
    public void setPlayerNumber(PlayerNumber playerNumber)
    {
        this.playerNumber = playerNumber;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
}
