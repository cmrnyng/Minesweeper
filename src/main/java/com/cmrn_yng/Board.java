package com.cmrn_yng;

import java.util.Random;

public class Board {
    private int rows;
    private int cols;
    private Cell[][] cells;

    public Board(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        cells = new Cell[rows][cols];
        
        initialiseBoard();
        placeMines(mines);
        calculateAdjacentMines();
        logBoard();
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

    private boolean isValidPosition(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }

    public void logBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMine()) {
                    System.out.print("* ");
                } else {
                    System.out.print(cells[row][col].getAdjacentMines() + " ");
                }
            }
            System.out.println(); // Move to the next line after each row
        }
    }
}
