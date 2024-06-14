package com.cmrn_yng;

import java.util.Scanner;

public class Game implements GameStateCallback {
    Board board;
    private boolean ongoing;

    public Game() {
        this.ongoing = true;
        System.out.println("\n┌───────────────────────┐\n│ M I N E S W E E P E R │\n└───────────────────────┘");
        board = difficultySelect();
        playGame();
    }

    public Board difficultySelect() {
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
                        board = new Board(4, 4, 1, this);
                        break;
                    case 2:
                        board = new Board(16, 16, 40, this);
                        break;
                    case 3:
                        board = new Board(16, 30, 99, this);
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

    private void playGame() {
        while (ongoing) {
            board.logBoard();
            int[] move = move();
            board.cellAction(move[0], move[1], (char) move[2]);
            if (board.hasPlayerWon()) gameWin();
        }
    }

    public int[] move() {
        Scanner scanner = new Scanner(System.in);
        int[] move;
        while (true) {
            System.out.println("Enter your move in the following format:");
            System.out.println("row, column, (F/R)");
            System.out.println("Where F = flag, R = reveal");
            System.out.println("Example: 4, 3, R");
            String input = scanner.nextLine();
            try {
                move = convertInput(input);
                break;
            } catch (Exception e) {
                System.out.println("Invalid move.\n");
            }
        }

        if (!board.isValidPosition(move[0], move[1])) {
            System.out.println("Please enter a position which is within the board.\n");
            return move();
        }

        return move;
    }

    private int[] convertInput(String input) throws Exception {
        String[] parts = input.split(",");
        if (parts.length != 3) throw new Exception();

        int row = Integer.parseInt(parts[0].trim()) - 1;
        int col = Integer.parseInt(parts[1].trim()) - 1;
        char action = parts[2].trim().toLowerCase().charAt(0);

        if (action != 'f' && action != 'r') throw new Exception();

        return new int[]{row, col, action};
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    @Override
    public void gameOver() {
        setOngoing(false);
        System.out.println("\n┌──────────────────┐\n│ G A M E  O V E R │\n└──────────────────┘\n");
        System.out.println("You stepped on a mine!");
        board.logBoard();
        playOrExit();
    }

    @Override
    public void gameWin() {
        setOngoing(false);
        System.out.println("\n┌──────────────┐\n│ Y O U  W I N │\n└──────────────┘");
        board.logBoard();
        playOrExit();

    }

    private void playOrExit() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Type 'play' to play again, or 'exit' to exit");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("play")) {
                new Game();
                break;
            } else if (response.equalsIgnoreCase("exit")) {
                System.out.println("Thanks for playing.");
                System.exit(0);
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
