package main.game;

import main.client.IGameClient;

import java.rmi.RemoteException;

public interface IGame {

    boolean addPlayer(IGameClient gameClient) throws RemoteException;

    String getWinner();

    boolean hasEnded();

    IChecker placeChecker(IChecker checker);

    void startGame(int columns, int rows);
}
