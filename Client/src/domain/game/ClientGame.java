package domain.game;


import com.google.gson.Gson;
import domain.enums.PlayerNumber;
import domain.interfaces.IToJson;

public class ClientGame implements IToJson {
    private ClientPlayer playerOne;
    private ClientPlayer playerTwo;
    private PlayerNumber currentTurn;
    private boolean gameStarted;
    private String errorMessage;

    public ClientPlayer getPlayerOne() {
        return playerOne;
    }
    public ClientPlayer getPlayerTwo() {
        return playerTwo;
    }
    public PlayerNumber getCurrentTurn() {
        return currentTurn;
    }
    public boolean hasStarted() {
        return gameStarted;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public void setErrorMessage(String message)
    {
        errorMessage = message;
    }

    public ClientPlayer getPlayerByPlayerNumber(PlayerNumber playerNumber) {
        if (playerOne.getPlayerNumber() == playerNumber)
            return playerOne;
        return playerTwo;
    }


    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
