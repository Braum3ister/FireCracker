package edu.kit.stephan.firecracker.view.command;


import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SyntaxException;
import edu.kit.stephan.firecracker.core.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represents a Command Parser, which has the purpose to check the Validation of the Syntax and handles
 * the conversion of the inputted String
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class CommandParserFireBreaker implements CommandParser {


    /**
     * String representation of quit Command
     */
    public static final String QUIT = "quit";
    /**
     * String representation of Move Command
     */
    public static final String MOVE = "move";
    /**
     * String representation of Extinguish Command
     */
    public static final String EXTINGUISH = "extinguish";
    /**
     * String representation of Refill Command
     */
    public static final String REFILL = "refill";
    /**
     * String representation of BuyFireEngine Command
     */
    public static final String BUY_FIRE_ENGINE = "buy-fire-engine";
    /**
     * String representation of FireToRoll Command
     */
    public static final String FIRE_TO_ROLL = "fire-to-roll";
    /**
     * String representation of PlayerManagement Command
     */
    public static final String TURN = "turn";
    /**
     * String representation of Reset Command
     */
    public static final String RESET = "reset";
    /**
     * String representation of ShowBoard Command
     */
    public static final String SHOW_BOARD = "show-board";
    /**
     * String representation of ShowField Command
     */
    public static final String SHOW_FIELD = "show-field";
    /**
     * String representation of ShowPlayer Command
     */
    public static final String SHOW_PLAYER = "show-player";


    private static final String SPACE = " ";
    /**
     * The regex of the move command.
     */
    public static final String REGEX_MOVE = MOVE + SPACE +  "[A-D][0-9]+,[0-9]+,[0-9]+";
    /**
     * The regex of the extinguish command.
     */
    public static final String REGEX_EXTINGUISH = EXTINGUISH + SPACE + "[A-D][0-9]+,[0-9]+,[0-9]+";
    /**
     * The regex of the refill command.
     */
    public static final String REGEX_REFILL = REFILL + SPACE + "[A-D][0-9]+";
    /**
     * The regex of the buy-fire-engine command.
     */
    public static final String REGEX_BUY_FIRE_ENGINE = BUY_FIRE_ENGINE + SPACE + "[0-9]+,[0-9]+";
    /**
     * The regex of the fire-to-roll command.
     */
    public static final String REGEX_FIRE_TO_ROLL = FIRE_TO_ROLL + SPACE + "[1-6]";
    /**
     * The regex of the turn command.
     */
    public static final String REGEX_TURN = TURN;
    /**
     * The regex of the reset command.
     */
    public static final String REGEX_RESET = RESET;
    /**
     * The regex of the show-board command.
     */
    public static final String REGEX_SHOW_BOARD = SHOW_BOARD;
    /**
     * The regex of the show-field command.
     */
    public static final String REGEX_SHOW_FIELD = SHOW_FIELD + SPACE + "[0-9]+,[0-9]+";
    /**
     * The regex of the show-player command.
     */
    public static final String REGEX_SHOW_PLAYER = SHOW_PLAYER;
    /**
     * The regex of the quit command.
     */
    public static final String REGEX_QUIT = QUIT;



    @Override
    public Pair<String, List<String>> parseCommand(String inputUser) throws SyntaxException {
        checkBasicRegex(inputUser);
        String commandValue = inputUser.split(SPACE)[0];

        return new Pair<>(checkCommand(commandValue, inputUser), createParameters(inputUser, commandValue));

    }

    private List<String> createParameters(String inputUser, String commandValue) {
        String modifiedInput = inputUser.substring(commandValue.length());
        if (modifiedInput.equals("")) {
            return new LinkedList<>();
        }

        String[] outputAsStringArray = modifiedInput.substring(1).split(",");
        return new LinkedList<>(Arrays.asList(outputAsStringArray));
    }

    private void checkBasicRegex(String inputUser) throws SyntaxException {
        if (inputUser.isEmpty()) throw new SyntaxException(Errors.SYNTAX_ERROR);
        if (inputUser.charAt(0) == ' ') {
            throw new SyntaxException(Errors.SYNTAX_ERROR);
        }
    }


    private String checkCommand(String command, String userInput) throws SyntaxException {
        if (userInput.matches(Command.getCommand(command).getRegexOfTheCommand())) {
            return command;
        }
        throw new SyntaxException(Errors.SYNTAX_ERROR);
    }
}
