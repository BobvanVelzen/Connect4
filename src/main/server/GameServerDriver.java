package main.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServerDriver extends Observable {

    private static final Logger LOGGER = Logger.getLogger(GameServerDriver.class.getName());

    private static final int PORT = 5099;

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(PORT);
        LOGGER.log(Level.INFO, () -> "SERVER: Registry created on port number " + PORT);
        registry.rebind("connect4", new GameServer());
        LOGGER.log(Level.INFO, () -> "SERVER: Running...");
    }
}
