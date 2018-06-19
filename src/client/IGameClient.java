package client;

import game.IChecker;
import game.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameClient extends Remote {

    void updateChecker(IChecker checker) throws RemoteException;

    void assignPlayerColor(PlayerColor color) throws RemoteException;

    void clearBoard() throws RemoteException;

    PlayerColor getPlayerColor() throws RemoteException;
}