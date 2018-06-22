package main.game;

public interface IChecker {

    PlayerColor getColor();

    void setColumn(int column);

    int getColumn();

    void setRow(int row);

    int getRow();
}
