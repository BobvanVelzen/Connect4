package test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServerDriver implements Remote {

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("hello", new HelloServer());
    }
}
