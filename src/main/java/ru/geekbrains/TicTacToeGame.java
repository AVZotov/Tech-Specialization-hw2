package ru.geekbrains;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class TicTacToeGame {
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final int fieldSize;
    private final char playerMarker = 'X';
    private final char aiMarker = 'O';
    private final char emptyMarker = '_';
    private char[][] gameField;
    private int turnsCount = 0;
    private final int repeatsInLineToWin;

    public TicTacToeGame(int fieldSize) {
        this.fieldSize = fieldSize;
        repeatsInLineToWin = fieldSize;
        startGame();
    }

    public TicTacToeGame(int fieldSize, int repeatsInLineToWin){
        this.fieldSize = fieldSize;
        this.repeatsInLineToWin = repeatsInLineToWin;
        startGame();
    }

    private void startGame() {
        boolean isEasyLevel = getAiLevel();
        boolean isGameActive = true;
        initializeField();
        printField();

        while (isGameActive){
            userTurn();
            printField();
            int gameStatus = isCombinationDown();

            System.out.println(gameStatus);
            if (gameStatus != 0){
                isGameActive = false;
                System.out.println(gameStatus == 1 ? "Player Win" : "AI Wins");
            }
        }
    }

    private boolean getAiLevel() {
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

        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                gameField[y][x] = emptyMarker;
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
        turnsCount ++;
    }


    private Integer isCombinationRight(){
        int result = 0;
        char[] combination = new char[fieldSize];

        for (int y = 0; y < fieldSize; y++) {
            System.arraycopy(gameField[y], 0, combination, 0, fieldSize);
            result = checkCombination(combination);
            if (result != 0) { return result; }
        }

        return result;
    }

    private Integer isCombinationDown(){
        int result = 0;
        char[] combination = new char[fieldSize];

        for (int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                combination[y] = gameField[y][x];
            }
            result = checkCombination(combination);
            if (result != 0){return result;}
        }
        return result;
    }

    private boolean checkCombinationRightDown(int length){
        int result = 0;
        char[] combination = new char[fieldSize];
        return false;
    }

    private boolean checkCombinationUpLeft(int length){
        return false;
    }

    private Integer checkCombination(char[] combination) {
        int counter = 0;
        int noCombination = 0;
        int playerWin = 1;
        int aiWin = 2;

        for (int i = 0; i < combination.length; i++) {
            if (i != 0 && combination[i] != combination[i - 1]){
                counter = 0;
            }

            counter++;

            if (combination[i] == emptyMarker){
                counter = 0;
            }

            if (counter == repeatsInLineToWin){
                return combination[i] == playerMarker ? playerWin : aiWin;
            }
        }

        return noCombination;
    }

    private boolean isDraw(){
        return turnsCount == fieldSize * fieldSize;
    }

}
