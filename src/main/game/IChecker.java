package main.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChecker extends Remote {

    PlayerColor getColor() throws RemoteException;

    void setColumn(int column) throws RemoteException;

    int getColumn() throws RemoteException;

    void setRow(int row) throws RemoteException;

    int getRow() throws RemoteException;
}
