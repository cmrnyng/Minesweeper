package com.cmrn_yng;

import java.util.Random;
import java.util.Scanner;

public class Board {
    final private int rows;
    final private int cols;
    final private Cell[][] cells;
    final private int mines;
    private boolean isFirstMove = true;
    final private GameStateCallback callback;

    public Board(int rows, int cols, int mines, GameStateCallback callback) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.callback = callback;
        cells = new Cell[rows][cols];

        generateBoard();
//        logBoard();
    }

    private void generateBoard() {
        initialiseBoard();
        placeMines(mines);
        calculateAdjacentMines();
    }

    private void initialiseBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

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

    public boolean isValidPosition(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }

    public void cellAction(int row, int col, char action) {
        Scanner scanner = new Scanner(System.in);
        Cell cell = cells[row][col];
        if (action == 'f') {
            if (cell.isRevealed()) {
                System.out.println("You can't flag a revealed cell!");
                return;
            }
            cell.setFlagged(!cell.isFlagged());
        } else if (action == 'r') {
            if (cell.isFlagged()) {
                System.out.println("You have previously flagged this cell.\nAre you sure you wish to proceed? (y/N)");
                String decision = scanner.nextLine().trim();
                if (decision.equalsIgnoreCase("n")) return;
                cell.setFlagged(false);
            }
            if (cell.isMine()) {
                if (isFirstMove) {
                    spawnProtection(cell);
                } else {
                    revealCell(row, col);
                    callback.gameOver();
                }
            } else {
                revealCell(row, col);
            }
        } else {
            System.out.println("Error with move conversion.");
        }
        setFirstMove();
    }

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

    public void logBoard() {
        String divider = "\n─" + "────".repeat(cols) + "\n";
        StringBuilder grid = new StringBuilder();

        // Top border
        grid.append("┌");
        for (int col = 0; col < cols; col++) {
            grid.append("───");
            if (col < cols - 1) {
                grid.append("┬");
            }
        }
        grid.append("┐\n");

        // Rows
        for (int row = 0; row < rows; row++) {
            grid.append("│");
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isRevealed()) {
                    if (cells[row][col].isMine()) {
                        grid.append(" * ");
                    } else {
                        int adjacentMines = cells[row][col].getAdjacentMines();
                        grid.append(" ").append(adjacentMines).append(" ");
                    }
                } else if (cells[row][col].isFlagged()) {
                    grid.append(" F ");
                } else {
                    grid.append(" · ");
                }
                grid.append("│");
            }
            grid.append("\n");

            // Row separator or bottom border
            if (row < rows - 1) {
                grid.append("├");
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
        grid.append("└");
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

    public void logBoardDebug() {
        String divider = "\n─" + "────".repeat(cols) + "\n";
        StringBuilder grid = new StringBuilder();

        // Top border
        grid.append("┌");
        for (int col = 0; col < cols; col++) {
            grid.append("───");
            if (col < cols - 1) {
                grid.append("┬");
            }
        }
        grid.append("┐\n");

        // Rows
        for (int row = 0; row < rows; row++) {
            grid.append("│");
            for (int col = 0; col < cols; col++) {

                    if (cells[row][col].isMine()) {
                        grid.append(" * ");
                    } else {
                        int adjacentMines = cells[row][col].getAdjacentMines();
                        grid.append(" ").append(adjacentMines).append(" ");
                    }

                grid.append("│");
            }
            grid.append("\n");

            // Row separator or bottom border
            if (row < rows - 1) {
                grid.append("├");
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
        grid.append("└");
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

    private void setFirstMove() {
        this.isFirstMove = false;
    }
}
