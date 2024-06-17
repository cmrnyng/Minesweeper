package com.cmrn_yng;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Board {
    final private int rows;
    final private int cols;
    final private Cell[][] cells;
    final private int mines;
    private boolean isFirstMove = true;
    private boolean gameOver = false;

    /**
     * Board constructor for testing
     */
    public Board() {
        this.rows = 9;
        this.cols = 9;
        this.mines = 10;
        cells = new Cell[rows][cols];
        initialiseBoard();
        boolean[][] minePositions = {
                {false, false, false, true, false, false, false, false, false},
                {false, false, false, false, true, false, false, false, false},
                {false, false, true, false, false, false, false, true, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, true, false, false, false},
                {false, true, false, false, false, false, false, true, false},
                {false, false, false, false, false, false, false, false, false},
                {false, true, false, false, false, true, false, false, false}
        };
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean isMine = minePositions[row][col];
                cells[row][col].setMine(isMine);
            }
        }
        calculateAdjacentMines();
    }
    /**
     * Board constructor
     * @param rows  number of rows board should have
     * @param cols  number of columns board should have
     * @param mines number of mines board should have
     */
    public Board(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        cells = new Cell[rows][cols];
        generateBoard();
    }

    /**
     * Generates a board by creating it, placing mines, and calculating adjacent mines
     */
    private void generateBoard() {
        initialiseBoard();
        placeMines(mines);
        calculateAdjacentMines();
    }

    /**
     * Fills the board with cells
     */
    private void initialiseBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    /**
     * Randomly sets specified number of cells to be mines
     * @param mines number of mines the board should have
     */
    private void placeMines(int mines) {
        int placedMines = 0;
        Random random = new Random();

        while (placedMines < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                placedMines++;
            }
        }
    }

    /**
     * Calculates adjacent mines and stores that value within each cell
     */
    private void calculateAdjacentMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine()) {
                    int adjacentMines = countAdjacentMines(row, col);
                    cells[row][col].setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    /**
     * Counts the number of adjacent mines for a specific cell
     * @param row the row the cell occupies
     * @param col the column the cell occupies
     * @return    the number of adjacent mines
     */
    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int checkRow = row + i;
                int checkCol = col + j;
                if (isValidPosition(checkRow, checkCol) && cells[checkRow][checkCol].isMine()) count++;
            }
        }
        return count;
    }

    /**
     * Checks if a position is within the board
     * @param row the row to check
     * @param col the column to check
     * @return    whether the position is within the board
     */
    public boolean isValidPosition(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }

    /**
     * Performs an action on a cell
     * @param row    the row the cell occupies
     * @param col    the column the cell occupies
     * @param action the action to take on the cell, flagging / revealing, represented by 1 and 0 respectively
     */
    public void cellAction(int row, int col, int action) {
        Scanner scanner = new Scanner(System.in);
        Cell cell = cells[row][col];
        if (action == 1) { // Flag
            if (cell.isRevealed()) {
                System.out.println("You can't flag a revealed cell!");
                return;
            }
            cell.setFlagged(!cell.isFlagged());
        } else if (action == 0) { // Reveal
            if (cell.isFlagged()) {
                while (true) {
                    System.out.println("You have previously flagged this cell.\nAre you sure you wish to proceed? (y/N)");
                    String decision = scanner.nextLine().trim();
                    if (decision.equalsIgnoreCase("n")) {
                        return;
                    } else if (decision.equalsIgnoreCase("y")) {
                        cell.setFlagged(false);
                        break;
                    } else {
                        System.out.println("Invalid response.");
                    }
                }
            }
            if (cell.isRevealed()) {
                System.out.println("You have already revealed this cell!");
                return;
            }
            if (cell.isMine()) {
                if (isFirstMove) {
                    spawnProtection(cell);
                    revealCell(row, col);
                } else {
                    revealCell(row, col);
                    gameOver = true;
                }
            } else {
                revealCell(row, col);
            }
        } else {
            System.out.println("Error with move conversion.");
        }
        setFirstMove();
    }

    /**
     * Reveals a cell, and will chain for cells with no adjacent mines
     * @param row the row occupied by the cell to be revealed
     * @param col the column occupied by the cell to be revealed
     */
    private void revealCell(int row, int col) {
        if (cells[row][col].isRevealed()) return;

        cells[row][col].setRevealed(true);

        if (cells[row][col].getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int checkRow = row + i;
                    int checkCol = col + j;

                    if (checkRow == row && checkCol == col) continue;
                    if (isValidPosition(checkRow, checkCol) && !cells[checkRow][checkCol].isMine() && !cells[checkRow][checkCol].isRevealed()) {
                        revealCell(checkRow, checkCol);
                    }
                }
            }
        }
    }

    /**
     * Prevents the user from triggering a mine on the first turn by moving the mine to the top left of the board
     * @param cell the cell the user selected, which will always be a mine
     */
    private void spawnProtection(Cell cell) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine()) {
                    cells[row][col].setMine(true);
                    cell.setMine(false);
                    calculateAdjacentMines();
                    return;
                }
            }
        }
    }

    /**
     * Checks to see if the player has won
     * @return whether the player has won
     */
    public boolean hasPlayerWon() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                if (!cell.isMine() && !cell.isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the value of gameOver
     * @return whether gameOver is true or false
     */
    public boolean getGameOver() {
        return this.gameOver;
    }

    /**
     * Prints the board to the terminal
     */
    public void logBoard() {
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String CYAN = "\u001B[36m";
        final String BLUE = "\u001B[34m";
        final String YELLOW = "\u001B[33m";

        String divider = "\n─" + "────".repeat(cols) + "\n";
        StringBuilder grid = new StringBuilder();

        // Top
        grid.append("   ");
        for (int col = 0; col < cols; col++) {
            grid.append(GREEN).append(" ").append(col + 1 < 10 ? " " + (col + 1) : (col + 1)).append(" ").append(RESET);
        }
        grid.append("\n");

        grid.append("   ┌");
        for (int col = 0; col < cols; col++) {
            grid.append("───");
            if (col < cols - 1) {
                grid.append("┬");
            }
        }
        grid.append("┐\n");

        // Rows
        for (int row = 0; row < rows; row++) {
            grid.append(GREEN).append(row + 1 < 10 ? " " + (row + 1) : (row + 1)).append(RESET).append(" │");
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isRevealed()) {
                    if (cells[row][col].isMine()) {
                        grid.append(" * ");
                    } else {
                        int adjacentMines = cells[row][col].getAdjacentMines();
                        switch (adjacentMines) {
                            case 1:
                                grid.append(BLUE).append(" 1 ").append(RESET);
                                break;
                            case 2:
                                grid.append(GREEN).append(" 2 ").append(RESET);
                                break;
                            case 3:
                                grid.append(RED).append(" 3 ").append(RESET);
                                break;
                            case 4:
                                grid.append(CYAN).append(" 4 ").append(RESET);
                                break;
                            default:
                                grid.append(" ").append(adjacentMines == 0 ? "·" : adjacentMines).append(" ");
                                break;
                        }
                    }
                } else if (cells[row][col].isFlagged()) {
                    grid.append(YELLOW).append(" F ").append(RESET);
                } else {
                    grid.append("   ");
                }
                grid.append("│");
            }
            grid.append("\n");

            // Row separator
            if (row < rows - 1) {
                grid.append("   ├");
                for (int col = 0; col < cols; col++) {
                    grid.append("───");
                    if (col < cols - 1) {
                        grid.append("┼");
                    }
                }
                grid.append("┤\n");
            }
        }

        // Bottom border
        grid.append("   └");
        for (int col = 0; col < cols; col++) {
            grid.append("───");
            if (col < cols - 1) {
                grid.append("┴");
            }
        }
        grid.append("┘");

        System.out.println(divider);
        System.out.println(grid);
    }

    /**
     * Sets the 'isFirstMove' attribute to false
     */
    private void setFirstMove() {
        this.isFirstMove = false;
    }

    /**
     * Gets cells array
     * @return cells array
     */
    public Cell[][] getCells() {
        return cells;
    }
}
