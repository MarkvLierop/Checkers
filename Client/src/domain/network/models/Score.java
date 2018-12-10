package domain.network.models;

public class Score {
    private PlayerDetails playerDetails;
    private int score;
    private String serverOrigin;

    public Score(PlayerDetails playerDetails, int score, String serverOrigin)
    {
        this.playerDetails = playerDetails;
        this.score = score;
        this.serverOrigin = serverOrigin;
    }
}
