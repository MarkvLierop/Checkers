package domain.game;

import com.google.gson.annotations.Expose;
import domain.game.checkers.AbstractChecker;

import java.util.HashSet;
import java.util.Set;

public class Move {
    private Set<Move> moves;
    private int checkerHit;
    private int from;
    private int to;

    public Move(int from)
    {
        this.from = from;
        moves = new HashSet<>();
    }

    public int getFrom()
    {
        return from;
    }
    public int getTo()
    {
        return to;
    }

    public void setTo(int to)
    {
        this.to = to;
    }
    public void setCheckerHit(int checkerHit)
    {
        this.checkerHit = checkerHit;
    }

    public Set<Move> getMoves()
    {
        return moves;
    }
}
