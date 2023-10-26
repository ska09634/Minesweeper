package cs1302.game;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * This class reads the seed file and creates a MinesweeperGame.
 */
public class MinesweeperGame {
    private int mines = 0;
    private int rounds = 0;
    private String[][] noFogArray;
    private String[][] fieldArray;
    private boolean[][] hasMine;
    private boolean noFog;
    private final Scanner stdIn;
    private String seedPath;

    /**
     * Creates a {@code MinesweeperGame} object with instance variables setup from the information
     * from the {@code seedPath} file. The initial values of {@code stdIn} and {@code seedPath}
     * are from the {@link MinesweeperDriver} class. The seed file is read and makes a new game.
     * @param stdIn the scanner that takes the player's command input
     * @param seedPath the seed file that will be read to create the game
     * @throws FileNotFoundException if the seed file is not found
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) throws FileNotFoundException {
        this.stdIn = stdIn;
        this.seedPath = seedPath;
        File file = new File(this.seedPath);
        Scanner scanner = new Scanner(file);
        int rows = 0;
        int cols = 0;
        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
            if (scanner.hasNextInt()) {
                cols = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    this.mines = scanner.nextInt();
                }
            }
        } else {
            System.err.println();
            System.err.println("Can't make game with " + seedPath +
                " because it is not formatted correctly");
            System.exit(1);
        } // if
        if (!(rows > 0 && rows <= 10 && cols > 0 && cols <= 10) || (this.mines > rows * cols)) {
            System.err.println();
            System.err.println("Can't make game with " + seedPath +
                " because it is not formatted correctly");
            System.exit(1);
        } // if
        hasMine = new boolean[rows][cols];
        fieldArray = new String[rows][cols];
        noFogArray = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                hasMine[i][j] = false;
                fieldArray[i][j] = "   ";
                noFogArray[i][j] = "   ";
            }
        } // for; makes 2d arrays
        int count = 0;
        for (int i = 0; i < this.mines; i++) {
            int row = 0, col = 0;
            if (scanner.hasNextInt()) {
                row = scanner.nextInt();
                count++;
                if (scanner.hasNextInt()) {
                    col = scanner.nextInt();
                    count++;
                    if ((row >= 0 && row < rows) && (col >= 0 && col < cols)) {
                        hasMine[row][col] = true;
                        count++;
                    }
                }
            }
        }
        if (count != 3 * mines) {
            System.err.println();
            System.err.println("Can't make game with " + seedPath
                + " because it is not formatted correctly");
            System.exit(1);
        } // if
    } // constructor

    /**
     * Prints the welcome banner to standard output.
     */
    public void printWelcome() {
        System.out.println("        _");
        System.out.println("  /\\/\\ (F)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\'__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                             ALPHA EDITION |_| v2021.sp");
        System.out.println();
    } // welcome

