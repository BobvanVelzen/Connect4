package main.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Checker extends UnicastRemoteObject implements IChecker{

    private final PlayerColor color;
    private int column;
    private int row;

    public Checker(PlayerColor color) throws RemoteException {
        this.color = color;
    }

    public Checker(PlayerColor color, int column) throws RemoteException {
        this.color = color;
        this.column = column;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
