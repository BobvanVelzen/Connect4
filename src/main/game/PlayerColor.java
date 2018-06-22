package main.game;

import javafx.scene.paint.Color;

public enum PlayerColor {
    RED(Color.RED),
    YELLOW(Color.YELLOW),
    NONE(Color.BLACK);

    private Color color;

    PlayerColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
