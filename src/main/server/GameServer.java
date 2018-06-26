package main.server;

import main.client.IGameClient;
import main.game.Game;
import main.game.IChecker;
import main.game.IGame;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer extends UnicastRemoteObject implements IGameServer {

    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private transient List<IGame> games;

    GameServer() throws RemoteException {
        games = new ArrayList<>();
    }

    @Override
    public UUID registerGameClient(IGameClient gameClient) throws RemoteException {
        UUID gameId = null;
        boolean added = false;

        for (IGame game : games) {
            added = game.addPlayer(gameClient);
            if (added){
                gameId = game.getGameId();
                UUID finalGameId = gameId;
                LOGGER.log(Level.INFO, () -> "SERVER: Game joined with gameID " + finalGameId);
                break;
            }
        }

        if (!added){
            Game game = new Game(7, 6);
            game.addPlayer(gameClient);
            gameId = game.getGameId();
            games.add(game);
            UUID finalGameId1 = gameId;
            LOGGER.log(Level.INFO, () -> "SERVER: New game created with gameID " + finalGameId1);
        }

        return gameId;
    }

    @Override
    public void broadcastChecker(UUID id, IChecker checker) throws RemoteException {

        for (IGame game : games) {
            if (game.getGameId().equals(id)) {
                for (IGameClient gc : game.getGameClients()) {
                    gc.updateChecker(checker);
                }
                break;
            }
        }
    }

    @Override
    public void startGame(UUID id, int columns, int rows) throws RemoteException {

        for (IGame game : games) {
            if (game.getGameId().equals(id)){
                game.startGame(columns, rows);
                for (IGameClient gc : game.getGameClients()) {
                    gc.clearBoard();
                }
                LOGGER.log(Level.INFO, () -> "SERVER: New game started on gameID " + id);
                break;
            }
        }
    }

    @Override
    public String getWinner(UUID id) {
        for (IGame game : games) {
            if (game.getGameId().equals(id)) {
                return game.getWinner();
            }
        }
        return "NO GAME FOUND";
    }

    @Override
    public boolean hasEnded(UUID id) {
        for (IGame game : games) {
            if (game.getGameId().equals(id)) {
                return game.hasEnded();
            }
        }
        return true;
    }

    @Override
    public IChecker placeChecker(UUID id, IChecker checker) throws RemoteException {
        IChecker c = checker;
        for (IGame game : games) {
            if (game.getGameId().equals(id)) {
                c = game.placeChecker(checker);
                broadcastChecker(id, checker);
                break;
            }
        }
        return c;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof GameServer)) {
            return false;
        }
        GameServer gs = (GameServer) o;
        return games == gs.games;
    }

    @Override
    public int hashCode() {
        return Objects.hash(games);
    }
}
