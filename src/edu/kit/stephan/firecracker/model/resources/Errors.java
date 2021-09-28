package edu.kit.stephan.firecracker.model.resources;

/**
 * Error-Message Storage
 * @author Johannes Stephan
 * @version 1.0
 */
public final class Errors {
    /*
     ***************************************************************
     ***************************Syntax******************************
     ***************************************************************
     */

    /**
     * Error-Message which gets thrown when the board is too small
     */
    public static final String BOARD_TO_SMALL = "The entered Board is too small";
    /**
     * Error-Message which gets thrown when the inputted Board violates the Board - Syntax
     */
    public static final String SYNTAX_ERROR = "Syntax of the Command is not valid";
    /**
     * Error-Message which gets thrown when the command Arguments are wrong
     */
    public static final String PARAMETERS_ARE_WRONG = "Please fix your Parameters";
    /**
     * Error-Message which gets thrown when the board does not match the more specific Regex
     */
    public static final String REGEX_OF_GAME_BOARD_IS_WRONG = "The Regex does not match";
    /**
     * Error-Message which gets thrown when the board is too big
     */
    public static final String FIELD_SIZE_TO_BIG = "The entered field is too big";
    /**
     * Error-Message which gets thrown when the board has not the appropriate starting fires
     */
    public static final String HAS_NO_FIRE = "There must be at least one small and one big fire";


    /*
    ***************************************************************
    **************************APPLICATION**************************
    ***************************************************************
     */

    /**
     * Error-Message which gets thrown when the game is already over and only a few commands are allowed
     */
    public static final String GAME_IS_OVER = "The game is over only show board is allowed";
    /**
     * Error-Message which gets thrown when its to early to execute the roll fire command
     */
    public static final String YOU_CANT_ROLL = "Its to early to be crushed by fire";
    /**
     * Error-Message which gets thrown when you have to do the roll fire command and you violate against it
     */
    public static final String YOU_NEED_TO_ROLL = "The first player needs to roll before continuing";
    /**
     * Error-Message which gets thrown when the position was already extinguished
     */
    public static final String POSITION_WAS_ALREADY_EXTINGUISHED = "The position was already extinguished";
    /**
     * Error-Message which gets thrown when the action was already performed
     */
    public static final String ALREADY_PERFORMED_ACTION = "This station has already performed Action";
    /**
     * Error-Message which gets thrown when someone is trying to extinguish fire stations
     */
    public static final String YOU_CANT_EXTINGUISH_FIRE_STATIONS = "You cant extinguish fire stations";
    /**
     * Error-Message which gets thrown when someone is trying to extinguish lakes
     */
    public static final String YOU_CANT_EXTINGUISH_LAKES = "Lakes are not possible to extinguish";
    /**
     * Error-Message which gets thrown when the range is too short
     */
    public static final String RANGE_TO_EXTINGUISH = "Your range is too short";
    /**
     * Error-Message which gets thrown when the tank is empty and you try to extinguish something
     */
    public static final String TANK_IS_EMPTY = "The tank is Empty you need to refill";
    /**
     * Error-Message which gets thrown when you try to extinguish a wet field
     */
    public static final String FOREST_IS_ALREADY_WET = "The forest is already wet";
    /**
     * Error-Message which gets thrown when there are no refill stations
     */
    public static final String NO_REFILL_STATION = "There is no refill station next to you";
    /**
     * Error-Message which gets thrown when there are not enough action points to perform this action
     */
    public static final String NOT_ENOUGH_ACTION_POINTS = "Not enough action points to perform this task";
    /**
     * Error-Message which gets thrown when the tank is already full
     */
    public static final String TANK_IS_ALREADY_FULL = "Tank is already full";
    /**
     * Error-Message which gets thrown when the fireBrigade does not exist
     */
    public static final String FIRE_BRIGADE_DOES_NOT_EXIST = "The Fire Brigade does not exist";
    /**
     * Error-Message which gets thrown when the Destination to move is not valid
     */
    public static final String END_POINT_IS_NOT_VALID = "Destination is not valid";
    /**
     * Error-Message which gets thrown when you try to move to the same field
     */
    public static final String POINTS_CANNOT_BE_EQUAL = "Points cannot be equal";
    /**
     * Error-Message which gets thrown when the points are not reachable
     */
    public static final String POINTS_ARE_NOT_REACHABLE = "Points are not reachable";
    /**
     * Error-Message which gets thrown when you try to place a fire brigade on burning ground
     */
    public static final String CANT_PLACE_FIRE_BRIGADE = "The ground is burning";

    /**
     * Error-Message which gets thrown when whe Points are the same
     */
    public static final String POINTS_ARE_THE_SAME = "Both Points are the same";

    /**
     * Error-Message which gets thrown when there are not enough reputation points to perform this action
     */
    public static final String NOT_ENOUGH_REPUTATION
            = "Your reputation points are not high enough to perform this action";

    /**
     * Error-Message which gets thrown when the command should have been implemented but is not
     */
    public static final String COMMAND_NOT_IMPLEMENTED = "Command is not implemented";

    /**
     * Error-Message which gets thrown when the position which was entered is invalid
     */
    public static final String POSITION_INVALID = "The Position which was entered is invalid";
    /**
     * Error-Message if a command did not succeed but no message was supplied
     */
    public static final String COMMAND_ENDED_ERROR = "Command did not succeed.";
    /**
     * Error-Message if something was not implemented, but should
     */
    public static final String NOT_IMPLEMENTED = "Should have been implemented";

    private Errors() {
        throw new IllegalStateException("Utility-class constructor.");
    }
}
