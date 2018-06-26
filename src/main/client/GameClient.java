package main.client;

import main.game.Checker;
import main.game.IChecker;
import main.game.PlayerColor;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.server.IGameServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient extends Application implements IGameClient {

    private static final Logger LOGGER = Logger.getLogger(GameClient.class.getName());

    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private IGameServer gameServer;
    private UUID gameId = null;
    private PlayerColor playerColor = PlayerColor.NONE;
    private boolean clickToReset = false;

    private Pane checkerRoot = new Pane();
    private Text textGameId = new Text(10, 550, "");
    private Text textStatus = new Text(500, 550, "");

    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(checkerRoot);
        root.getChildren().add(makeGrid());
        root.getChildren().addAll(makeColumns());

        textGameId.setFont(Font.font("Verdana",10));
        textGameId.setFill(Color.ORANGE);
        root.getChildren().add(textGameId);

        textStatus.setFont(Font.font("Verdana", FontWeight.BOLD,10));
        setTextStatus(PlayerColor.YELLOW);
        root.getChildren().add(textStatus);

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
                    placeChecker(new Checker(playerColor, column));
                } catch (RemoteException re) {
                    LOGGER.log(Level.SEVERE, re.getMessage());
                }
            });
            
            list.add(rect);
        }

        return list;
    }

    private void placeChecker(IChecker checker) throws RemoteException {
        if (clickToReset) {
            clickToReset = false;
            gameServer.startGame(gameId, COLUMNS, ROWS);
            return;
        }
        gameServer.placeChecker(gameId, checker);
    }

    private void endGame() throws RemoteException {
        String winner = gameServer.getWinner(gameId);
        textStatus.setText(winner);
        LOGGER.log(Level.INFO, winner);
        clickToReset = true;
    }

    @Override
    public void start(Stage primaryStage) throws RemoteException, MalformedURLException, NotBoundException {

        IGameClient listener = (IGameClient) UnicastRemoteObject.exportObject(this, 0);

        gameServer = (IGameServer)Naming.lookup("rmi://localhost:5099/connect4");
        this.gameId = gameServer.registerGameClient(listener);
        textGameId.setText("gameId: " + gameId.toString());
        LOGGER.log(Level.INFO, () -> "CLIENT: New game joined with gameID " + gameId);

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
                else setTextStatus(checker.getColor());
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    private void setTextStatus(PlayerColor color) {
        if (playerColor.equals(color)) {
            textStatus.setText("Opponent's turn");
        } else {
            textStatus.setText("Your turn");
        }
        textStatus.setFill(color.equals(PlayerColor.RED) ? PlayerColor.YELLOW.getColor() : PlayerColor.RED.getColor());
    }

    @Override
    public void clearBoard() {
        Platform.runLater(() -> checkerRoot.getChildren().clear());
        clickToReset = false;
        setTextStatus(PlayerColor.YELLOW);
    }

    @Override
    public void setPlayerColor(PlayerColor color) {
        this.playerColor = color;
    }

    @Override
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
