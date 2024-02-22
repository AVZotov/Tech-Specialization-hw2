package ru.geekbrains;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

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
        HashMap<Integer, Integer> activeTurn;

        while (isGameActive){
            activeTurn = userTurn();
            printField();
            int gameStatus = checkDiagonalRightLeft(activeTurn);

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

    private HashMap<Integer, Integer> userTurn() {
        int x = 0;
        int y = 0;
        boolean isCellEmpty = false;

        while (!isCellEmpty) {
            x = getCoordinateFromUser("Enter X (diagonal) coordinate:");
            y = getCoordinateFromUser("Enter Y (vertical) coordinate:");

            if (gameField[y - 1][x - 1] != emptyMarker){
                System.out.println("Error! Cell is occupied! Please try again!");
                continue;
            }

            isCellEmpty = true;
            }

        gameField[y - 1][x - 1] = playerMarker;
        turnsCount ++;
        HashMap<Integer, Integer> turn = new HashMap<>();
        turn.put(x - 1, y - 1);
        return turn;
    }

    private Integer checkHorizontal(HashMap<Integer, Integer> userTurn){
        int xPosition = userTurn.keySet().iterator().next();
        int yPosition = userTurn.get(xPosition);
        char[] combination = new char[fieldSize];
        System.arraycopy(gameField[yPosition], 0, combination, 0, fieldSize);
        return checkCombination(combination);
    }

    private Integer checkVertical(HashMap<Integer, Integer> turn){
        int xPosition = turn.keySet().iterator().next();
        char[] combination = new char[fieldSize];

        for (int y = 0; y < fieldSize; y++) {
            combination[y] = gameField[y][xPosition];
        }
        return checkCombination(combination);
    }

    private Integer checkDiagonalLeftRight(HashMap<Integer, Integer> turn){
        HashMap<Integer, Integer> initialPosition = getStartPointDiagonal(turn, true);
        int xPosition = initialPosition.keySet().iterator().next();
        int yPosition = initialPosition.get(xPosition);
        char[] combination = new char[fieldSize];
        int counter = 0;

        while (xPosition < fieldSize && yPosition < fieldSize){
            combination[counter] = gameField[yPosition][xPosition];
            counter++;
            xPosition++;
            yPosition++;
        }

        return checkCombination(combination);
    }

    private Integer checkDiagonalRightLeft(HashMap<Integer, Integer> turn){
        HashMap<Integer, Integer> initialPosition = getStartPointDiagonal(turn, false);
        int xPosition = initialPosition.keySet().iterator().next();
        int yPosition = initialPosition.get(xPosition);
        char[] combination = new char[fieldSize];
        int counter = 0;

        while (xPosition < fieldSize && yPosition >= 0){
            combination[counter] = gameField[yPosition][xPosition];
            counter++;
            xPosition++;
            yPosition--;
        }

        return checkCombination(combination);
    }

    private HashMap<Integer, Integer> getStartPointDiagonal(HashMap<Integer, Integer> currentPosition, boolean isLeftToRight){
        int xPosition = currentPosition.keySet().iterator().next();
        int yPosition = currentPosition.get(xPosition);
        int counter = Math.max(xPosition, yPosition);
        HashMap<Integer, Integer> initialPosition = new HashMap<>();

        if (isLeftToRight) {
            for (int i = counter; i >=0 ; i--) {
                if (xPosition == 0 || yPosition == 0){
                    initialPosition.put(xPosition, yPosition);
                    break;
                }

                xPosition--;
                yPosition--;
            }
        }

        if (!isLeftToRight){
            for (int i = counter; i >=0 ; i--) {
                if (xPosition == 0 || yPosition == fieldSize - 1){
                    initialPosition.put(xPosition, yPosition);
                    break;
                }

                xPosition--;
                yPosition++;
            }
        }

        return initialPosition;
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

            if (combination[i] == emptyMarker || combination[i] == 0){
                counter = 0;
            }

            if (counter == repeatsInLineToWin){
                return combination[i] == playerMarker ? playerWin : aiWin;
            }
        }

        return noCombination;
    }

    private Integer getCoordinateFromUser(String message){
        boolean isCorrectValue = false;
        int value = 0;

        while (!isCorrectValue) {
            System.out.println(message);
            while (!scanner.hasNextInt()) {
                System.out.println("Error! That's not a number! Please try again!");
                scanner.next();
            }
            value = scanner.nextInt();

            if (value > fieldSize || value <= 0){
                System.out.println("Entered value is out of field. Please try again");
                continue;
            }

            isCorrectValue = true;
        }

        return value;
    }

    private boolean isDraw(){
        return turnsCount == fieldSize * fieldSize;
    }

}
