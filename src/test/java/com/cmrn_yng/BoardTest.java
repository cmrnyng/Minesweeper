package com.cmrn_yng;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class BoardTest {
    private Board board;

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
        Cell[][] cells = board.getCells();
        Cell cell = cells[row][col];

        Assertions.assertFalse(cell.isFlagged(), "Cell's 'flagged' attribute should be false by default.");

        board.cellAction(row, col, 1);
        Assertions.assertTrue(cell.isFlagged(),
                "Cell's 'flagged' attribute should be true after calling cellAction to flag that specific cell.");
    }

    @Test
    public void cellActionRevealingUnrevealedCell() {
        board = new Board(9, 9, 10);
        int row = 3, col = 8;
        Cell[][] cells = board.getCells();
        Cell cell = cells[row][col];

        Assertions.assertFalse(cell.isRevealed(), "Cell's 'revealed' attribute should be false by default.");

        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(),
                "Cell's 'revealed' attribute should be true after calling cellAction to reveal that specific cell.");
    }

    @Test
    public void cellActionFlaggingUnflaggedCell() {
        board = new Board(9, 9, 10);
        int row = 8, col = 3;
        Cell[][] cells = board.getCells();
        Cell cell = cells[row][col];

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
        Cell[][] cells = board.getCells();
        Cell cell = cells[row][col];

        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(), "Cell's 'revealed' attribute should be true at first.");

        board.cellAction(row, col, 0);
        Assertions.assertTrue(cell.isRevealed(),
                "Cell's 'revealed' attribute should still be true after calling cellAction to reveal that specific cell.");
    }

    @Test
    public void newBoardGeneratesCorrectNumberOfMines() {
        board = new Board(16, 16, 40);
        Cell[][] cells = board.getCells();
        int expected = 40;
        int actual = 0;
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[0].length; col++) {
                if (cells[row][col].isMine()) actual++;
            }
        }
        Assertions.assertEquals(expected, actual, "Board should have 10 cells marked as mines");
    }

    @Test
    public void newBoardCalculatesAdjacentMinesCorrectly() {
        board = new Board();
        Cell[][] cells = board.getCells();

        int row = 7, col = 1;
        int expected = 2;
        int actual = cells[row][col].getAdjacentMines();
        Assertions.assertEquals(expected, actual, "Number of adjacent mines at this position should equal 2.");

        row = 0;
        col = 0;
        expected = 0;
        actual = cells[row][col].getAdjacentMines();
        Assertions.assertEquals(expected, actual, "Number of adjacent mines at this position should equal 0.");
    }

    @Test
    public void cellActionRevealingMineEndsGame() {
        board = new Board();

        board.cellAction(0, 2, 0); // First move not a mine
        board.cellAction(0, 3, 0); // 2nd move a mine
        Assertions.assertTrue(board.getGameOver(), "gameOver should be true as a mine would have been revealed");
    }

    @Test
    public void cellActionRevealingMineOnFirstMoveDoesntEndGame() {
        board = new Board();
        Cell[][] cells = board.getCells();
        Assertions.assertFalse(cells[0][0].isMine(), "Cell at top left should not be a mine");

        board.cellAction(0, 3, 0); // First move on a mine
        Assertions.assertFalse(board.getGameOver(), "gameOver should be false as it is the first turn");

        Assertions.assertTrue(cells[0][0].isMine(), "Cell at top left should now be a mine");
    }
}
