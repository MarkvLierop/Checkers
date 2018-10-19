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
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    private Pane checkersPane;
    private Pane root;
    private Dimension screenSize;

    private SocketClient sc;
    private ObjectOutputStream objectOutputStream;
    private Game game;
    private Player player;

    private int selectedCheckerPosition = 0;

    private Parent createContent() {
        root = new Pane();
        checkersPane = new Pane();

        root.getChildren().addAll(createGrid());
        root.getChildren().addAll(btnNewGame());

        return root;
    }

    private synchronized List<Circle> addCheckersToGrid()
    {
        List<Circle> checkers = new ArrayList();

        for (Map.Entry<Integer, Checker> pair : game.getPlayerOne().getCheckers().entrySet())
        {
            Circle checker = createChecker(pair.getKey());
            checker.setFill(Color.RED);
            checkers.add(checker);
        }

//        for (Map.Entry<Integer, Checker> pair : game.getPlayerTwo().getCheckers().entrySet())
//        {
//            Circle checker = createChecker(pair.getKey());
//            checker.setFill(Color.GREEN);
//            checkers.add(checker);
//        }

        return checkers;
    }

    private Circle createChecker(int key)
    {
        Circle checker = new Circle(TILE_SIZE / 3);
        checker.setOnMouseClicked(e -> {
            selectedCheckerPosition = key;
            drawCheckers();
            System.out.print("checker selected " + selectedCheckerPosition);
        });

        checker.setCenterX(TILE_SIZE / 2);
        checker.setCenterY(TILE_SIZE / 2);

        if (key >= 10)
        {
            String s = String.valueOf(key);
            char charx = s.charAt(0);
            char chary = s.charAt(1);
            String x = Character.toString(charx);
            String y = Character.toString(chary);
            checker.setTranslateX(Integer.parseInt(y) * (TILE_SIZE) + TILE_SIZE);
            checker.setTranslateY(Integer.parseInt(x) * (TILE_SIZE));
        }
        else
        {
            checker.setTranslateX(key * (TILE_SIZE) + TILE_SIZE);
            checker.setTranslateY(0);
        }

        return checker;
    }

    private Button btnNewGame()
    {
        Button btnNewGame = new Button("New Game");
        btnNewGame.setLayoutX(screenSize.getWidth() - 100); // TODO: AANPASSEN???
        btnNewGame.setLayoutY(50);
        btnNewGame.setOnMouseClicked(event ->{
            try {
                objectOutputStream.writeObject(new NewGame(player));
                Platform.runLater(() -> {
                    while (true) // TODO: AANPASSEN?
                    {
                        if (sc.getGame() != null)
                        {
                            game = sc.getGame();
                            checkersPane.getChildren().addAll(addCheckersToGrid());
                            root.getChildren().addAll(checkersPane);
                            break;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return btnNewGame;
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
        tile.setOnMouseClicked(event -> {
            if (selectedCheckerPosition != 0)
            {
                try {
                    objectOutputStream.writeObject(
                            new MoveChecker(player.getPlayerNumber(), selectedCheckerPosition, toLocation));
                    System.out.println("move sent");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                selectedCheckerPosition = 0;
                drawCheckers();
            }
        });
        tile.setTranslateX(y * (TILE_SIZE) + TILE_SIZE);
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

        player = new Player("username 1"); // TODO: AAnpassen; inloggen
        player.setPlayerNumber(PlayerNumber.ONE);
        primaryStage.setScene(new Scene(createContent(), screenSize.width, screenSize.height));
        primaryStage.show();
    }
}
