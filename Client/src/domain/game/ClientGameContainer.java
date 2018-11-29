package domain.game;


import domain.enums.PlayerNumber;

public class ClientGameContainer {
    private ClientGame game;
    private PlayerNumber playerNumber;

    public ClientGame getGame() {
        return game;
    }

    public void setGame(ClientGame game) {
        this.game = game;
    }

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public void setLocalPlayerNumber(PlayerNumber playerNumber) {
        this.playerNumber = playerNumber;
    }
}
