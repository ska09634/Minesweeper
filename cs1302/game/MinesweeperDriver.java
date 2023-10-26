package cs1302.game;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Enters the {@code MinesweeperGame}.
 */
public class MinesweeperDriver {
    /**
     * Creates the standard input {@code Scanner} stdIn object which takes
     * in the player's command. This method also expects only one command line argument
     * that represents the path to the seed file. The file is assigned to a {@code String}
     * cariable called {@code seedPath} so that it can be used in the {@code MinesweeperGame}
     * constructor. Once the scanner and file objects are created, the play() method is called.
     *
     * @param args the command line argument
     */
    public static void main(String[] args) {
        try {
            Scanner stdIn = new Scanner(System.in);
            String seedPath = args[0];
            MinesweeperGame game = new MinesweeperGame(stdIn, seedPath);
            game.play();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Can't create game because it is not formatted correctly.");
        } // try
    } // main
} // class
