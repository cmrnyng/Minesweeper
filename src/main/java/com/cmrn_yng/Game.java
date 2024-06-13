package com.cmrn_yng;

import java.util.Scanner;

public class Game {
    Board board;

    public Game() {
        System.out.println("\n─────────────────────\nM I N E S W E E P E R\n─────────────────────");
        board = difficultySelect();
    }

    public static Board difficultySelect() {
        Scanner input = new Scanner(System.in);
        int difficulty;
        Board board;

        while (true) {
            System.out.println();
            System.out.println("Select your difficulty");
            System.out.println("──────────────────────");
            System.out.println("1 - Easy");
            System.out.println("2 - Medium");
            System.out.println("3 - Hard");

            if (input.hasNextInt()) {
                difficulty = input.nextInt();
                switch (difficulty) {
                    case 1:
                        board = new Board(9, 9, 10);
                        break;
                    case 2:
                        board = new Board(16, 16, 40);
                        break;
                    case 3:
                        board = new Board(16, 30, 99);
                        break;
                    default:
                        System.out.println("Invalid selection. Please enter 1, 2, or 3.");
                        input.nextLine();
                        continue;
                }
                break;
            } else {
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                input.next();
            }
        }
        return board;
    }

    private void turn() {
        board.logBoard();
        System.out.println("Enter your move in the following format:");
        System.out.println("row, column, (F/R)");
        System.out.println("Where F = flag, R = reveal");
        System.out.println("Example: 4, 3, R");
    }
}
