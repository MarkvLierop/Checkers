package domain.ui;

import domain.Packet;
import domain.game.checkers.ClientChecker;
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
import javafx.scene.text.Font;
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
    private Text txtCurrentTurn;
    private Text txtErrorMessage;
    private Text txtUsernamePlayerOne;
    private Text txtUsernamePlayerTwo;
    private Dimension screenSize;

    private SocketClient sc;
    private ClientPlayer player;

    private int selectedCheckerPosition = 0;

    private Parent createContent() {
        Pane root = new Pane();
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

    private synchronized List<Group> addCheckersToGrid()
    {
        List<Group> checkers = new ArrayList();

        addPlayerOneCheckers(checkers);
        addPlayerTwoCheckers(checkers);

        return checkers;
    }

    private void addPlayerOneCheckers(List<Group> checkers)
    {
        for (ClientChecker checker : sc.getGame().getPlayerOne().getCheckers())
        {
            Group checkerGroup = new Group();

            Circle checkerCircle = createChecker(checkerGroup, checker.getLocation(), sc.getGame().getPlayerOne().getPlayerNumber());
            checkerCircle.setFill(Color.PINK);

            checkerGroup.getChildren().add(checkerCircle);
            checkers.add(checkerGroup);
        }
    }

    private void addPlayerTwoCheckers(List<Group> checkers)
    {
        for (ClientChecker checker : sc.getGame().getPlayerTwo().getCheckers())
        {
            Group checkerGroup = new Group();

            Circle checkerCircle = createChecker(checkerGroup, checker.getLocation(), sc.getGame().getPlayerTwo().getPlayerNumber());
            checkerCircle.setFill(Color.LIGHTBLUE);

            checkerGroup.getChildren().add(checkerCircle);
            checkers.add(checkerGroup);
        }
    }

    private Circle createChecker(Group checkerGroup, int location, PlayerNumber playerNumber)
    {
        Circle checker = new Circle(TILE_SIZE / 3);
        checker.setCenterX(TILE_SIZE / 2);
        checker.setCenterY(TILE_SIZE / 2);

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
                checkerGroup.getChildren().add(txtCheckMark(checker));
                selectedCheckerPosition = location;
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
                if ((sc.getGame().getPlayerByPlayerNumber(player.getPlayerNumber())).availablMovesContainsInt(toLocation))
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
                try {
                    sc.setWaitingForServer();
                    sc.sendPacket(new Packet(Action.MOVECHECKER,
                            new String[] { player.getPlayerNumber().toJson(),
                                            Integer.toString(selectedCheckerPosition),
                                            Integer.toString(toLocation)}));

                    refreshBoardOnServerResponse();

                    refreshBoardOnTurnStart();
                    selectedCheckerPosition = 0;
//                    if (!showErrorMessage())
//                    {
//                        refreshBoardOnTurnStart();
//                        selectedCheckerPosition = 0;
//                    }

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
                sc.sendPacket(new Packet(Action.NEWGAME, new String[] { player.toJson() }));
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

    private boolean showErrorMessage()
    {
        if (sc.getGame().getErrorMessage() != null || !sc.getGame().getErrorMessage().equals(""))
        {
            txtErrorMessage.setText(sc.getGame().getErrorMessage());
            txtErrorMessage.setVisible(true);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    txtErrorMessage.setVisible(false);
                    sc.getGame().setErrorMessage("");
                }
            }, 2000);

            return true;
        }
        else
            return false;
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
        txtUsernamePlayerOne = new Text("Username: ");
        txtUsernamePlayerOne.setLayoutY(15);
        txtUsernamePlayerOne.setFont(new Font(FONT_SIZE));

        return txtUsernamePlayerOne;
    }

    private Text txtUserNamePlayerTwo()
    {
        txtUsernamePlayerTwo = new Text("Username: ");
        txtUsernamePlayerTwo.setLayoutY(screenSize.getHeight() / 2);
        txtUsernamePlayerTwo.setFont(new Font(FONT_SIZE));

        return txtUsernamePlayerTwo;
    }

    private Text txtCurrentTurn()
    {
        txtCurrentTurn = new Text("Current turn: ");
        txtCurrentTurn.setLayoutY(15);
        txtCurrentTurn.setLayoutX(TILE_SIZE * 12);
        txtCurrentTurn.setFont(new Font(FONT_SIZE));

        return txtCurrentTurn;
    }

    private Text txtErrorMessage()
    {
        txtErrorMessage = new Text("Waiting for a player to join...");
        txtErrorMessage.setLayoutY(screenSize.height / 2 - txtErrorMessage.getFont().getSize() / 2);
        txtErrorMessage.setLayoutX(TILE_SIZE * 12);
        txtErrorMessage.setFont(new Font(25));
        txtErrorMessage.setVisible(false);

        return txtErrorMessage;
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
