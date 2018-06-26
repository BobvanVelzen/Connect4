package test;

import main.client.IGameClient;
import main.game.IChecker;
import main.game.PlayerColor;

public class MockGameClient implements IGameClient {

    private PlayerColor playerColor = PlayerColor.NONE;

    @Override
    public void updateChecker(IChecker checker) {
        // Not Tested
    }

    @Override
    public void setPlayerColor(PlayerColor color) {
        this.playerColor = color;
    }

    @Override
    public void clearBoard() {
        // Not Tested
    }

    @Override
    public PlayerColor getPlayerColor() {
        return playerColor;
    }
}
