package server;

import client.IGameClient;
import game.Game;
import game.IChecker;
import game.PlayerColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameServer extends UnicastRemoteObject implements IGameServer {

    private List<Game> games;

    GameServer() throws RemoteException {
        games = new ArrayList<>();
    }

    @Override
    public UUID registerGameClient(IGameClient gameClient) throws RemoteException {
        UUID gameId = null;
        PlayerColor playerColor = PlayerColor.NONE;

        for (Game game : games) {
            playerColor = game.addPlayer(gameClient);
            if (playerColor != PlayerColor.NONE){
                gameId = game.gameId;
                System.out.println("SERVER: Game joined with gameID " + gameId);
                break;
            }
        }

        if (playerColor == PlayerColor.NONE){
            Game game = new Game(7, 6);
            playerColor = game.addPlayer(gameClient);
            gameId = game.gameId;
            games.add(game);
            gameClient.assignPlayerColor(playerColor);
            System.out.println("SERVER: New game created with gameID " + gameId);
        } else {
            gameClient.assignPlayerColor(playerColor);
        }

        return gameId;
    }

    @Override
    public void broadcastChecker(UUID id, IChecker checker) throws RemoteException {

        for (Game game : games) {
            if (game.gameId.equals(id)) {
                for (IGameClient gc : game.gameClients) {
                    gc.updateChecker(checker);
                }
                break;
            }
        }
    }

    @Override
    public void startGame(UUID id, int columns, int rows) throws RemoteException {

        for (Game game : games) {
            if (game.gameId.equals(id)){
                game.startGame(columns, rows);
                for (IGameClient gc : game.gameClients) {
                    gc.clearBoard();
                }
                System.out.println("SERVER: New game started on gameID " + id);
                break;
            }
        }
    }

    @Override
    public String getWinner(UUID id) {
        for (Game game : games) {
            if (game.gameId.equals(id)) {
                return game.getWinner();
            }
        }
        return "NO GAME FOUND";
    }

    @Override
    public boolean hasEnded(UUID id) {
        for (Game game : games) {
            if (game.gameId.equals(id)) {
                return game.hasEnded();
            }
        }
        return true;
    }

    @Override
    public IChecker placeChecker(UUID id, IChecker checker) throws RemoteException {
        for (Game game : games) {
            if (game.gameId.equals(id)) {
                checker = game.placeChecker(checker);
                broadcastChecker(id, checker);
                break;
            }
        }
        return checker;
    }
}
