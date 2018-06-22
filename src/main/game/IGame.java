package main.game;

import main.client.IGameClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGame extends Remote {

    boolean addPlayer(IGameClient gameClient) throws RemoteException;

    String getWinner() throws RemoteException;

    boolean hasEnded() throws RemoteException;

    IChecker placeChecker(IChecker checker) throws RemoteException;

    void startGame(int columns, int rows) throws RemoteException;
}
