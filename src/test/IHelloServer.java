package test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHelloServer extends Remote {

    String echo(String echo) throws RemoteException;
}
