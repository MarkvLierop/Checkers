package domain.game.checkers;

import domain.enums.Operator;
import domain.game.Player;

import java.util.HashSet;

public class King extends AbstractChecker {
    public King(int x, int y, Player owner)
    {
        super(x,y, owner);
    }

    @Override
    public void lookforCheckersToHit(AbstractChecker[][] gameBoard) {
        availableHits = new HashSet<>();
    }

    @Override
    public void calculateAvailableMoves(AbstractChecker[][] gameBoard, Operator direction) {
        availableHits = new HashSet<>();
    }

}
