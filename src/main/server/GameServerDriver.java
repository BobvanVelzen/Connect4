package main.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observable;

public class GameServerDriver extends Observable {

    private static final int PORT = 5099;

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(PORT);
        System.out.println("SERVER: Registry created on port number " + PORT);
        registry.rebind("connect4", new GameServer());
        System.out.println("SERVER: Running...");
    }
}
