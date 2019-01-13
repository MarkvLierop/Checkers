package domain.network.models;

public class Score {
    private String playerName;
    private Result result;
    private int checkersHit;
    private int checkersLost;
    private String serverOrigin;

    public Score() {
    }

    public Score(String playerName, Result result, int checkersHit, int checkersLost, String serverOrigin)
    {
        this.playerName = playerName;
        this.result = result;
        this.checkersHit = checkersHit;
        this.checkersLost = checkersLost;
        this.serverOrigin = serverOrigin;
    }
}
