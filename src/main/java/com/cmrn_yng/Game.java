package com.cmrn_yng;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    Board board;
    private boolean ongoing;

    /**
     * Constructor to create a Game with a board (testing purposes)
     * @param board the board
     */
    public Game(Board board) {
        this.board = board;
    }

    /**
     * Constructor to create a Game without a board - standard approach
     */
    public Game() {
        this.board = null;
    }

    /**
     * Prompts the user to select the difficulty
     * @return a Board with the requested settings
     */
    public Board difficultySelect() {
        Scanner input = new Scanner(System.in);
        int difficulty;
        Board board;

        while (true) {
            System.out.println("\nSelect your difficulty\n──────────────────────\n1 - Easy\n2 - Medium\n3 - Hard");

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

    /**
     * Starts and plays through a Minesweeper game
     */
    public void playGame() {
        this.ongoing = true;
        System.out.println("\n┌───────────────────────┐\n│ M I N E S W E E P E R │\n└───────────────────────┘");
        board = difficultySelect();

        while (ongoing) {
            board.logBoard();
            int[] move = move();
            board.cellAction(move[0], move[1], move[2]);
            if (board.hasPlayerWon()) gameWin();
            if (board.getGameOver()) gameOver();
        }
    }

    /**
     * Takes the user's move
     * @return the move as an integer array
     */
    public int[] move() {
        Scanner scanner = new Scanner(System.in);
        int[] move;
        while (true) {
            System.out.println("Enter your move in the following format:");
            System.out.println("column, row, (F/R)");
            System.out.println("Where F = flag, R = reveal");
            System.out.println("Example: 4, 3, R");
            String input = scanner.nextLine().trim();
            if (isValidInput(input)) {
                move = convertInput(input);
                if (!board.isValidPosition(move[0], move[1])) {
                    System.out.println("Please enter a position which is within the board.\n");
                    continue;
                }
                break;
            } else {
                System.out.println("Invalid move.\n");
            }
        }
        return move;
    }

    /**
     * Checks if user input is valid
     * @param input the user's input
     * @return      true if valid input, false if invalid
     */
    private boolean isValidInput(String input) {
        Pattern pattern = Pattern.compile("^\\d+,\\s?\\d+,\\s?[FR]$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * Converts user input to an array representing the move
     * @param input the user's input
     * @return      an integer array containing the
     */
    private int[] convertInput(String input) {
        String[] parts = input.split(",");

        int row = Integer.parseInt(parts[1].trim()) - 1;
        int col = Integer.parseInt(parts[0].trim()) - 1;
        int action = parts[2].trim().equalsIgnoreCase("f") ? 1 : 0;

        return new int[]{row, col, action};
    }

    /**
     * Sets attribute 'ongoing'
     * @param ongoing boolean for if the game is ongoing or not
     */
    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    /**
     * Ends the game as a loss
     */
    public void gameOver() {
        setOngoing(false);
        System.out.println("\n┌──────────────────┐\n│ G A M E  O V E R │\n└──────────────────┘\n");
        System.out.println("You stepped on a mine!");
        board.logBoard();
        playOrExit();
    }

    /**
     * Ends the game as a win
     */
    public void gameWin() {
        setOngoing(false);
        System.out.println("\n┌──────────────┐\n│ Y O U  W I N │\n└──────────────┘");
        board.logBoard();
        playOrExit();
    }

    /**
     * Checks if user wants to play again or exit the game
     */
    private void playOrExit() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Type 'play' to play again, or 'exit' to exit");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("play")) {
                Game game = new Game();
                game.playGame();
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
