package com.cmrn_yng;

public class Cell {
    private int adjacentMines;
    private boolean mine;
    private boolean flagged;
    private boolean revealed;

    /**
     * Constructor for the Cell
     */
    public Cell() {
        this.adjacentMines = 0;
        this.mine = false;
        this.flagged = false;
        this.revealed = false;
    }

    /**
     * Sets the number of adjacent mines
     * @param adjacentMines the number to set adjacent mines to
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    /**
     * Gets the number of adjacent mines
     * @return the number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Toggles a cell as a mine
     * @param mine boolean, whether cell should be a mine or not
     */
    public void setMine(boolean mine) {
        this.mine = mine;
    }

    /**
     * Checks if a cell is a mine
     * @return whether a cell is a mine
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Toggles a cell as flagged
     * @param flagged boolean, whether cell should be flagged or not
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Checks if a cell is flagged
     * @return whether a cell is flagged
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Toggles a cell as revealed
     * @param revealed boolean, whether cell should be revealed or not
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Checks if a cell is revealed
     * @return whether a cell is revealed
     */
    public boolean isRevealed() {
        return revealed;
    }
}
