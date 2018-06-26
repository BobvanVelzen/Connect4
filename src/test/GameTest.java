package test;

import main.client.IGameClient;
import main.game.*;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

class GameTest {
    private final IGame game = new Game(7, 6);
    private final IGameClient playerRed = new MockGameClient();
    private final IGameClient playerYellow = new MockGameClient();

    @Test
    void addPlayers() throws RemoteException {
        // TC1.1
        game.addPlayer(playerRed);
        assertEquals(PlayerColor.RED, playerRed.getPlayerColor());

        // TC1.2
        game.addPlayer(playerYellow);
        assertEquals(PlayerColor.YELLOW, playerYellow.getPlayerColor());

        // TC1.3
        IGameClient playerTest = new MockGameClient();

        game.addPlayer(playerTest);
        assertEquals(playerTest.getPlayerColor(), PlayerColor.NONE);
    }

    @Test
    void endGame1() {
        playRedWins();

        assertEquals("RED WINS!", game.getWinner());
        assertTrue(game.hasEnded());
    }

    @Test
    void endGame2() {
        playYellowWins();

        assertEquals("YELLOW WINS!", game.getWinner());
        assertTrue(game.hasEnded());
    }

    @Test
    void endGame3() {
        playDraw();

        assertEquals(16, ((Game)game).getCheckersPlaced());
        assertEquals("DRAW", game.getWinner());
        assertTrue(game.hasEnded());
    }

    @Test
    void endGame4() {
        assertEquals("Game hasn't ended yet", game.getWinner());
        assertFalse(game.hasEnded());
    }

    @Test
    void placeChecker1() {
        game.startGame(2, 2);
        IChecker checker;

        /* TC3.1
           ..
           R.
        */
        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        assertEquals(1, ((Game)game).getCheckersPlaced());

        /* TC3.2
           ..
           R.
        */
        game.placeChecker(checker);
        assertEquals(1, ((Game)game).getCheckersPlaced());

        /* TC3.3
           Y.
           R.
        */
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        assertEquals(2, ((Game)game).getCheckersPlaced());

        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        assertEquals(2, ((Game)game).getCheckersPlaced());
    }

    @Test
    void placeChecker2() {
        playRedWins();
        assertTrue(game.hasEnded());
        assertEquals(7, ((Game)game).getCheckersPlaced());

        game.placeChecker(new Checker(PlayerColor.YELLOW, 2));
        assertEquals(7, ((Game)game).getCheckersPlaced());
    }

    @Test
    void startGame1() {
        game.startGame(7, 6);
        assertEquals(0, ((Game)game).getCheckersPlaced());
    }

    @Test
    void startGame2() {
        startGame1();
        IChecker checker = new Checker(PlayerColor.RED);

        game.placeChecker(checker);
        assertEquals(1, ((Game)game).getCheckersPlaced());

        game.startGame(7, 6);
        assertEquals(0, ((Game)game).getCheckersPlaced());
    }

    // METHODS
    private void playRedWins() {
        // R...
        // RY..
        // RY..
        // RY..
        game.startGame(4, 4);
        IChecker checker;

        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
    }

    private void playYellowWins() {
        // Y...
        // YR..
        // YR..
        // YRR.
        game.startGame(4, 4);
        IChecker checker;

        checker = new Checker(PlayerColor.RED, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 2);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
    }

    private void playDraw() {
        // YYRR
        // RRYY
        // YYRR
        // RRYY
        game.startGame(4, 4);
        IChecker checker;

        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 0);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 1);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 2);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 2);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 2);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 2);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 3);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 3);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 3);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.RED, 3);
        game.placeChecker(checker);
        checker = new Checker(PlayerColor.YELLOW, 1);
        game.placeChecker(checker);
    }
}