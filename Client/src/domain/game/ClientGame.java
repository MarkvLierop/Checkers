package domain.game;


import domain.enums.PlayerNumber;
import domain.game.checkers.ClientAbstractChecker;

import java.util.List;

public class ClientGame {
    private ClientPlayer playerOne;
    private ClientPlayer playerTwo;
    private PlayerNumber currentTurn;
    private ClientPlayer winner;
    private boolean gameStarted;
    private String errorMessage;

    private ClientAbstractChecker[][] gameBoard;

    public ClientAbstractChecker[][] getGameBoard() {
        return gameBoard;
    }
    public ClientPlayer getPlayerOne() {
        return playerOne;
    }
    public ClientPlayer getPlayerTwo() {
        return playerTwo;
    }
    public PlayerNumber getCurrentTurn() {
        return currentTurn;
    }
    public ClientPlayer getWinner() { return winner; }
    public boolean hasStarted() {
        return gameStarted;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public void setErrorMessage(String message)
    {
        errorMessage = message;
    }

    public ClientPlayer getPlayerByPlayerNumber(PlayerNumber playerNumber) {
        if (playerOne.getPlayerNumber() == playerNumber)
            return playerOne;
        return playerTwo;
    }

    public boolean availableHitsContains(PlayerNumber playerNumber, int value)
    {
        for (ClientAbstractChecker[] line : gameBoard)
        {
            for (ClientAbstractChecker checker : line)
            {
                if (checker != null && checker.getOwner().getPlayerNumber() == playerNumber)
                {
                    for (List<Integer> list : checker.getAvailableHits())
                    {
                        if (list.contains(value))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean availableMovesContains(PlayerNumber playerNumber, int selectedCheckerPosition, int toLocation)
    {
        int x = setX(selectedCheckerPosition);
        int y = setY(selectedCheckerPosition);
        ClientAbstractChecker checker = gameBoard[x][y];

        if (checker == null)
            return false;
        if (checker.getOwner().getPlayerNumber() != playerNumber)
            return false;

        for (List<Integer> list : checker.getAvailableMoves())
        {
            if (list.contains(toLocation))
                return true;
        }

        return false;
    }

    public boolean playerIsObligatedToHit(PlayerNumber playerNumber)
    {
        boolean result = false;
        for (ClientAbstractChecker[] line : gameBoard)
        {
            for (ClientAbstractChecker checker : line)
            {
                if (checker != null && checker.getOwner().getPlayerNumber() == playerNumber)
                {
                    for (List<Integer> list : checker.getAvailableHits())
                    {
                        if (!list.isEmpty())
                        {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean hasSelectedRightChecker(PlayerNumber playerNumber, int selectedLocation)
    {
        int x = setX(selectedLocation);
        int y = setY(selectedLocation);

        ClientAbstractChecker checker = gameBoard[x][y];

        if (checker.hasAvailableMoves() && !playerIsObligatedToHit(playerNumber))
            return true;

        for (List<Integer> list : checker.getAvailableHits())
        {
            if (!list.isEmpty())
            {
                if (checker.getLocation() == selectedLocation)
                    return true;
            }
        }

        return false;
    }

    private void setXandY(int selectedLocation, int x, int y)
    {

        if (selectedLocation < 10)
        {
            x = 0;
            y = Integer.parseInt(Integer.toString(selectedLocation).substring(0, 1));
        }
        else
        {
            x = Integer.parseInt(Integer.toString(selectedLocation).substring(0, 1));
            y = Integer.parseInt(Integer.toString(selectedLocation).substring(1, 2));
        }
    }

    private int setX(int selectedLocation)
    {
        if (selectedLocation < 10)
            return 0;
        else
            return Integer.parseInt(Integer.toString(selectedLocation).substring(0, 1));
    }

    private int setY(int selectedLocation)
    {
        if (selectedLocation < 10)
            return Integer.parseInt(Integer.toString(selectedLocation).substring(0, 1));
        else
            return Integer.parseInt(Integer.toString(selectedLocation).substring(1, 2));
    }
}
