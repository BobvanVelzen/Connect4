package game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface IGame extends Remote {

    String getWinner(UUID id) throws RemoteException;

    boolean hasEnded(UUID id) throws RemoteException;

    IChecker placeChecker(UUID id, IChecker checker) throws RemoteException;
}
