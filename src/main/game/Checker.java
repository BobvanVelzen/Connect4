package main.game;

import java.io.Serializable;

public class Checker implements IChecker, Serializable {

    private final PlayerColor color;
    private int column;
    private int row;

    public Checker(PlayerColor color) {
        this.color = color;
    }

    public Checker(PlayerColor color, int column) {
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
