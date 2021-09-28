package edu.kit.stephan.firecracker.model.firebreaker;

import edu.kit.stephan.firecracker.core.Output;
import edu.kit.stephan.firecracker.model.firebreaker.board.GameBoard;
import edu.kit.stephan.firecracker.model.firebreaker.player.PlayerManagement;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;
import edu.kit.stephan.firecracker.core.Pair;
import edu.kit.stephan.firecracker.core.Triple;


/**
 * This class models a game handler of the fire breaker game.
 * It is used to execute and handle every command of the game.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class GameHandlerFireBreaker {
    /**
     * Message which gets returned if a command was valid
     */
    public static final String VALID_COMMAND = "OK";
    private static final String VALID_EXTINGUISH = "%s,%d";
    private GameBoard gameBoard;
    private boolean endOfRound;
    private boolean gameIsOver;
    private PlayerManagement playerManagement;


    /**
     * Instantiates a new Fire breaker Game.
     *
     * @param gameBoard the parsed game board
     */
    public GameHandlerFireBreaker(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.playerManagement = new PlayerManagement(gameBoard.getRowLength(), gameBoard.getColumnLength());
        this.endOfRound = false;
        gameIsOver = false;
        placeInitialFireBrigades();
    }

    public void initialize(Output output) {
        output.output("Handelsphase beginn");
    }

    /**
     * Resets the Game.
     *
     * @return "OK" if the command succeeded.
     */
    public String resetGameCommand() {
        gameBoard = gameBoard.getGameBoardCopied();
        this.playerManagement = new PlayerManagement(gameBoard.getRowLength(), gameBoard.getColumnLength());
        endOfRound = false;
        gameIsOver = false;
        placeInitialFireBrigades();
        return VALID_COMMAND;
    }


    /**
     * Performs the player management command and checks if the command is allowed.
     *
     * @return The string representation of the new current player.
     * @throws SemanticsException if the move command is not allowed.
     */
    public String turnCommand() throws SemanticsException {
        checkIfGameIsOver();
        checkIfItWasRolled();
        Pair<Boolean, String> resultOfTurn = playerManagement.turnCommand();
        if (resultOfTurn.getFirstElement().equals(true)) endOfRound = true;
        return resultOfTurn.getSecondElement();
    }

    /**
     * Performs the fire-to-roll Command and executes the necessary steps to execute the command.
     *
     * @param cardinalDirection the direction in which the fire is spreading
     * @return lose -> if the fire-to-roll Command was decisive.
     *         OK -> if fire-to-roll did not kill the currentPlayer.
     *         String Representation of Player -> if the fire-to-roll killed the currentPlayer.
     * @throws SemanticsException if the fire-to-roll is not allowed to be performed.
     */
    public String fireToRollCommand(CardinalDirection cardinalDirection) throws SemanticsException {
        checkIfGameIsOver();

        if (!endOfRound) throw new SemanticsException(Errors.YOU_CANT_ROLL);
        endOfRound = false;
        boolean decisive = gameBoard.executeRollFire(cardinalDirection);
        if (decisive) {
            gameIsOver = true;
            return GameBoard.PLAYER_HAVE_LOST;
        }
        return playerManagement.fireToRollTurn();
    }


    /**
     * Performs the buy-fire-engine Command and executes the necessary steps to execute the command.
     *
     * @param positionOfNewFireEngine the position of the new fire engine
     * @return the remaining Reputation Points of the currentPlayer.
     * @throws SemanticsException if the command is not allowed to be performed or the player can't buy a fire-truck due
     *                            to game rules
     */
    public String buyFireEngineCommand(Position positionOfNewFireEngine) throws SemanticsException {
        checkIfGameIsOver();
        checkIfItWasRolled();
        FireBrigade fireBrigade = playerManagement.createFireBrigade();
        gameBoard.placeFireBrigade(fireBrigade, positionOfNewFireEngine
                , playerManagement.getPositionOfCurrentPlayerBase());
        playerManagement.addFireBrigadeToCurrentPlayer(fireBrigade);
        return String.valueOf(playerManagement.getReputationPointsOfCurrentPlayer());
    }

    /**
     * Performs the refill Command and executes the necessary steps to execute the command.
     *
     * @param uniqueIdentifierOfFireBrigade the unique identifier of fire brigade which needs to be refilled
     * @return The remaining action points of the fire Brigade
     * @throws SemanticsException if the command could not be performed in the correct manner.
     */
    public String refillFireBrigadeCommand(String uniqueIdentifierOfFireBrigade) throws SemanticsException {
        checkIfGameIsOver();
        checkIfItWasRolled();
        FireBrigade fireBrigade = searchBrigadeByString(uniqueIdentifierOfFireBrigade);
        fireBrigade.performAction();
        gameBoard.hasLakeOrStationNextToIt(fireBrigade);
        fireBrigade.refillFireBrigade();
        return String.valueOf(fireBrigade.getActionPoints());
    }

    /**
     * Performs the extinguish Command and executes the necessary steps to execute the command.
     *
     * @param uniqueIdentifierOfFireBrigade the unique identifier of fire brigade which wants to extinguish something
     * @param positionToExtinguish          the position to extinguish
     * @return "won" -> if the game was decisive
     *         "the Condition of the field which was extinguished,the remaining action points
     *          -> if the command was performed successfully
     * @throws SemanticsException if the command could not be performed in the correct manner.
     */
    public String extinguishCommand(String uniqueIdentifierOfFireBrigade, Position positionToExtinguish)
            throws SemanticsException {
        checkIfGameIsOver();
        checkIfItWasRolled();
        FireBrigade fireBrigade = searchBrigadeByString(uniqueIdentifierOfFireBrigade);
        fireBrigade.performAction();

        fireBrigade.extinguishValid(positionToExtinguish);
        Triple<Boolean, Boolean, String> result = gameBoard.extinguishMethod(fireBrigade, positionToExtinguish);


        if (result.getFirstElement().equals(true)) {
            gameIsOver = true;
            return GameBoard.PLAYER_HAVE_WON;
        }
        if (result.getSecondElement().equals(true)) {
            playerManagement.increaseReputationOfCurrentPlayer();
        }

        fireBrigade.extinguishFire(positionToExtinguish);
        return String.format(VALID_EXTINGUISH, result.getThirdElement(), fireBrigade.getActionPoints());
    }

    /**
     * Performs the show-board Command and executes the necessary steps to execute the command.
     *
     * @return the Board represented as String.
     */
    public String showBoardCommand() {
        return gameBoard.toString();
    }


    /**
     * Performs the move Command and executes the necessary steps to execute the command.
     *
     * @param identifierOfFireBrigade the identifier of the fire brigade.
     * @param endPosition             the destination to move to.
     * @return "OK" -> if the command was performed successfully.
     * @throws SemanticsException if the command could not be performed in the correct manner.
     */
    public String moveCommand(String identifierOfFireBrigade, Position endPosition) throws SemanticsException {
        checkIfGameIsOver();
        checkIfItWasRolled();
        FireBrigade fireBrigade = searchBrigadeByString(identifierOfFireBrigade);
        fireBrigade.moveAllowed();
        gameBoard.executeMoveMethod(fireBrigade, endPosition);
        fireBrigade.reduceActionPoints();
        return VALID_COMMAND;
    }

    /**
     * Performs the show Field Command and executes the necessary steps to execute the command.
     *
     * @param positionOfField the position of the field which should be outputted.
     * @return the string representation of the field -> if the command was performed successfully
     * @throws SemanticsException if the command could not be performed in the correct manner.
     */
    public String showFieldCommand(Position positionOfField) throws SemanticsException {
        return gameBoard.showField(positionOfField);
    }

    /**
     * Performs the show Player Command and executes the necessary steps to execute the command.
     *
     * @return the string Representation of a Player -> if the command was performed successfully.
     * @throws SemanticsException if the command could not be performed in the correct manner.
     */
    public String showPlayerCommand() throws SemanticsException {
        checkIfGameIsOver();
        return playerManagement.currentPlayerToString();
    }

    private FireBrigade searchBrigadeByString(String identifier) throws SemanticsException {
        return playerManagement.searchFireBrigade(identifier);
    }

    private void checkIfItWasRolled() throws SemanticsException {
        if (endOfRound) throw new SemanticsException(Errors.YOU_NEED_TO_ROLL);
    }

    private void checkIfGameIsOver() throws SemanticsException {
        if (gameIsOver) throw new SemanticsException(Errors.GAME_IS_OVER);
    }

    private void placeInitialFireBrigades() {
        for (int i = 0; i < PlayerManagement.AMOUNT_OF_PLAYERS; i++) {
            Triple<FireBrigade, Position, Position> essentialInfoOfPlayer;
            try {
                essentialInfoOfPlayer = playerManagement.getEssentialValuesToPlaceFirstFireBrigade();
                gameBoard.placeFireBrigade(essentialInfoOfPlayer.getFirstElement()
                        , essentialInfoOfPlayer.getSecondElement(), essentialInfoOfPlayer.getThirdElement());
            } catch (SemanticsException ignored) {
                //Catching redundant because the placing of the firstFireBrigade is always allowed.
            }
        }
    }

}
