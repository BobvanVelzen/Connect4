package client;

import game.Checker;
import game.IChecker;
import game.PlayerColor;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.IGameServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameClient extends Application implements IGameClient {

    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private static IGameClient listener;
    private static IGameServer gameServer;
    private UUID gameId = null;
    private boolean clickToReset = false;
    private PlayerColor playerColor = PlayerColor.NONE;

    private Pane checkerRoot = new Pane();

    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(checkerRoot);

        Shape gridShape = makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());

        return root;
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);            
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);
            
            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(e -> {
                try {
                    placeChecker(new Checker(playerColor), column);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            
            list.add(rect);
        }

        return list;
    }

    private void placeChecker(IChecker checker, int column) throws RemoteException {
        if (clickToReset) {
            clickToReset = false;
            gameServer.startGame(gameId, COLUMNS, ROWS);
            return;
        }

        checker.setColumn(column);
        gameServer.placeChecker(gameId, checker);
    }

    private void endGame() throws RemoteException {
        System.out.println(gameServer.getWinner(gameId));
        clickToReset = true;
    }

    @Override
    public void start(Stage primaryStage) throws RemoteException, MalformedURLException, NotBoundException {

        listener = (IGameClient) UnicastRemoteObject.exportObject(this, 0);

        gameServer = (IGameServer)Naming.lookup("rmi://localhost:5099/connect4");
        gameId = gameServer.registerGameClient(listener);
        System.out.println("CLIENT: New game joined with gameID " + gameId);

        gameServer.startGame(gameId, COLUMNS, ROWS);

        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void updateChecker(IChecker checker) {
        if (checker == null)
            return;

        Platform.runLater(() -> {
            try {
                clickToReset = false;

                Circle c = new Circle(TILE_SIZE / 2, checker.getColor().getColor());
                c.setCenterX(GameClient.TILE_SIZE / 2);
                c.setCenterY(GameClient.TILE_SIZE / 2);
                checkerRoot.getChildren().add(c);
                c.setTranslateX(checker.getColumn() * (TILE_SIZE + 5) + TILE_SIZE / 4);

                TranslateTransition animation = new TranslateTransition(Duration.seconds(0.3), c);
                animation.setToY(checker.getRow() * (TILE_SIZE + 5) + TILE_SIZE / 4);
                animation.play();

                if (gameServer.hasEnded(gameId)) endGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void assignPlayerColor(PlayerColor color) {
        this.playerColor = color;
    }

    @Override
    public void clearBoard() {
        Platform.runLater(() -> checkerRoot.getChildren().clear());
    }

    @Override
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
