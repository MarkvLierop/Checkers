package interfaces;

import enums.PlayerNumber;

import java.io.Serializable;

public interface IGame extends Serializable {
    void moveChecker(PlayerNumber playerNumber, int from, int to);
    void startGame();
}
