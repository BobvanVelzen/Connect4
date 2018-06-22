package main.client;

import main.game.IChecker;
import main.game.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameClient extends Remote {

    void updateChecker(IChecker checker) throws RemoteException;

    void clearBoard() throws RemoteException;

    void setPlayerColor(PlayerColor color) throws RemoteException;

    PlayerColor getPlayerColor() throws RemoteException;
}