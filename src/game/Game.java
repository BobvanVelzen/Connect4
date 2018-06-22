package game;

import client.IGameClient;
import javafx.geometry.Point2D;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game implements IGame {

    public List<IGameClient> gameClients = new ArrayList<>();
    public UUID gameId = UUID.randomUUID();

    private int columns;
    private int rows;
    private PlayerColor turn = PlayerColor.RED;
    private PlayerColor winner;
    private boolean hasEnded = false;

    private IChecker[][] grid;
    private int checkersPlaced = 0;

    public Game(int columns, int rows) {
        startGame(columns, rows);
    }

    public PlayerColor addPlayer(IGameClient gameClient) throws RemoteException {
        switch (gameClients.size()){
            case 0:
                gameClients.add(gameClient);
                return PlayerColor.RED;
            case 1:
                gameClients.add(gameClient);

                IGameClient gc = gameClients.get(0);
                return gc.getPlayerColor() != PlayerColor.YELLOW ? PlayerColor.YELLOW : PlayerColor.RED;
            default: return PlayerColor.NONE;
        }
    }

    private Optional<IChecker> getChecker(int column, int row) {
        if (column < 0 || column >= columns || row < 0 || row >= rows) {
            return Optional.empty();
        }
        return Optional.ofNullable(grid[column][row]);
    }

    @Override
    public String getWinner() {
        if (hasEnded) {
            return "Winner: " + (winner != null ? winner : "DRAW");
        } else return "Game hasn't ended yet";
    }

    @Override
    public boolean hasEnded() {
        return this.hasEnded;
    }

    private boolean checkRange(List<Point2D> points) throws RemoteException {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int)p.getX();
            int row = (int)p.getY();

            IChecker checker = getChecker(column, row).orElse(new Checker(PlayerColor.NONE));
            if (checker.getColor() == turn) {
                chain++;
                if (chain == 4){
                    return true;
                }
            } else chain = 0;
        }

        return false;
    }

    private boolean gameEnded(int column, int row) throws RemoteException {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> topLeftDiagonal = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> botLeftDiagonal = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        if (checkRange(vertical) || checkRange(horizontal) || checkRange(topLeftDiagonal) || checkRange(botLeftDiagonal)) {
            this.winner = turn;
            return true;
        } else return checkersPlaced >= columns * rows;

    }

    @Override
    public IChecker placeChecker(IChecker checker) throws RemoteException {
        if (hasEnded) {
            hasEnded = false;
            return null;
        }

        if (checker.getColor() != turn)
            return null;
        int row = rows - 1;

        while (row >= 0) {
            if (!getChecker(checker.getColumn(), row).isPresent()) {
                break;
            }
            row--;
        }

        if (row < 0)
            return null;

        checker.setRow(row);
        grid[checker.getColumn()][row] = checker;
        checkersPlaced++;

        if (gameEnded(checker.getColumn(), checker.getRow())) {
            endGame();
        } else if (turn == PlayerColor.RED) {
            turn = PlayerColor.YELLOW;
        } else if (turn == PlayerColor.YELLOW) {
            turn = PlayerColor.RED;
        }

        return checker;
    }

    private void endGame() {
        hasEnded = true;
    }

    @Override
    public void startGame(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.grid = new IChecker[columns][rows];
        this.turn = PlayerColor.RED;
        this.hasEnded = false;
        this.winner = null;
    }
}
