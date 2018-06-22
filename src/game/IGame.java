package game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IGame extends Remote {

    String getWinner() throws RemoteException;

    boolean hasEnded() throws RemoteException;

    IChecker placeChecker(IChecker checker) throws RemoteException;

    void startGame(int columns, int rows) throws RemoteException;
}
