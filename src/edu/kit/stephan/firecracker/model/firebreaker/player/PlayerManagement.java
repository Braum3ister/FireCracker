package edu.kit.stephan.firecracker.model.firebreaker.player;

import edu.kit.stephan.firecracker.model.firebreaker.FireBrigade;
import edu.kit.stephan.firecracker.model.firebreaker.Position;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;
import edu.kit.stephan.firecracker.core.Pair;
import edu.kit.stephan.firecracker.core.Triple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class models a turn in the Game.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class PlayerManagement {

    /**
     * Stores the Information of how many Players are Playing on Game Start.
     */
    public static final int AMOUNT_OF_PLAYERS = 4;

    private static final boolean PRINT_ONLY_PLAYER_WHEN_CURRENT_PLAYER_DIES = false;
    private static final String VALID_TURN = "OK";
    private Player currentPlayer;
    private final Map<Player, Player> playerMap;
    private int counterPerRound;
    private Player startPlayer;
    private int counterForInitialization;
    private Player playerForInitialization;
    private final Set<Player> playerWhoAreDead;


    /**
     * Instantiates a new Player management.
     *
     * @param rowLength    the row length
     * @param columnLength the column length
     */
    public PlayerManagement(int rowLength, int columnLength) {
        this.playerMap = createPlayers(rowLength, columnLength);
        this.playerWhoAreDead = new HashSet<>();
        this.counterPerRound = 1;
        this.currentPlayer = startPlayer;
        this.playerForInitialization = startPlayer;
    }

    /**
     * Performs a turn.
     *
     * @return a pair: firstElement: Boolean -> if the fire-to-roll-command needs to be performed
     *                 secondElement: String: -> currentPlayer String representation.
     * @throws SemanticsException an error if turn Command was invalid.
     */
    public Pair<Boolean, String> turnCommand() throws SemanticsException {
        if (counterPerRound == amountOfPlayersAlive()) {
            currentPlayer.resetFireBrigades();
            startPlayer = updateStartPlayer();
            currentPlayer = startPlayer;
            counterPerRound = 1;
            return new Pair<>(true, currentPlayer.getNameOfPlayer());
        }
        counterPerRound++;
        currentPlayer.resetFireBrigades();
        currentPlayer = getNextNotDeadPlayer(currentPlayer);
        return new Pair<>(false, currentPlayer.getNameOfPlayer());
    }

    /**
     * Executes the required turn, when fire-to-roll was executed.
     *
     * @return the string representation of a Player -> if the currentPlayer is dead.
     *         "OK" -> if the currentPlayer is still alive.
     * @throws SemanticsException if it would be executed after no one is alive.
     */
    public String fireToRollTurn() throws SemanticsException {
        boolean someoneDied = updatePlayers();
        if (!currentPlayer.isAlive()) {
            currentPlayer = getNextNotDeadPlayer(currentPlayer);
            return currentPlayer.getNameOfPlayer();
        }
        if (!PRINT_ONLY_PLAYER_WHEN_CURRENT_PLAYER_DIES && someoneDied) {
            return currentPlayer.getNameOfPlayer();
        } else {
            return VALID_TURN;
        }
    }

    /**
     * Creates a fire brigade of the current player.
     *
     * @return the created fire brigade.
     * @throws SemanticsException if an error occurred during the creation.
     */
    public FireBrigade createFireBrigade() throws SemanticsException {
        return currentPlayer.createFireBrigade();
    }

    /**
     * Gets the current position of the current player.
     *
     * @return the position of the current player.
     */
    public Position getPositionOfCurrentPlayerBase() {
        return currentPlayer.getPositionOfBase();
    }

    /**
     * Adds a FireBrigade to the current player.
     *
     * @param fireBrigadeToAdd the fire brigade which should be added.
     */
    public void addFireBrigadeToCurrentPlayer(FireBrigade fireBrigadeToAdd) {
        currentPlayer.addFireBrigade(fireBrigadeToAdd);
    }

    /**
     * Gets the reputation point of the current player.
     *
     * @return the reputation points.
     */
    public int getReputationPointsOfCurrentPlayer() {
        return currentPlayer.getReputationPoints();
    }

    /**
     * Increases the reputation of the current player.
     */
    public void increaseReputationOfCurrentPlayer() {
        currentPlayer.increaseReputation();
    }

    /**
     * Gets the string representation of the current player.
     *
     * @return the string representation of the current player.
     */
    public String currentPlayerToString() {
        return currentPlayer.toString();
    }

    /**
     * Method to search the fire brigades of the current player by a inputted string.
     *
     * @param identifierOfPotentialFireBrigade the string representation of a potential identifier.
     * @return the fire brigade which was found.
     * @throws SemanticsException if no fireBrigade corresponds to the inputted string.
     */
    public FireBrigade searchFireBrigade(String identifierOfPotentialFireBrigade) throws SemanticsException {
        return currentPlayer.getFireBrigadeWithString(identifierOfPotentialFireBrigade);
    }

    /**
     * Method to sum up the essential values to place the firstFireBrigade.
     *
     * @return a triple: first element -> the first fire brigade.
     *                   second element -> the position of the fire brigade.
     *                   third element -> the position of the players base.
     * @throws SemanticsException if its not possible to place a fire brigade -> should not happen at initialization.
     */
    public Triple<FireBrigade, Position, Position> getEssentialValuesToPlaceFirstFireBrigade()
            throws SemanticsException {
        if (counterForInitialization < AMOUNT_OF_PLAYERS) {
            Triple<FireBrigade, Position, Position> outputForEssentialValues = new Triple<>(playerForInitialization
                    .createFireBrigade(), playerForInitialization.getPositionOfFirstFireBrigade()
                    , playerForInitialization.getPositionOfBase());
            playerForInitialization.addFireBrigade(outputForEssentialValues.getFirstElement());
            playerForInitialization = playerMap.get(playerForInitialization);
            counterForInitialization++;
            return outputForEssentialValues;
        }
        throw new IllegalAccessError();
    }

    private boolean updatePlayers() {
        boolean someOneDied = false;
        for (Player player : playerMap.keySet()) {
            player.updateFireBrigades();
            if (!player.isAlive() && !playerWhoAreDead.contains(player)) {
                someOneDied = true;
                playerWhoAreDead.add(player);
            }
        }
        return someOneDied;
    }

    private Player getNextNotDeadPlayer(Player currentPlayer) throws SemanticsException {
        Player player = currentPlayer;
        for (int i = 0; i < playerMap.keySet().size(); i++) {
            player = playerMap.get(player);
            if (player.isAlive()) {
                return player;
            }
        }
        throw new SemanticsException(Errors.NOT_IMPLEMENTED);
    }

    private int amountOfPlayersAlive() {
        return (int) playerMap.keySet().stream().filter(Player::isAlive).count();
    }

    private Player updateStartPlayer() throws SemanticsException {
        return getNextNotDeadPlayer(startPlayer);
    }


    private Map<Player, Player> createPlayers(int rowLength, int columnLength) {
        Map<Player, Player> cratedPlayers = new HashMap<>();
        Player aPlayer = new Player(Player.REPRESENTATION_FOR_PLAYER_A, new Position(0, 0));
        Player bPlayer = new Player(Player.REPRESENTATION_FOR_PLAYER_B
                , new Position(rowLength - 1, columnLength - 1));
        Player cPlayer = new Player(Player.REPRESENTATION_FOR_PLAYER_C
                , new Position(rowLength - 1, 0));
        Player dPlayer = new Player(Player.REPRESENTATION_FOR_PLAYER_D
                , new Position(0, columnLength - 1));
        cratedPlayers.put(aPlayer, bPlayer);
        cratedPlayers.put(bPlayer, cPlayer);
        cratedPlayers.put(cPlayer, dPlayer);
        cratedPlayers.put(dPlayer, aPlayer);
        startPlayer = aPlayer;
        return cratedPlayers;
    }
}
