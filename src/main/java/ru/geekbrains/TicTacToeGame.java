package ru.geekbrains;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TicTacToeGame {
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final int fieldSize;
    private final char playerMarker = 'X';
    private final char aiMarker = 'O';
    private final char emptyMarker = '_';
    private char[][] gameField;

    public TicTacToeGame(int fieldSize) {
        this.fieldSize = fieldSize;
        startGame();
    }

    private void startGame() {
        initializeField();
        printField();
        userTurn();
        printField();
    }

    private boolean getMenuSelection() {
        System.out.println("Welcome to TicTacToeGame!\nPlease select level to start the game\n1. Easy\n2.Hard\nEnter your choose:");
        boolean isSelectionMade = false;
        String userInput = "";

        while (!isSelectionMade) {
            userInput = scanner.nextLine();
            if (userInput.equals("1") || userInput.equals("2")) {
                isSelectionMade = true;
            } else {
                System.out.println("Wrong input, please try again!");
            }
        }

        return userInput.equals("1");
    }

    private void initializeField() {
        gameField = new char[this.fieldSize][this.fieldSize];

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                gameField[i][j] = emptyMarker;
            }
        }
    }

    private void printField() {
        for (int i = 0; i < fieldSize; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSize; j++) {
                System.out.print(gameField[i][j] + "|");
            }
            System.out.println();
        }

        System.out.print("*");
        for (int i = 0; i < fieldSize; i++) {
            System.out.printf(" " + (i + 1));
        }
        System.out.println();
    }

    private void userTurn() {
        int x;
        int y;
        System.out.println("Enter X (diagonal) coordinate:");
        while (!scanner.hasNextInt()) {
            System.out.println("Error! That's not a number!");
            scanner.next();
        }
        x = scanner.nextInt();
        System.out.println("Enter Y (vertical) coordinate:");
        while (!scanner.hasNextInt()) {
            System.out.println("Error! That's not a number!");
            scanner.next();
        }
        y = scanner.nextInt();

        gameField[y - 1][x - 1] = playerMarker;
    }
}
