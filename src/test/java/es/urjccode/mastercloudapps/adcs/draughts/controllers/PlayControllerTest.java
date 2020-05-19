package es.urjccode.mastercloudapps.adcs.draughts.controllers;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.urjccode.mastercloudapps.adcs.draughts.models.Coordinate;
import es.urjccode.mastercloudapps.adcs.draughts.models.Game;
import es.urjccode.mastercloudapps.adcs.draughts.models.State;
import es.urjccode.mastercloudapps.adcs.draughts.models.Color;
import es.urjccode.mastercloudapps.adcs.draughts.models.GameBuilder;

public class PlayControllerTest {

    private PlayController playController;

    @Test
    public void testGivenPlayControllerWhenMoveThenOk() {
        Game game = new GameBuilder().build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(4, 1);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveWithoutPiecesThenIsBlocked() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "        ",
            "        ",
            " n      ",
            "b       ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(3, 2);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertTrue(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveWithoutMovementsThenIsBlocked() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "   n    ",
            "  b b   ",
            "     b  ",
            "b       ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(4, 1);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenCancelThenOk() {
        Game game = new GameBuilder().build();
        playController = new PlayController(game, new State());
        playController.cancel();
        assertEquals(Color.BLACK, playController.getColor());
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMovedPieceCanEat() {
        Game game = new GameBuilder().rows(
            "   n    ",
            "        ",
            "  n     ",
            "   b    ",
            "        ",
            "n    n  ",
            "        ",
            " b    b ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(3, 3);
        Coordinate target = new Coordinate(4, 5);
        playController.move(origin, target);
        assertNull(playController.getPiece(target));
    }

    @Test
    public void testGivenPlayControllerWhenPieceCanEatAndWillNotEatWhite() {
        Game game = new GameBuilder().rows(
            "        ",
            "       b",
            "   n    ",
            "b       ",
            "      n ",
            "        ",
            "    b   ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(6, 4);
        Coordinate target = new Coordinate(5, 5);
        playController.move(origin, target);
        origin = new Coordinate(2, 3);
        target = new Coordinate(3, 4);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(4, 6)));
    }

    @Test
    public void testGivenPlayControllerWhenPieceCanEatAndWillNotEatBlack() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "b  n    ",
            "  b     ",
            "     n  ",
            "        ",
            "        ",
            "  b     ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(2, 0);
        Coordinate target = new Coordinate(1, 1);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(3, 2)));
    }

    @Test
    public void testGivenPlayControllerWhenTwoPiecesCanEatAndWillNotEatWhite() {
        Game game = new GameBuilder().rows(
            "       n",
            "        ",
            "  n n   ",
            "        ",
            "    b   ",
            "        ",
            "      b ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(4, 4);
        Coordinate target = new Coordinate(3, 3);
        playController.move(origin, target);
        origin = new Coordinate(0, 7);
        target = new Coordinate(1, 6);
        playController.move(origin, target);
        assertTrue(playController.getPiece(new Coordinate(2, 2)) == null
            || playController.getPiece(new Coordinate(2, 4)) == null);
    }

    @Test
    public void testGivenPlayControllerWhenTwoPiecesCanEatAndWillNotEatBlack() {
        Game game = new GameBuilder().rows(
            " n      ",
            "     n  ",
            "      b ",
            "        ",
            "        ",
            "  n     ",
            " b      ",
            "      b ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(7, 6);
        Coordinate target = new Coordinate(6, 7);
        playController.move(origin, target);
        assertTrue(playController.getPiece(new Coordinate(6, 1)) == null
            || playController.getPiece(new Coordinate(2, 6)) == null);
    }

    @Test
    public void testGivenPlayControllerWhenDraughtsCanEatAndWillNotEatWhite() {
        Game game = new GameBuilder().rows(
            "       n",
            "  B n   ",
            " n      ",
            "      n ",
            "        ",
            "    b   ",
            "   N   b",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(1, 2);
        Coordinate target = new Coordinate(3, 0);
        playController.move(origin, target);
        origin = new Coordinate(3, 6);
        target = new Coordinate(4, 7);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(6, 3)));
    }

    @Test
    public void testGivenPlayControllerWhenDraughtsCanEatAndWillNotEatBlack() {
        Game game = new GameBuilder().rows(
            "       n",
            "  B   n ",
            " n      ",
            "        ",
            "        ",
            "    b   ",
            "   n   b",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 4);
        Coordinate target = new Coordinate(4, 5);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(1, 2)));
    }
}
