package domain.game;

import domain.game.checkers.ClientAbstractChecker;

import java.util.HashSet;
import java.util.Set;

public class ClientMove {
    private Set<ClientMove> moves;
    private ClientAbstractChecker checkerHit;
    private int from;
    private int to;

    public ClientMove()
    {
        moves = new HashSet<>();
    }
    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public ClientAbstractChecker getCheckerHit() {
        return checkerHit;
    }

    public Set<ClientMove> getMoves() {
        return moves;
    }
}
