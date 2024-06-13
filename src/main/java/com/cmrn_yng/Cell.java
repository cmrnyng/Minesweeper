package com.cmrn_yng;

public class Cell {
    private int adjacentMines;
    private boolean mine;
    private boolean flagged;
    private boolean revealed;

    public Cell() {
        this.adjacentMines = 0;
        this.mine = false;
        this.flagged = false;
        this.revealed = false;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isMine() {
        return mine;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
