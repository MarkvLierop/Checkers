package ui;

import classes.Checker;
import classes.Game;
import classes.Player;
import enums.PlayerNumber;
import javafx.application.Application;
import javafx.application.Platform;
import java.util.Timer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.SocketClient;
import network.commands.MoveChecker;
import network.commands.NewGame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends Application
{
    private static final int TILE_SIZE = 70;
    private static final int FONT_SIZE = 16;
    private Pane root;
    private Pane checkersPane;
    private Pane gridPane;
    private Text txtCurrentTurn;
    private Text txtWaitingForPlayer;
    private Text txtUsernamePlayerOne;
    private Text txtUsernamePlayerTwo;
    private Dimension screenSize;

    private SocketClient sc;
    private ObjectOutputStream objectOutputStream;
    private Game game;
    private Player player;

    private int selectedCheckerPosition = 0;

    private Parent createContent() {
        root = new Pane();
        gridPane = new GridPane();
        checkersPane = new Pane();

        gridPane.getChildren().addAll(createGrid());
        root.getChildren().addAll(gridPane);
        root.getChildren().addAll(btnNewGame());
        root.getChildren().addAll(txtCurrentTurn());
        root.getChildren().addAll(txtWaitingForPlayer());
        root.getChildren().addAll(txtUserNamePlayerOne());
        root.getChildren().addAll(txtUserNamePlayerTwo());

        return root;
    }

    private synchronized List<Circle> addCheckersToGrid()
    {
        List<Circle> checkers = new ArrayList();

        addPlayerOneCheckers(checkers);
        addPlayerTwoCheckers(checkers);

        return checkers;
    }

    private void addPlayerOneCheckers(List<Circle> checkers)
    {
        for (Map.Entry<Integer, Checker> pair : game.getPlayerOne().getCheckers().entrySet())
        {
            Circle checker = createChecker(pair.getKey(), game.getPlayerOne().getPlayerNumber());
            checker.setFill(Color.PINK);
            checkers.add(checker);
        }
    }

    private void addPlayerTwoCheckers(List<Circle> checkers)
    {
        for (Map.Entry<Integer, Checker> pair : game.getPlayerTwo().getCheckers().entrySet())
        {
            Circle checker = createChecker(pair.getKey(), game.getPlayerTwo().getPlayerNumber());
            checker.setFill(Color.LIGHTBLUE);
            checkers.add(checker);
        }
    }

    private Circle createChecker(int location, PlayerNumber playerNumber)
    {
        Circle checker = new Circle(TILE_SIZE / 3);

        if (sc.getPlayerNumber() == playerNumber)
            setCheckerEvent(checker, location);

        checker.setCenterX(TILE_SIZE / 2);
        checker.setCenterY(TILE_SIZE / 2);

        if (location >= 10)
        {
            String s = String.valueOf(location);
            char charx = s.charAt(0);
            char chary = s.charAt(1);
            String x = Character.toString(charx);
            String y = Character.toString(chary);
            checker.setTranslateX(Integer.parseInt(y) * TILE_SIZE + (TILE_SIZE * 2));
            checker.setTranslateY(Integer.parseInt(x) * TILE_SIZE);
        }
        else
        {
            checker.setTranslateX(location * (TILE_SIZE) + (TILE_SIZE * 2));
            checker.setTranslateY(0);
        }

        return checker;
    }

    private void setCheckerEvent(Circle checker, int location)
    {
        checker.setOnMouseClicked(e -> {
            selectedCheckerPosition = location;
            gridPane.toFront();
            System.out.print("checker selected " + selectedCheckerPosition);
        });
    }

    private Button btnNewGame()
    {
        Button btnNewGame = new Button("New Game");
        btnNewGame.setLayoutX(screenSize.getWidth() - 100); // TODO: AANPASSEN???
        btnNewGame.setLayoutY(50);
        setBtnNewGameEvent(btnNewGame);

        return btnNewGame;
    }

    private void setBtnNewGameEvent(Button btnNewGame)
    {
        btnNewGame.setOnMouseClicked(event ->{
            try {
                objectOutputStream.writeObject(new NewGame(player));
                Platform.runLater(() -> {
                    while (true) // TODO: AANPASSEN? Asynchrone aanvraag oplossing
                    {
                        if (sc.getGame() != null)
                        {
                            game = sc.getGame();
                            player.setPlayerNumber(sc.getPlayerNumber());
                            break;
                        }
                    }
                });

                waitForPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private Text txtWaitingForPlayer()
    {
        txtWaitingForPlayer = new Text("Waiting for a player to join...");
        txtWaitingForPlayer.setLayoutY(screenSize.height / 2 - txtWaitingForPlayer.getFont().getSize() / 2);
        txtWaitingForPlayer.setLayoutX(TILE_SIZE * 12);
        txtWaitingForPlayer.setFont(new Font(25));
        txtWaitingForPlayer.setVisible(false);

        return txtWaitingForPlayer;
    }

    private void waitForPlayer()
    {
        txtWaitingForPlayer.setVisible(true);

        Platform.runLater(() -> {
            while (true)
            {
                if (sc.getGame().isGameStarted())
                {
                    txtWaitingForPlayer.setVisible(false);
                    txtUsernamePlayerOne.setText(game.getPlayerOne().getUsername());
                    txtUsernamePlayerTwo.setText(game.getPlayerTwo().getUsername());
                    txtCurrentTurn.setText("Current Turn: " + game.getCurrentTurn());
                    checkersPane.getChildren().addAll(addCheckersToGrid());
                    root.getChildren().addAll(checkersPane);

                    break;
                }
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
        setTileEvent(tile, toLocation);

        tile.setTranslateX(y * (TILE_SIZE) + (TILE_SIZE * 2));
        tile.setTranslateY(x * TILE_SIZE);

        if (x % 2 == 0 && y % 2 != 0 ||
                x % 2 != 0 && y % 2 == 0) {
            tile.setFill(Color.BLACK);
        }
        else {
            tile.setFill(Color.WHITE);
        }

        return tile;
    }

    private void setTileEvent(Rectangle tile, int toLocation)
    {
        tile.setOnMouseClicked(event -> {
            if (selectedCheckerPosition != 0 && player.getUsername().equals(game.getCurrentTurn()))
            {
                try {
                    objectOutputStream.writeObject(
                            new MoveChecker(player.getPlayerNumber(), selectedCheckerPosition, toLocation));
                    System.out.println("move sent");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                selectedCheckerPosition = 0;
                txtCurrentTurn.setText("Current Turn: " + game.getCurrentTurn());
                checkersPane.toFront();
                drawCheckers();
            }
        });
    }

    private void drawCheckers()
    {
        checkersPane.getChildren().clear();
        checkersPane.getChildren().addAll(addCheckersToGrid());
    }

    @Override
    public void start(Stage primaryStage) {
        sc = new SocketClient();
        objectOutputStream = sc.getOutputStream();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        player = new Player("Player 2"); // TODO: AAnpassen; inloggen
        primaryStage.setScene(new Scene(createContent(), screenSize.width, screenSize.height));
        primaryStage.show();
    }
}
