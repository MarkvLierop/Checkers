package domain.classes.game;

import domain.classes.game.checkers.AbstractChecker;
import domain.classes.game.checkers.Checker;

import java.util.HashSet;
import java.util.Set;

public class Move {
    private Set<Move> moves;
    private AbstractChecker checkerHit;
    private int from;
    private int to;

    public Move(int from)
    {
        this.from = from;
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
    public void setCheckerHit(AbstractChecker checkerHit)
    {
        this.checkerHit = checkerHit;
    }

    public void addMove(Move move){
        moves.add(move);
    }
    public void newMovesSet()
    {
        moves = new HashSet<>();
    }
}
