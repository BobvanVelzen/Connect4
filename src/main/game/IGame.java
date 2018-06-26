package main.game;

import main.client.IGameClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IGame {

    boolean addPlayer(IGameClient gameClient) throws RemoteException;

    String getWinner();

    boolean hasEnded();

    IChecker placeChecker(IChecker checker);

    void startGame(int columns, int rows);

    UUID getGameId();

    List<IGameClient> getGameClients();
}
