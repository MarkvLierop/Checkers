package ui;

import classes.Player;
import enums.PlayerNumber;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import network.SocketClient;
import network.commands.MoveChecker;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BoardScreen extends Application
{
    private static final int TILE_SIZE = 70;
    private Pane pane;
    private Dimension screenSize;

    private SocketClient sc;
    private ObjectOutputStream objectOutputStream;
    private Player player;

    private int selectedCheckerPosition = 0;

    private void createGrid(GraphicsContext gc) {
        for (int y = 0; y < 10; y++)
        {
            for (int x = 0; x < 10; x++)
            {
                int toLocation = Integer.parseInt(x + "" + y);
                int xLocation = y * (TILE_SIZE) + TILE_SIZE;
                int yLocation = x * TILE_SIZE;

                if (x % 2 == 0 && y % 2 != 0 ||
                        x % 2 != 0 && y % 2 == 0) {
                    gc.setFill(Color.BLACK);
                    gc.setStroke(Color.WHITE);
                }
                else {
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.BLACK);
                }

                gc.fillRect(xLocation, yLocation, TILE_SIZE, TILE_SIZE);
                gc.strokeText(x+""+y, xLocation, yLocation + TILE_SIZE);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        sc = new SocketClient();
        objectOutputStream = sc.getOutputStream();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        player = new Player("username 1"); // TODO: AAnpassen; inloggen

        Canvas canvas = new Canvas(screenSize.getWidth(), screenSize.getHeight());
        canvas.setWidth(screenSize.width);
        canvas.setHeight(screenSize.height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        createGrid(gc);

        pane = new Pane();
        pane.getChildren().add(canvas);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }


}
