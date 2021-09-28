package edu.kit.stephan.firecracker.model.firebreaker.player;

import edu.kit.stephan.firecracker.model.firebreaker.FireBrigade;
import edu.kit.stephan.firecracker.model.firebreaker.Position;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;

import java.util.HashSet;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class models a player.
 * @author Johannes Stephan
 * @version 1.0
 */
public class Player {
    /**
     * The constant REPRESENTATION_FOR_PLAYER_A.
     */
    public static final String REPRESENTATION_FOR_PLAYER_A = "A";
    /**
     * The constant REPRESENTATION_FOR_PLAYER_B.
     */
    public static final String REPRESENTATION_FOR_PLAYER_B = "B";
    /**
     * The constant REPRESENTATION_FOR_PLAYER_C.
     */
    public static final String REPRESENTATION_FOR_PLAYER_C = "C";
    /**
     * The constant REPRESENTATION_FOR_PLAYER_D.
     */
    public static final String REPRESENTATION_FOR_PLAYER_D = "D";


    private static final String OUTPUT_SEPARATOR = ",";
    private static final int POINTS_TO_BUY_BRIGADE = 5;
    private static final int STARTING_REPUTATION = 5;
    private final String nameOfPlayer;
    private int reputationPoints;
    private final Set<FireBrigade> fireBrigades;
    private int numberOfFireBrigades;
    private final Position positionOfBase;


    /**
     * Instantiates a new Player.
     *
     * @param nameOfPlayer           the nameOfPlayer
     * @param positionOfBase the position of base
     */
    public Player(String nameOfPlayer, Position positionOfBase)  {
        this.nameOfPlayer = nameOfPlayer;
        this.fireBrigades = new TreeSet<>();
        reputationPoints = STARTING_REPUTATION;
        this.positionOfBase = positionOfBase;
    }

    /**
     * Gets the position of the first fire brigade.
     *
     * @return the position of the first fire brigade.
     */
    public Position getPositionOfFirstFireBrigade() {
        int xCoordinate;
        int yCoordinate;

        if (positionOfBase.getXCoordinate() == 0) {
            xCoordinate = 1;
        } else {
            xCoordinate = positionOfBase.getXCoordinate() - 1;
        }
        if (positionOfBase.getYCoordinate() == 0) {
            yCoordinate = 1;
        } else {
            yCoordinate = positionOfBase.getYCoordinate() - 1;
        }
        return new Position(xCoordinate, yCoordinate);
    }

    /**
     * Creates a fire brigade.
     *
     * @return the fire brigade
     * @throws SemanticsException the semantics exception which gets thrown if the player has not enough points.
     */
    public FireBrigade createFireBrigade() throws SemanticsException {
        if (reputationPoints < POINTS_TO_BUY_BRIGADE) throw new SemanticsException(Errors.NOT_ENOUGH_REPUTATION);
        return new FireBrigade(nameOfPlayer + numberOfFireBrigades);
    }

    /**
     * Adds fire brigade to the playerList.
     *
     * @param fireBrigade the fire brigade
     */
    public void addFireBrigade(FireBrigade fireBrigade) {
        reputationPoints -= POINTS_TO_BUY_BRIGADE;
        numberOfFireBrigades++;
        fireBrigades.add(fireBrigade);
    }

    /**
     * Gets position of the base of base.
     *
     * @return the position of base
     */
    public Position getPositionOfBase() {
        return new Position(positionOfBase.getXCoordinate(), positionOfBase.getYCoordinate());
    }

    /**
     * Gets nameOfPlayer.
     *
     * @return the nameOfPlayer
     */
    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    /**
     * Gets reputation points.
     *
     * @return the reputation points
     */
    public int getReputationPoints() {
        return reputationPoints;
    }

    /**
     * Gets fire brigade with string.
     *
     * @param identifier the identifier
     * @return the fire brigade with string
     * @throws SemanticsException the semantics exception
     */
    public FireBrigade getFireBrigadeWithString(String identifier) throws SemanticsException {
        return fireBrigades.stream().filter(fireBrigade -> fireBrigade.getUniqueIdentifier().equals(identifier))
                .findFirst()
                .orElseThrow(() -> new SemanticsException(Errors.FIRE_BRIGADE_DOES_NOT_EXIST));

    }

    /**
     * Increase reputation.
     */
    public void increaseReputation() {
        reputationPoints++;
    }

    /**
     * Reset fire brigades.
     */
    public void resetFireBrigades() {
        for (FireBrigade fireBrigade : fireBrigades) {
            fireBrigade.resetFireBrigade();
        }
    }

    /**
     * Update fire brigades.
     */
    public void updateFireBrigades() {
        Set<FireBrigade> fireBrigadesToRemove = new HashSet<>();
        for (FireBrigade fireBrigade : fireBrigades) {
            if (fireBrigade.isBurning()) fireBrigadesToRemove.add(fireBrigade);
        }
        for (FireBrigade fireBrigade : fireBrigadesToRemove) {
            fireBrigades.remove(fireBrigade);
        }
    }

    /**
     * Method to determine if a player is alive.
     *
     * @return true: -> if the player is alive,
     *         false: ->if the player is dead.
     */
    public boolean isAlive() {
        return !fireBrigades.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(nameOfPlayer).append(OUTPUT_SEPARATOR).append(reputationPoints).append(System.lineSeparator());

        for (FireBrigade fireBrigade : fireBrigades) {
            output.append(fireBrigade.toString()).append(System.lineSeparator());
        }

        return output.deleteCharAt(output.length() - 1).toString();
    }
}
