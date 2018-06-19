package test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServer extends UnicastRemoteObject implements IHelloServer {

    HelloServer() throws RemoteException {
        super();
    }

    @Override
    public String echo(String echo) {
        return "From server" + echo;
    }
}