    /**
     * Prints the current contents of the minefield to standard output.
     */
    public void printMineField() {
        for (int i = 0; i < fieldArray.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < fieldArray[i].length; j++) {
                System.out.print(fieldArray[i][j]);
                if (j < fieldArray[i].length - 1) {
                    System.out.print("|");
                } // if
            } // for
            System.out.println("|");
        } // for
        System.out.print("    ");
        for (int i = 0; i < fieldArray[0].length; i++) {
            System.out.print(i + "   ");
        } // for
        System.out.println();
    } // minefield

    /**
     * Checks to see if the square the player types in has a mine or not.
     * If the square has a mine, the game ends.
     */
    private void hasMineField() {
        for (int i = 0; i < hasMine.length; i++) {
            System.out.print(i + "|");
            for (int j = 0; j < hasMine[i].length; j++) {
                if (hasMine[i][j] == true) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                } // if
                if (j < hasMine[i].length - 1) {
                    System.out.print("|");
                } // if
            } // for
            System.out.println("|");
        } // for
        System.out.print("  ");
        for (int i = 0; i < hasMine[0].length; i++) {
            System.out.print(i + " ");
        } // for
        System.out.println();
        System.out.println();
    } // hasMineField

    /**
     * Displays the current number of rounds that has been completed.
     */
    private void rounds() {
        System.out.println("Rounds Completed: " + rounds);
        System.out.println();
    } // rounds

   /**
    * Displays the bash command line where the player will input their command.
    */
    private void bash() {
        System.out.println();
        System.out.print("minesweeper-alpha: ");
    } // bash

    /**
     * Indicates whether or not the square is in the game grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return true if the square is in the game grid; false otherwise
     */
    private boolean isInBounds(int row, int col) {
        return (row < hasMine.length && row >= 0 && col >= 0 && col < hasMine[0].length);
    } // return

    /**
     * Displays the score the player recieved.
     * @return the score
     */
    private int score() {
        int score;
        score = fieldArray.length * fieldArray[0].length - mines - rounds;
        return score;
    } // score

    /**
     * Prints the game prompt to standard output and interpret user input
     * from standard input. Based on the command received, the method delegates other
     * methods to handle the work.
     */
    public void promptUser() {

        String fullCommand = stdIn.nextLine();
        Scanner commandScan = new Scanner(fullCommand);
        String command = commandScan.next();

        switch (command) {
        case "h":
        case "help":
            help();
            break;

        case "q":
        case "quit":
            quit();
            break;

        case "g":
        case "guess":
            if (!guess(commandScan)) {
                rounds++;
                System.err.println("Invalid Command: Command not recognized!");
                System.out.println();
            } // if
            break;

        case "m":
        case "mark":
            if (!mark(commandScan)) {
                rounds++;
                System.err.println("Invalid Command: Command not recognized!");
                System.out.println();
            } // if
            break;

        case "r":
        case "reveal":
            if (!reveal(commandScan)) {
                rounds++;
                System.err.println("Invalid Command: Command not recognized!");
                System.out.println();
            } // if
            break;

        case "nofog":
            noFog();
            break;

        default:
            System.err.println("Invalid Command: Command not recognized!");
            System.out.println();
        } // switch
    } // promptuser

    /**
     * Displays the commands available to the player.
     */
    private void help() {
        this.rounds++;
        System.out.println();
        System.out.println("Commands Available...");
        System.out.println("- Reveal: r/reveal row col");
        System.out.println("- Mark: m/mark row col");
        System.out.println("- Guess: g/guess row col");
        System.out.println("- Help: h/help");
        System.out.println("- Quit: q/quit");
        System.out.println();
    } // help

    /**
     * Quits the game.
     */
    private void quit() {
        System.out.println();
        System.out.println("Quitting the game...");
        System.out.println("Bye!");
        System.exit(0);
    } // quit

    /**
     * Labels the square the player chooses as ' ? ' if they think a mine
     * is there.
     *
     * @param stdIn the scanner that takes the player's command input
     * @return true if the square is labeled ' ? ' and is in bounds
     */
    private boolean guess(Scanner stdIn) {
        boolean guessed = false;
        int row = 0;
        int col = 0;
        if (stdIn.hasNextInt()) {
            row = stdIn.nextInt();
            if (stdIn.hasNextInt()) {
                col = stdIn.nextInt();
                if (!stdIn.hasNextInt() && isInBounds(row, col)) {
                    guessed = true;
                    rounds++;
                    fieldArray[row][col] = " ? ";
                    System.out.println();
                }
            }
        }
        return guessed;
    } // guess

    /**
     * Marks the square the player thinks has a mine with an 'F'.
     *
     * @param stdIn the scanner that takes the player's command input
     * @return true if the square has been marked and is in bounds
     */
    private boolean mark(Scanner stdIn) {
        boolean marked = false;
        int row = 0;
        int col = 0;
        if (stdIn.hasNextInt()) {
            row = stdIn.nextInt();
            if (stdIn.hasNextInt()) {
                col = stdIn.nextInt();
                if (!stdIn.hasNextInt() && isInBounds(row, col)) {
                    marked = true;
                    rounds++;
                    fieldArray[row][col] = " F ";
                    System.out.println();
                }
            }
        }
        return marked;
    } // mark

    /**
     * Reveals the square of the coordinates the player types in. Either returns the
     * of mines adjacent to the current square or reveals a mine, ending the game.
     *
     * @param stdIn the scanner that takes the player's command input
     * @return true if the square has been revealed and is in bounds
     */
    private boolean reveal(Scanner stdIn) {
        boolean revealed = false;
        int row = 0;
        int col = 0;
        if (stdIn.hasNextInt()) {
            row = stdIn.nextInt();
            if (stdIn.hasNextInt()) {
                col = stdIn.nextInt();
                if (!stdIn.hasNextInt() && isInBounds(row, col)) {
                    revealed = true;
                    rounds++;
                    if (!hasMine[row][col]) {
                        fieldArray[row][col] = " "
                            + getNumAdjMines(row, col)
                            + " ";
                        System.out.println();
                    } else {
                        printLoss();
                    }
                }
            }
        }
        return revealed;
    } // reveal

    /**
     * Returns {@code true} if, and only if, all the conditions are met
     * to win the game.
     * @return true if all the conditions are met; false otherwise
     */
    public boolean isWon() {
        boolean minesRevealed = true;
        boolean squaresRevealed = true;

        for (int i = 0; i < fieldArray.length; i++) {
            for (int j = 0; j < fieldArray[0].length; j++) {
                if (hasMine[i][j]) {
                    if (fieldArray[i][j] != " F ") {
                        minesRevealed = false;
                    }
                } else {
                    if (fieldArray[i][j] == " F " || fieldArray[i][j] == " ? " ||
                        fieldArray[i][j] == "") {
                        squaresRevealed = false;
                    } // if
                } // else
            } // for j
        } // for i
        return minesRevealed && squaresRevealed;
    } // isWon

    /**
     * Prints the win banner congratulating the player along with
     * the score.
     */
    public void printWin() {
        System.out.println();
        System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
        System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
        System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
        System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
        System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
        System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
        System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
        System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
        System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
        System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
        System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
        System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
        System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
        System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
        System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
        System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
        System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE:" + score());
        System.out.println();
        System.exit(0);
    } // printWin

    /**
     * Prints the game over message to standard error.
     */
    public void printLoss() {
        System.out.println();
        System.out.println(" Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/ ");
        System.out.println();
        System.exit(0);
    } // printLoss

    /**
     * Shows the positions of the mines in the grid.
     */
    private void noFog() {
        rounds++;
        noFog = true;
        System.out.println();
    } // noFog

    /**
     * Prints the grid similar to the minefield of where the
     * the mines on the grid are. Can be accessed if the player
     * types in 'nofog' into the prompt.
     */
    private void noFogField() {
        for (int i = 0; i < noFogArray.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < noFogArray[i].length; j++) {
                if (hasMine[i][j]) {
                    System.out.print("<" + fieldArray[i][j].substring(1, 2) + ">");
                } else {
                    System.out.print(fieldArray[i][j]);
                }
                if (j < noFogArray[i].length - 1) {
                    System.out.print("|");
                } // if
            }
            System.out.println("|");
        } // for
        System.out.print("    ");
        for (int i = 0; i < fieldArray[0].length; i++) {
            System.out.print(i + "   ");
        } // for
        System.out.println();
    } // noFogField

    /**
     * Prints the display for each turn of the game.
     */
    public void turn() {
        rounds();
        if (noFog) {
            noFogField();
            noFog = false;
        } else {
            printMineField();
        } // if
        bash();
        promptUser();
    } // turn

    /**
     * Provides the main game loop.
     */
    public void play() {
        printWelcome();

        while (true) {
            turn();
            if (isWon()) {
                break;
            } // if
        } // while
        printWin();
    } // play

  /**
   * Returns the number of mines adjacent to the specified
   * square in the grid.
   *
   * @param row the row index of the square
   * @param col the column index of the square
   * @return the number of adjacent mines
   */
    private int getNumAdjMines(int row, int col) {
        int adjMines = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            if (!(i >= 0 && i < fieldArray.length)) {
                continue;
            }
            for (int j = col - 1; j <= col + 1; j++) {
                if ((i == row && j == col) || (!(j >= 0 && j < fieldArray[0].length))) {
                    continue;
                } else {
                    if (hasMine[i][j]) {
                        adjMines++;
                    }
                }
            }
        }
        return adjMines;
    } // getNumAdjMines
} // class
