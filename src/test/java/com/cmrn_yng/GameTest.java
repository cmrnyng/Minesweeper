package com.cmrn_yng;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GameTest {
    private Game game;
    private Board board;
    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    public void setUp() {
        board = new Board(9, 9, 10);
        game = new Game(board);
    }

    @AfterEach
    public void tearDown() {
        // Restore original System.in after each test
        System.setIn(originalSystemIn);
    }

    @Test
    public void moveReturnsArrayForValidInput() {
        String input = "2, 8, R";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        final int[] expected = {7, 1, 0};
        final var actual = game.move();

        Assertions.assertArrayEquals(expected, actual,
                "Game::move should return array for valid input.");
    }

    @Test
    public void moveReturnsValidArrayForMultipleInputs() {
        String input = "2, 8, G\r\n3.5, 4, F\r\n0, 0, R\r\n13, 13, F\r\n9, 9, R\r\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        final int[] expected = {8, 8, 0};
        final var actual = game.move();

        Assertions.assertArrayEquals(expected, actual,
                "Game::move should only return an array for the valid input.");
    }
}
