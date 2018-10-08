package ui;

import classes.Checker;
import classes.Game;
import classes.Player;
import enums.PlayerNumber;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen extends Application
{
    private static final int TILE_SIZE = 70;
    private Pane checkersPane;
    private Dimension screenSize;

    private ObjectOutputStream objectOutputStream;
    private Game game;

    private Parent createContent() {
        Pane root = new Pane();
        checkersPane = new Pane();

        root.getChildren().addAll(createGrid());
        checkersPane.getChildren().addAll(addCheckersToGrid());
        root.getChildren().addAll(checkersPane);

        return root;
    }

    private List<Circle> addCheckersToGrid()
    {
        List<Circle> checkers = new ArrayList();

        for (Map.Entry<Integer, Checker> pair : game.getPlayerOne().getCheckers().entrySet())
        {
            Circle checker = createChecker(pair.getKey());
            checker.setFill(Color.RED);
            checkers.add(checker);
        }

        for (Map.Entry<Integer, Checker> pair : game.getPlayerTwo().getCheckers().entrySet())
        {
            Circle checker = createChecker(pair.getKey());
            checker.setFill(Color.GREEN);
            checkers.add(checker);
        }

        return checkers;
    }

    private Circle createChecker(int key)
    {
        Circle checker = new Circle(TILE_SIZE / 3);
        checker.setOnMouseClicked(e -> {
            try {
                objectOutputStream.writeObject(new MoveChecker(PlayerNumber.ONE, key, 55));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            game.moveChecker(PlayerNumber.ONE, key, 45);
            drawCheckers();
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

    private List<Rectangle> createGrid() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setTranslateX(x * (TILE_SIZE) + TILE_SIZE);
                tile.setTranslateY(y * (TILE_SIZE));

                if (x % 2 == 0 && y % 2 != 0 ||
                        x % 2 != 0 && y % 2 == 0) {
                    tile.setFill(Color.BLACK);
                }
                else {
                    tile.setFill(Color.WHITE);
                }

                //tile.setOnMouseEntered(e -> tile.setFill(Color.rgb(255, 200, 200, 0.4)));
                //tile.setOnMouseExited(e -> tile.setFill(Color.TRANSPARENT));

                list.add(tile);
            }
        }

        return list;
    }

    private void drawCheckers()
    {
        checkersPane.getChildren().clear();
        checkersPane.getChildren().addAll(addCheckersToGrid());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        game = SocketClient.GetInstance().getGame();
        objectOutputStream = SocketClient.GetInstance().getOutputStream();
        // TODO: DIT IS TIJDELIJK
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        game.startGame();

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setScene(new Scene(createContent(), screenSize.width, screenSize.height));
        primaryStage.show();
    }
}
