package domain.ui;

import com.google.gson.Gson;
import domain.Packet;
import domain.game.checkers.ClientAbstractChecker;
import domain.game.ClientPlayer;
import domain.enums.Action;
import domain.enums.PlayerNumber;
import domain.network.SocketClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends Application
{
    private static final int TILE_SIZE = 70;
    private static final int FONT_SIZE = 16;
    private Pane checkersPane;
    private Pane root;
    private Text txtCurrentTurn;
    private Text txtErrorMessage;
    private Text txtUsernamePlayerOne;
    private Text txtUsernamePlayerTwo;
    private Dimension screenSize;

    private SocketClient sc;
    private ClientPlayer player;

    private int selectedCheckerPosition = 0;

    private Parent createContent() {
        root = new Pane();
        checkersPane = new Pane();

        checkersPane.getChildren().addAll(createGrid());
        root.getChildren().addAll(checkersPane);
        root.getChildren().addAll(btnNewGame());
        root.getChildren().addAll(txtCurrentTurn());
        root.getChildren().addAll(txtErrorMessage());
        root.getChildren().addAll(txtUserNamePlayerOne());
        root.getChildren().addAll(txtUserNamePlayerTwo());

        return root;
    }

    private List<Group> addCheckersToGrid()
    {
        ArrayList<Group> checkers = new ArrayList<>();

        for (ClientAbstractChecker[] array : sc.getGame().getGameBoard())
        {
            for (ClientAbstractChecker checker : array)
            {
                if (checker != null)
                {
                    Group checkerGroup = new Group();
                    Circle checkerCircle = createChecker(checkerGroup, checker.getLocation(), checker.getOwner().getPlayerNumber());

                    checkerGroup.getChildren().add(checkerCircle);
                    checkers.add(checkerGroup);
                }
            }
        }

        return checkers;
    }

    private Circle createChecker(Group checkerGroup, int location, PlayerNumber playerNumber)
    {
        Circle checker = new Circle(TILE_SIZE / 3);
        checker.setCenterX(TILE_SIZE / 2);
        checker.setCenterY(TILE_SIZE / 2);

        if (playerNumber == PlayerNumber.ONE)
            checker.setFill(Color.LIGHTBLUE);
        else if (playerNumber == PlayerNumber.TWO)
            checker.setFill(Color.BROWN);

        if (location >= 10)
        {
            String s = String.valueOf(location);
            char charx = s.charAt(0);
            char chary = s.charAt(1);
            String x = Character.toString(charx);
            String y = Character.toString(chary);
            checkerGroup.setTranslateX(Integer.parseInt(y) * TILE_SIZE + (TILE_SIZE * 2));
            checkerGroup.setTranslateY(Integer.parseInt(x) * TILE_SIZE);
        }
        else
        {
            checkerGroup.setTranslateX(location * (TILE_SIZE) + (TILE_SIZE * 2));
            checkerGroup.setTranslateY(0);
        }

        clickCheckerEvent(checkerGroup, checker, location, playerNumber);

        return checker;
    }

    private void clickCheckerEvent(Group checkerGroup, Circle checker, int location, PlayerNumber ownerPlayerNumber)
    {
        checker.setOnMouseClicked(e -> {
            if (player.getPlayerNumber().equals(sc.getGame().getCurrentTurn())
                    && player.getPlayerNumber() == ownerPlayerNumber
                    && selectedCheckerPosition == 0)
            {
                if (sc.getGame().hasSelectedRightChecker(player.getPlayerNumber(), location))
                {
                    checkerGroup.getChildren().add(txtCheckMark(checker));
                    selectedCheckerPosition = location;
                }
                else
                    showErrorMessage("Wrong checker selected");
            }
        });
    }

    private List<Rectangle> createGrid() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                list.add(createTile(x, y));
            }
        }

        return list;
    }

    private Rectangle createTile(int x, int y)
    {
        int toLocation = Integer.parseInt(x + "" + y);
        Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
        clickTileEvent(tile, toLocation);

        tile.setTranslateX(y * (TILE_SIZE) + (TILE_SIZE * 2));
        tile.setTranslateY(x * TILE_SIZE);

        if (x % 2 == 0 && y % 2 != 0 ||
                x % 2 != 0 && y % 2 == 0) {
            if (sc.getGame() != null)
            {
                if ((sc.getGame().availableHitsContains(player.getPlayerNumber(), toLocation)))
                    tile.setFill(Color.DARKRED);
                else
                    tile.setFill(Color.BLACK);
            }
            else
                tile.setFill(Color.BLACK);
        }
        else {
            tile.setFill(Color.WHITE);
        }

        return tile;
    }

    private void clickTileEvent(Rectangle tile, int toLocation)
    {
        tile.setOnMouseClicked(event -> {
            if (selectedCheckerPosition != 0 && player.getPlayerNumber().equals(sc.getGame().getCurrentTurn()))
            {
                if (!moveIsValid(toLocation))
                    return;

                try {
                    sc.setWaitingForServer();
                    sc.sendPacket(new Packet(Action.MOVE_CHECKER,
                            new String[] { new Gson().toJson(player),
                                            formatInt(selectedCheckerPosition),
                                            formatInt(toLocation)}));

                    refreshBoardOnServerResponse();

                    refreshBoardOnTurnStart();
                    selectedCheckerPosition = 0;

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setBtnNewGameEvent(Button btnNewGame)
    {
        btnNewGame.setOnMouseClicked(event ->{
            try {
                sc.sendPacket(new Packet(Action.NEW_GAME, new String[] { new Gson().toJson(player) }));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                while (true)
                {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sc.getGame() != null && sc.getGame().hasStarted())
                    {
                        player.setPlayerNumber(sc.getPlayerNumber());

                        refresh();
                        break;
                    }
                }
            });

            waitForPlayer();
        });
    }

    private void waitForPlayer()
    {
        txtErrorMessage.setVisible(true);

        Platform.runLater(() -> {
            while (true)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (sc.getGame().hasStarted())
                {
                    txtErrorMessage.setVisible(false);
                    txtUsernamePlayerOne.setText(sc.getGame().getPlayerOne().getUsername());
                    txtUsernamePlayerTwo.setText(sc.getGame().getPlayerTwo().getUsername());
                    txtCurrentTurn.setText("Current Turn: " + sc.getGame().getCurrentTurn() + "\n You are " + player.getPlayerNumber());
                    checkersPane.getChildren().addAll(addCheckersToGrid());

                    refreshBoardOnTurnStart();
                    break;
                }
            }
        });
    }

    private void refreshBoardOnServerResponse()
    {
        new Thread(() -> {
            while (true)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!sc.isWaitingForServerResponse())
                {
                    checkIfGameHasEnded();
                    Platform.runLater(this::refresh);
                    break;
                }
            }
        }).start();
    }

    private void refreshBoardOnTurnStart()
    {
        new Thread(() -> {
            while (true)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (sc.getGame().getCurrentTurn() == player.getPlayerNumber())
                {
                    Platform.runLater(this::refresh);
                    break;
                }
            }
        }).start();
    }

    private void refresh()
    {
        txtCurrentTurn.setText("Current Turn: " + sc.getGame().getCurrentTurn() + "\n You are " + player.getPlayerNumber());
        checkersPane.getChildren().clear();
        checkersPane.getChildren().addAll(createGrid());
        checkersPane.getChildren().addAll(addCheckersToGrid());
    }

    private boolean moveIsValid(int toLocation)
    {
        if ((sc.getGame().playerIsObligatedToHit(player.getPlayerNumber())))
        {
            if (!(sc.getGame().availableHitsContains(player.getPlayerNumber(), toLocation)))
            {
                showErrorMessage("You are obligated to hit a checker.");
                return false;
            }
        }
        else if (!(sc.getGame().availableMovesContains(player.getPlayerNumber(), selectedCheckerPosition, toLocation)))
        {
            showErrorMessage("Not a valid move.");
            return false;
        }

        return true;
    }
    private void showErrorMessage(String message)
    {
        txtErrorMessage.setText(message);
        txtErrorMessage.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                txtErrorMessage.setVisible(false);
            }
        },2000);
    }

    private void checkIfGameHasEnded()
    {
        if (sc.getGame().getWinner() == null)
            return;

        txtErrorMessage.setText(sc.getGame().getWinner().getUsername() +  " has won the game");
        txtErrorMessage.setVisible(true);
        Platform.runLater(() -> checkersPane.getChildren().clear());
    }

    private String formatInt(int value)
    {
        if (value < 10)
            return String.format("%02d", value);

        return Integer.toString(value);
    }
    /*----------------*/
    /* UI CONTROLS */
    /*----------------*/
    private Button btnNewGame()
    {
        Button btnNewGame = new Button("New Game");
        btnNewGame.setLayoutX(screenSize.getWidth() - 100); // TODO: AANPASSEN???
        btnNewGame.setLayoutY(50);
        setBtnNewGameEvent(btnNewGame);

        return btnNewGame;
    }

    private Text txtUserNamePlayerOne()
    {
        return txtUsernamePlayerOne = domain.ui.controls.Text.newTextObject("Username: ", 0, 15, true, FONT_SIZE);
    }

    private Text txtUserNamePlayerTwo()
    {
        return txtUsernamePlayerTwo = domain.ui.controls.Text.newTextObject("Username: ",
                                    0, (int)screenSize.getHeight() / 2, true, FONT_SIZE);
    }

    private Text txtCurrentTurn()
    {
        return txtCurrentTurn = domain.ui.controls.Text.newTextObject("Current turn: ",
                                    TILE_SIZE * 12, 15, true, FONT_SIZE);
    }

    private Text txtErrorMessage()
    {
        int fontSize = 25;
        return txtErrorMessage  = domain.ui.controls.Text.newTextObject("Waiting for a player to join...",
                                    TILE_SIZE * 12, (int)(screenSize.height / 2 - fontSize / 2),
                                    false, fontSize);
    }

    private Text txtCheckMark(Circle checker)
    {
        Text checkMark = new Text("✔");
        checkMark.setFill(Color.BLACK);
        checkMark.setX(checker.getCenterX());
        checkMark.setY(checker.getCenterY());

        return checkMark;
    }

    @Override
    public void start(Stage primaryStage) {
        sc = new SocketClient();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        player = new ClientPlayer(); // TODO: AAnpassen; inloggen
        player.setUsername("Player 2");

        primaryStage.setScene(new Scene(createContent(), screenSize.width, screenSize.height));
        primaryStage.show();
    }
}
