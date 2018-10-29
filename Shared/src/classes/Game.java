package classes;

import enums.Operator;
import enums.PlayerNumber;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Serializable, Comparable
{
    private Player playerOne;
    private Player playerTwo;
    private PlayerNumber currentTurn;
    private boolean gameStarted;

    private final int NINE = 9;
    private final int ELEVEN = 11;

    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }
    public void setPlayerTwo(Player playerTwo) { this.playerTwo = playerTwo; }
    public PlayerNumber getCurrentTurn() { return currentTurn; }
    public boolean isGameStarted() { return gameStarted; }

    public Game() {
        gameStarted = false;
    }

    public String getPlayerNameByCurrentTurn()
    {
        switch (currentTurn)
        {
            case ONE:
                return playerOne.getUsername();
            case TWO:
                return playerTwo.getUsername();
            default:
                return null;

        }
    }

    public Player getPlayerByPlayerNumber(PlayerNumber playerNumber)
    {
        if (playerOne.getPlayerNumber() == playerNumber)
            return playerOne;
        return playerTwo;
    }

    public void startGame()
    {
        playerOne.addCheckers(PlayerNumber.ONE);
        playerTwo.addCheckers(PlayerNumber.TWO);

        int randomNum = ThreadLocalRandom.current().nextInt(0,1);
        if (randomNum == 0)
            currentTurn = playerOne.getPlayerNumber();
        else
            currentTurn = playerTwo.getPlayerNumber();

        setAvailableMoves();
        gameStarted = true;

        System.out.println("Game started.");
    }

    public void moveChecker(PlayerNumber playerNumber, int from, int to)
    {
        switch (playerNumber)
        {
            case ONE:
                playerOne.moveChecker(from, to);
                currentTurn = PlayerNumber.TWO;
                break;
            case TWO:
                playerTwo.moveChecker(from, to);
                currentTurn = PlayerNumber.ONE;
                break;
        }

        setAvailableMoves();
    }

    private void setAvailableMoves()
    {
        switch (currentTurn)
        {
            case ONE:
                checkForAvailableMoves(playerOne, playerTwo, Operator.ADDITION, NINE);
                checkForAvailableMoves(playerOne, playerTwo, Operator.ADDITION, ELEVEN);
                break;
            case TWO:
                checkForAvailableMoves(playerTwo, playerOne, Operator.SUBTRACTION, NINE);
                checkForAvailableMoves(playerTwo, playerOne, Operator.SUBTRACTION, ELEVEN);
                break;
        }
    }

    public Map<Checker, List<Integer>> getPossibleHits(PlayerNumber playerNumber)
    {
        Map<Checker, List<Integer>> checkersWithHits = new TreeMap<>();

        switch (playerNumber)
        {
            case ONE:
                for (Checker checker : playerOne.getCheckers())
                {
                    checkForCheckers(playerTwo, checkersWithHits, checker);
                }
                break;
            case TWO:
                for (Checker checker : playerTwo.getCheckers())
                {
                    checkForCheckers(playerOne, checkersWithHits, checker);
                }
                break;
        }

        return checkersWithHits;
    }

    private void checkForCheckers(Player player, Map<Checker, List<Integer>> checkersWithHits, Checker checker)
    {
        List<Integer> possibleHits = new ArrayList<>();

        if (checker.getLocation() % 10 != 0)
            checkForChecker(possibleHits, player, checker.getLocation(), Operator.ADDITION, NINE);
        if (checker.getLocation() % 10 != 9)
            checkForChecker(possibleHits, player, checker.getLocation(), Operator.SUBTRACTION, NINE);
        if (checker.getLocation() % 10 != 9)
            checkForChecker(possibleHits, player, checker.getLocation(), Operator.ADDITION, ELEVEN);
        if (checker.getLocation() % 10 != 0)
            checkForChecker(possibleHits, player, checker.getLocation(), Operator.SUBTRACTION, ELEVEN);

        if (!possibleHits.isEmpty())
            checkersWithHits.put(checker, possibleHits);
    }

    private void checkForChecker(List<Integer> possibleHits, Player player, int checkerLocation, Operator op, int possibleTileNr)
    {
        if (player.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr))){
            checkForTile(possibleHits, player, op.apply(checkerLocation, possibleTileNr), op, possibleTileNr);
        }
    }

    private void checkForTile(List<Integer> possibleHits, Player player, int checkerLocation, Operator op, int possibleTileNr)
    {
        if (!player.hasCheckerWithLocation(op.apply(checkerLocation, possibleTileNr))){
            possibleHits.add(op.apply(checkerLocation, possibleTileNr));
            checkForChecker(possibleHits, player, checkerLocation, Operator.ADDITION, NINE);
            checkForChecker(possibleHits, player, checkerLocation, Operator.SUBTRACTION, NINE);
            checkForChecker(possibleHits, player, checkerLocation, Operator.ADDITION, ELEVEN);
            checkForChecker(possibleHits, player, checkerLocation, Operator.SUBTRACTION, ELEVEN);
        }
    }

    private void checkForAvailableMoves(Player currentPlayer, Player opponent, Operator op, int tileNumber)
    {
        Map<Checker, Set<Integer>> availableMoves = new TreeMap<>();

        for (Checker checker : currentPlayer.getCheckers())
        {
            Set<Integer> moves = new LinkedHashSet<>();

            if (!opponent.hasCheckerWithLocation(op.apply(checker.getLocation(), tileNumber))
                    && checker.getLocation() % 10 != 0){
                moves.add(op.apply(checker.getLocation(), tileNumber));
                availableMoves.put(checker, moves);
            }
            if (!opponent.hasCheckerWithLocation(op.apply(checker.getLocation(), tileNumber))
                    && checker.getLocation() % 10 != 9){
                moves.add(op.apply(checker.getLocation(), tileNumber));
                availableMoves.put(checker, moves);
            }
        }

        currentPlayer.setAvailableMoves(availableMoves);
    }

    public boolean addPlayer(Player player) {
        if (playerOne == null)
        {
            playerOne = player;
            return true;
        }
        else if (playerTwo == null)
        {
            playerTwo = player;
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
