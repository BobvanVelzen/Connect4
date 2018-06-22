package main.server;

import main.client.IGameClient;
import main.game.IChecker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IGameServer extends Remote {

    UUID registerGameClient(IGameClient gameClient) throws RemoteException;

    void broadcastChecker(UUID id, IChecker checker) throws RemoteException;

    String getWinner(UUID id) throws RemoteException;

    boolean hasEnded(UUID id) throws RemoteException;

    IChecker placeChecker(UUID id, IChecker checker) throws RemoteException;

    void startGame(UUID id, int columns, int rows) throws RemoteException;
}
