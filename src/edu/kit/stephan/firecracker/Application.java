package edu.kit.stephan.firecracker;

import edu.kit.informatik.Terminal;
import edu.kit.stephan.firecracker.view.Session;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SyntaxException;
import edu.kit.stephan.firecracker.model.firebreaker.board.FireStation;
import edu.kit.stephan.firecracker.model.firebreaker.board.Forest;
import edu.kit.stephan.firecracker.model.firebreaker.board.GameBoard;
import edu.kit.stephan.firecracker.model.firebreaker.board.GameField;
import edu.kit.stephan.firecracker.model.firebreaker.board.Lake;
import edu.kit.stephan.firecracker.model.firebreaker.player.Player;
import edu.kit.stephan.firecracker.core.Input;
import edu.kit.stephan.firecracker.core.Output;
import edu.kit.stephan.firecracker.view.command.CommandParserFireBreaker;

import java.util.Scanner;

/**
 * The Application. Creates the needed instances and runs the interactive command processing.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public final class Application {
    private static final String INPUT_SEPARATOR = ",";
    private static final String REGEX_ODD_INTEGER = "[0-9]*[13579]";
    private static final String REGEX_INFINITE_AMOUNT_OF_ZEROS = "[0]*";
    private static final String REGEX_COMMAND_PARAMETER
            = REGEX_ODD_INTEGER + INPUT_SEPARATOR + REGEX_ODD_INTEGER + INPUT_SEPARATOR
            + Player.REPRESENTATION_FOR_PLAYER_A + INPUT_SEPARATOR + "((A0|B0|C0|D0|[CDL+d*w]),)++B";
    private static final String REGEX_FOR_FOREST = "[*+dw]";
    private static final String REGEX_FOR_FIRE_STATION = "[A-D]";
    private static final int INDEX_OF_FIRST_STATION = 0;
    private static final int INDEX_ON_WHICH_THE_VALUES_OF_GAME_BOARD_START = 2;
    private static final Output OUTPUT = Terminal::printLine;
    private static final Output ERROR_OUTPUT = Terminal::printError;
    private static final Input INPUT = Terminal::readLine;




    /**
     * Utility class constructor
     */
    private Application() {
        throw new IllegalStateException("Utility-class constructor.");
    }

    /**
     * The main entry point to the application.
     *
     * @param args the command-line arguments to define the game board.
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            ERROR_OUTPUT.output(Errors.PARAMETERS_ARE_WRONG);
        }
        else {
            GameBoard gameBoard;
            try {
                gameBoard = checkCommandParameterAndCreateGameBoard(args[0]);
            } catch (SyntaxException e) {
                ERROR_OUTPUT.output(e.getMessage());
                return;
            }
            var session = new Session(OUTPUT, ERROR_OUTPUT, INPUT, new CommandParserFireBreaker(), gameBoard);
            session.interactive();
        }
    }


    private static String createRegex(long rowLength, long columnLength) {
        StringBuilder regex = new StringBuilder();
        regex.append(REGEX_INFINITE_AMOUNT_OF_ZEROS)
                .append(rowLength)
                .append(INPUT_SEPARATOR)
                .append(REGEX_INFINITE_AMOUNT_OF_ZEROS)
                .append(columnLength)
                .append(INPUT_SEPARATOR);
        for (long i = 0; i < rowLength * columnLength; i++) {
            /*
            fireStations, which are in each corner.
             */
            if (i == 0) regex.append(Player.REPRESENTATION_FOR_PLAYER_A + INPUT_SEPARATOR);
            else if (i == rowLength * columnLength - 1) regex.append(Player.REPRESENTATION_FOR_PLAYER_B);
            else if (i == columnLength - 1) regex.append(Player.REPRESENTATION_FOR_PLAYER_D + INPUT_SEPARATOR);
            else if (i == columnLength * (rowLength - 1))
                regex.append(Player.REPRESENTATION_FOR_PLAYER_C + INPUT_SEPARATOR);
            /*
            lakes, which are positioned between two fire stations.
             */
            else if (i == columnLength / 2 || i == (columnLength * rowLength - ((columnLength / 2) + 1))
                    || i == rowLength / 2 * columnLength
                    || i == (rowLength / 2 + 1) * columnLength - 1) regex.append(Lake.REPRESENTATION_OF_LAKE
                    + INPUT_SEPARATOR);
            /*
            fireBrigades, which are diagonal one apart from the corresponding fire station.
             */
            else if (i == columnLength + 1) regex.append(Player.REPRESENTATION_FOR_PLAYER_A
                    + INDEX_OF_FIRST_STATION + INPUT_SEPARATOR);
            else if (i == 2 * columnLength - 2) regex.append(Player.REPRESENTATION_FOR_PLAYER_D
                    + INDEX_OF_FIRST_STATION + INPUT_SEPARATOR);
            else if (i == (rowLength - 2) * columnLength + 1) regex.append(Player.REPRESENTATION_FOR_PLAYER_C
                    + INDEX_OF_FIRST_STATION + INPUT_SEPARATOR);
            else if (i == (rowLength - 1) * columnLength - 2) regex.append(Player.REPRESENTATION_FOR_PLAYER_B
                    + INDEX_OF_FIRST_STATION + INPUT_SEPARATOR);
            else regex.append(REGEX_FOR_FOREST).append(INPUT_SEPARATOR);
        }
        return regex.toString();
    }



    private static GameBoard checkCommandParameterAndCreateGameBoard(String input) throws SyntaxException {
        if (!input.matches(REGEX_COMMAND_PARAMETER)) throw new SyntaxException(Errors.REGEX_OF_GAME_BOARD_IS_WRONG);
        String[] inputSplit = input.split(INPUT_SEPARATOR);
        int numberOfRows = checkInteger(inputSplit[0]);
        int numberOfColumns = checkInteger(inputSplit[1]);

        if (!input.matches(createRegex(numberOfRows, numberOfColumns)))
            throw new SyntaxException(Errors.REGEX_OF_GAME_BOARD_IS_WRONG);

        if (!(input.contains(Forest.ConditionOfForestSection.BIG_FIRE.getRepresentationAsString())
                && input.contains(Forest.ConditionOfForestSection.SMALL_FIRE.getRepresentationAsString())))
            throw new SyntaxException(Errors.HAS_NO_FIRE);


        return new GameBoard(numberOfRows, numberOfColumns,
                createGameBoardOutOfArray(makeArrayOutOfInput(numberOfColumns
                        , numberOfRows, inputSplit), numberOfColumns, numberOfRows));
    }

    private static int checkInteger(String intRepresentedAsString) throws SyntaxException {
        int convertedInteger;
        try {
            convertedInteger = Integer.parseInt(intRepresentedAsString);
        } catch (NumberFormatException e) {
            throw new SyntaxException(Errors.FIELD_SIZE_TO_BIG);
        }
        if (convertedInteger < GameBoard.MINIMUM_GAME_BOARD_SIZE) throw new SyntaxException(Errors.BOARD_TO_SMALL);
        return convertedInteger;
    }


    private static String[][] makeArrayOutOfInput(int numberOfColumns, int numberOfRows, String[] input) {
        String[][] output = new String[numberOfRows][numberOfColumns];
        int indexOnGameBoard = INDEX_ON_WHICH_THE_VALUES_OF_GAME_BOARD_START;
        for (int i = 0; i < numberOfRows; i++) {
            for (int k = 0; k < numberOfColumns; k++) {
                output[i][k] = input[indexOnGameBoard];
                indexOnGameBoard++;
            }
        }
        return output;
    }

    private static GameField[][] createGameBoardOutOfArray(String[][] gameFieldAsString
            , int numberOfColumns, int numberOfRows) throws SyntaxException {
        GameField[][] gameBoard = new GameField[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (gameFieldAsString[i][j].matches(REGEX_FOR_FIRE_STATION)) {
                    gameBoard[i][j] = createStationOutOfString(gameFieldAsString[i][j]);
                }
                else if (gameFieldAsString[i][j].equals(Lake.REPRESENTATION_OF_LAKE)) {
                    gameBoard[i][j] = new Lake();
                }
                else if (gameFieldAsString[i][j].matches(REGEX_FOR_FOREST)) {
                    gameBoard[i][j] = createForestOutOfString(gameFieldAsString[i][j]);

                } else {
                    gameBoard[i][j] = new Forest();
                }
            }
        }
        return gameBoard;
    }

    private static FireStation createStationOutOfString(String stringRepresentationOfFireStation) {
        return new FireStation(stringRepresentationOfFireStation);
    }

    private static Forest createForestOutOfString(String inputUser) throws SyntaxException {
        return new Forest(Forest.ConditionOfForestSection.findConditionOfForestSection(inputUser));
    }
}