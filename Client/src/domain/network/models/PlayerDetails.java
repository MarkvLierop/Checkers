package domain.network.models;

public class PlayerDetails {
    private String playerName;
    private int wins;
    private int losses;
    private int draws;
    private int checkersHit;
    private int checkersLost;

    public PlayerDetails(String playerName, int wins, int losses, int draws, int checkersHit, int checkersLost)
    {
        this.playerName = playerName;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.checkersHit = checkersHit;
        this.checkersLost = checkersLost;
    }
}
