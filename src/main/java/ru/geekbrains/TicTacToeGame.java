package ru.geekbrains;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Simple ticTacToe game designed as a solution to second homework
 */
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

    /**
     * Simple constructor which implements default length of win combination
     * which equals to field size
     * @param fieldSize the size of the square field
     */
    public TicTacToeGame(int fieldSize) {
        this.fieldSize = fieldSize;
        repeatsInLineToWin = fieldSize;
        startGame();
    }

    /**
     * Custom constructor
     * @param fieldSize the size of the square field
     * @param repeatsInLineToWin custom length of repeats to win
     */
    public TicTacToeGame(int fieldSize, int repeatsInLineToWin){
        this.fieldSize = fieldSize;
        this.repeatsInLineToWin = repeatsInLineToWin;
        startGame();
    }

    /**
     * EntryPoint method for the game to start
     */
    private void startGame() {
        boolean isEasyLevel = getAiLevel();
        initializeField();
        printField();
        HashMap<Integer, Integer> activeTurn;
        int gameStatus;

        if (!isEasyLevel) {
            System.out.println("Warning! Hard level is not implemented. Switching to easy level");
            isEasyLevel = true;
        }

        while (true){
            activeTurn = userTurn();
            printField();
            gameStatus = checkGameStatus(activeTurn);

            if (gameStatus != 0){
                printGameStatus(gameStatus);
                return;
            }

            if (isEasyLevel) {
                activeTurn = aiEasyTurn();
                printField();
                gameStatus = checkGameStatus(activeTurn);

                if (gameStatus != 0){
                    printGameStatus(gameStatus);
                    return;
                }
            }
        }
    }

    /**
     * Console messenger to inform the user about game status
     * @param gameStatus digit code which receive 1, 2, 3 value to determine if player win or AI
     *                   or it a draw in a game
     */
    private void printGameStatus(int gameStatus){
        if(gameStatus == 3){
            System.out.println("There is a draw!");
        }

        System.out.println(gameStatus == 1 ? "Player Win" : "AI Wins");
    }

    /**
     * Console user interface to interact with user to get the preferred level of AI strength
     * @return return true if easy level selected
     */
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

    /**
     * Method to fill the field with empty markers on game start
     */
    private void initializeField() {
        gameField = new char[this.fieldSize][this.fieldSize];

        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                gameField[y][x] = emptyMarker;
            }
        }
    }

    /**
     * Method to print the game to the console
     */
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

    /**
     * Method to interact with user and get both x and y coordinates
     * Occupied cells check implemented to avoid incorrect turns
     * @return Map with x and y coordinates with x as key
     */
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

    /**
     * AI turns generator for easy level of AI with checks for occupied cells turns
     * @return Map with x and y coordinates with x as key
     */
    private HashMap<Integer, Integer> aiEasyTurn(){
        HashMap<Integer, Integer> turn = new HashMap<>();
        boolean isEmptyCell = false;

        while (!isEmptyCell){
            int x = random.nextInt(fieldSize);
            int y = random.nextInt(fieldSize);

            if (gameField[y][x] != emptyMarker){
                continue;
            }

            isEmptyCell = true;
            gameField[y][x] = aiMarker;
            turn.put(x, y);
        }

        turnsCount++;
        return turn;
    }

    /**
     * supporting method to check if it is draw win or loose for player
     * @param turn receiving last turn as a Map with x and y coordinates with x as key
     * @return code value there 1:player wins, 2: AI wins and 3: is Draw
     */
    private Integer checkGameStatus(HashMap<Integer, Integer> turn){
        int result;
        int noCombination = 0;
        int draw = 3;

        if (isDraw()){return draw;}
        result = checkHorizontal(turn);
        if (result != noCombination){return result;}
        result = checkVertical(turn);
        if (result != noCombination){return result;}
        result = checkDiagonalLeftRight(turn);
        if (result != noCombination){return result;}
        return checkDiagonalRightLeft(turn);
    }

    /**
     * Method to check if it is a horizontal combination from the last turn
     * @param turn receiving last turn as a Map with x and y coordinates with x as key
     * @return code value there 1:player wins, 2: AI wins and 3: is Draw
     */
    private Integer checkHorizontal(HashMap<Integer, Integer> turn){
        int xPosition = turn.keySet().iterator().next();
        int yPosition = turn.get(xPosition);
        char[] combination = new char[fieldSize];
        System.arraycopy(gameField[yPosition], 0, combination, 0, fieldSize);
        return checkCombination(combination);
    }

    /**
     * Method to check if it is a vertical combination from the last turn
     * @param turn receiving last turn as a Map with x and y coordinates with x as key
     * @return code value there 1:player wins, 2: AI wins and 3: is Draw
     */
    private Integer checkVertical(HashMap<Integer, Integer> turn){
        int xPosition = turn.keySet().iterator().next();
        char[] combination = new char[fieldSize];

        for (int y = 0; y < fieldSize; y++) {
            combination[y] = gameField[y][xPosition];
        }
        return checkCombination(combination);
    }

    /**
     * Method to check if it is a diagonal combination from the last turn
     * @param turn receiving last turn as a Map with x and y coordinates with x as key
     * @return code value there 1:player wins, 2: AI wins and 3: is Draw
     */
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

    /**
     * Method to check if it is a diagonal combination from the last turn
     * @param turn receiving last turn as a Map with x and y coordinates with x as key
     * @return code value there 1:player wins, 2: AI wins and 3: is Draw
     */
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

    /**
     * Support method to search the start point of diagonals
     * @param currentPosition Map with x and y coordinates with x as key of the last turn
     * @param isLeftToRight flag to change the logic for the initial position for combination to search
     * @return Map with x and y coordinates with x as key
     */
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

    /**
     * Support method to determine if there is a repeat in a sequence
     * @param combination passed to method combination which need to be checked for repeats
     * @return Integer as code for win status or no combinations
     */
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

    /**
     * Support method to get coordinated from user
     * through the console with checks if it is an integer and if the value in a boundaries of the field
     * @param message custom message prior to user enter the value
     * @return desired coordinate
     */
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

    /**
     * method to check if possible steps available
     * @return true if draw
     */
    private boolean isDraw(){
        return turnsCount == fieldSize * fieldSize;
    }
}
