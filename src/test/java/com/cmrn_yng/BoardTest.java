package com.cmrn_yng;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class BoardTest {
    private Board board;

    // Workaround to get private attribute "cells"
    private Cell getCell(Board board, int row, int col) {
        try {
            Field cellsField = Board.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            Cell[][] cells = (Cell[][]) cellsField.get(board);
            return cells[row][col];
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isValidPositionWhenPositionIsValidForA9x9Board() {
        board = new Board(9, 9, 10);
        final boolean expected = board.isValidPosition(5, 8);
        final boolean actual = true;
        Assertions.assertTrue(expected,
                "Board::isValidPosition should return true for a valid position within a 9x9 board.");
    }

    @Test
    public void isValidPositionWhenPositionIsValidForA16x16Board() {
        board = new Board(16, 16, 40);
        final boolean expected = board.isValidPosition(15, 12);
        Assertions.assertTrue(expected,
                "Board::isValidPosition should return true for a valid position within a 16x16 board.");
    }

    @Test
    public void isValidPositionWhenPositionIsInvalidForA16x30Board() {
        board = new Board(16, 30, 99);
        final boolean expected = board.isValidPosition(15, 31);
        Assertions.assertFalse(expected,
                "Board::isValidPosition should return false for an invalid position within a 16x30 board.");
    }

    @Test
    public void cellActionFlaggingUnrevealedCell() {
        board = new Board(9, 9, 10);
        int row = 3, col = 3;
        Cell cell = getCell(board, row, col);
        Assertions.assertFalse(cell.isFlagged(), "Cell's 'flagged' attribute should be false by default.");

        board.cellAction(row, col, 1);
        Assertions.assertTrue(cell.isFlagged(),
                "Cell's 'flagged' attribute should be true after calling cellAction to flag that specific cell.");
    }

    @Test
    public void cellActionRevealingUnrevealedCell() {
        board = new Board(9, 9, 10);
        int row = 3, col = 8;
        Cell cell = getCell(board, row, col);
        Assertions.assertFalse(cell.isRevealed(), "Cell's 'revealed' attribute should be false by default.");

        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(),
                "Cell's 'revealed' attribute should be true after calling cellAction to reveal that specific cell.");
    }

    @Test
    public void cellActionFlaggingUnflaggedCell() {
        board = new Board(9, 9, 10);
        int row = 8, col = 3;
        Cell cell = getCell(board, row, col);
        board.cellAction(row, col, 1);
        Assertions.assertTrue(cell.isFlagged(), "Cell's 'flagged' attribute should be true at first.");

        board.cellAction(row, col, 1);
        Assertions.assertFalse(cell.isFlagged(),
                "Cell's 'flagged' attribute should be false after calling cellAction to flag a flagged cell.");
    }

    @Test
    public void cellActionRevealingRevealedCell() {
        board = new Board(9, 9, 10);
        int row = 5, col = 1;
        Cell cell = getCell(board, row, col);
        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(), "Cell's 'revealed' attribute should be true at first.");

        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(),
                "Cell's 'revealed' attribute should still be true after calling cellAction to reveal that specific cell.");
    }
}
