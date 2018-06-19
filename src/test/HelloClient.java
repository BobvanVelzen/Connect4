package test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class HelloClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IHelloServer service = (IHelloServer) Naming.lookup("rmi://localhost:5099/hello");
        System.out.println(service.echo(" ghello"));
    }
}
