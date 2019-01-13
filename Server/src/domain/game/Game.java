package domain.game;

import domain.enums.PlayerNumber;
import domain.game.checkers.AbstractChecker;
import domain.game.checkers.Checker;

public class Game implements Comparable
{
    private AbstractChecker[][] gameBoard;
    private Player playerOne;
    private Player playerTwo;
    private Player winner;
    private PlayerNumber currentTurn;
    private boolean gameStarted;


    public Game() {
        gameBoard = new AbstractChecker[10][10];
        gameStarted = false;
    }

    public boolean isGameStarted() { return gameStarted; }
    public Player getWinner()
    {
        return winner;
    }

    public void startGame()
    {
        addCheckers(6, playerOne);
        addCheckers(0, playerTwo);
        setPlayerNumbers();
        setCurrentTurn();
        calculateMoves();
        gameStarted = true;
    }

    public boolean moveChecker(Player owner, int xFrom, int yFrom, int xTo, int yTo)
    {
        AbstractChecker checkerToMove = gameBoard[xFrom][yFrom];

        if (checkerToMove.getOwner().getPlayerNumber() != owner.getPlayerNumber())
            return false;
        if (gameBoard[xTo][yTo] != null)
            return false;

        gameBoard[xFrom][yFrom] = null;
        gameBoard[xTo][yTo] = checkerToMove;
        checkerToMove.setX(xTo);
        checkerToMove.setY(yTo);

        if (checkerIsHit(xFrom, yFrom, xTo, yTo))
            removeChecker(xFrom, yFrom, xTo, yTo);

        if (checkerToMove.hasCheckersToHit())
        {
            checkerToMove.getOwner().calculateMoves(gameBoard);
            if (checkerToMove.hasCheckersToHit())
                return true;
        }

        calculateMoves();
        changeTurn(owner);

        return true;
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

    public boolean gameHasEnded()
    {
        boolean player1HasCheckers = false;
        boolean player2HasCheckers = false;

        for (AbstractChecker[] row : gameBoard)
        {
            for (AbstractChecker checker : row)
            {
                if (checker != null)
                {
                    if (checker.getOwner().getPlayerNumber() == PlayerNumber.ONE){
                        player1HasCheckers = true;
                    }
                    if (checker.getOwner().getPlayerNumber() == PlayerNumber.TWO){
                        player2HasCheckers = true;
                    }
                }

                if (player1HasCheckers && player2HasCheckers)
                    break;
            }
        }

        if (!player1HasCheckers || !player2HasCheckers)
        {
            setWinner(player1HasCheckers, player2HasCheckers);
            return true;
        }

        return false; // false // end game
    }

    private boolean endGame()
    {
        for (int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if (gameBoard[x][y] != null && gameBoard[x][y].getOwner().getPlayerNumber() == PlayerNumber.ONE)
                    gameBoard[x][y] = null;
            }
        }

        setWinner(false, true);
        return true;
    }

    private void setWinner(boolean player1HasCheckers, boolean player2HasCheckers)
    {
        if (!player1HasCheckers)
            winner = playerTwo;
        if (!player2HasCheckers)
            winner = playerOne;
    }
    private void addCheckers(int xStart, Player player)
    {
        for (int x = xStart; x < xStart + 4; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if (y % 2 == 0 && x % 2 == 1)
                {
                    gameBoard[x][y] = new Checker(x, y, player);
                }
                else if (y % 2 == 1 && x % 2 == 0)
                {
                    gameBoard[x][y] = new Checker(x, y, player);
                }
            }
        }
    }

//
//    public boolean removePlayer(Player player)
//    {
//        if (player.getUsername().equals(playerOne.getUsername()))
//        {
//            playerOne = null;
//            return true;
//        }
//        if (player.getUsername().equals(playerTwo.getUsername()))
//        {
//            playerTwo = null;
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean playerHasLeftActiveGame()
//    {
//        return playerOne != null || playerTwo != null;
//    }

    private void calculateMoves()
    {
        playerOne.calculateMoves(gameBoard);
        playerTwo.calculateMoves(gameBoard);
    }
    private void setCurrentTurn()
    {
        currentTurn = PlayerNumber.ONE;
    }

    private void setPlayerNumbers()
    {
        playerOne.setPlayerNumber(PlayerNumber.ONE);
        playerTwo.setPlayerNumber(PlayerNumber.TWO);
    }

    private void changeTurn(Player owner)
    {
        switch (owner.getPlayerNumber())
        {
            case ONE:
                currentTurn = PlayerNumber.TWO;
                break;
            case TWO:
                currentTurn = PlayerNumber.ONE;
                break;
        }
    }

    private boolean checkerIsHit(int xFrom, int yFrom, int xTo, int yTo)
    {
        return Math.abs(xFrom - xTo) > 1 || Math.abs(yFrom - yTo) > 1;
    }

    private void removeChecker(int xFrom, int yFrom, int xTo, int yTo)
    {
        int locationToRemove = Integer.MIN_VALUE;
        int originalLocation;

        if (Integer.parseInt(xFrom + "" + yFrom) < Integer.parseInt(xTo + "" + yTo))
        {
            originalLocation = Integer.parseInt(xFrom + "" + yFrom);
            locationToRemove = originalLocation +
                    ((Integer.parseInt(xTo + "" + yTo) - Integer.parseInt(xFrom + "" + yFrom)) / 2);
        }
        else if (Integer.parseInt(xFrom + "" + yFrom) > Integer.parseInt(xTo + "" + yTo)){
            originalLocation = Integer.parseInt(xFrom + "" + yFrom);
            locationToRemove = originalLocation -
                    ((Integer.parseInt(xFrom + "" + yFrom) - Integer.parseInt(xTo + "" + yTo)) / 2);
        }

        gameBoard[Integer.parseInt(Integer.toString(locationToRemove).substring(0, 1))]
                    [Integer.parseInt(Integer.toString(locationToRemove).substring(1, 2))] = null;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
