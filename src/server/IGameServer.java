package server;

import client.IGameClient;
import game.IChecker;
import game.IGame;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IGameServer extends Remote, IGame {

    UUID registerGameClient(IGameClient gameClient) throws RemoteException;

    void broadcastChecker(UUID id, IChecker checker) throws RemoteException;

    void startGame(UUID id, int columns, int rows) throws RemoteException;
}
