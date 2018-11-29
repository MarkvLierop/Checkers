package domain.classes.game.checkers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class King extends AbstractChecker {
    public King(int location)
    {
        super(location);
    }

    @Override
    public void calculateAvailableMoves(Set<AbstractChecker> checkersCurrentPlayer, Set<AbstractChecker> checkersOpponent) {
        availableMoves = new HashSet<>();
    }
}
