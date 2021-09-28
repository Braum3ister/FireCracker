package edu.kit.stephan.firecracker.model.firebreaker;

import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class models a fire Brigade.
 * @author Johannes Stephan
 * @version 1.0
 */
public class FireBrigade implements Comparable<FireBrigade> {
    private static final int TANK_FILLING_START = 3;
    private static final int CAPACITY_OF_TANK = 3;
    private static final int DEFAULT_ACTION_POINTS = 3;
    private static final String TO_STRING = "%s,%s,%s,%s,%s";
    private final String uniqueIdentifier;
    private int tankFilling;
    private Position positionOfFireStation;
    private int actionPoints;
    private boolean performedAction;
    private final List<Position> positionsWhichWereExtinguished;
    private boolean isBurning;


    /**
     * Instantiates a new Fire brigade.
     *
     * @param uniqueIdentifier the unique identifier
     */
    public FireBrigade(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.tankFilling = CAPACITY_OF_TANK;
        this.actionPoints = DEFAULT_ACTION_POINTS;
        performedAction = false;
        isBurning = false;
        positionsWhichWereExtinguished = new LinkedList<>();
    }

    /**
     * Refills a fire brigade.
     *
     * @throws SemanticsException if the tank is already full or there are not enough action points to refill.
     */
    public void refillFireBrigade() throws SemanticsException {
        if (tankFilling == CAPACITY_OF_TANK) {
            throw new SemanticsException(Errors.TANK_IS_ALREADY_FULL);
        }
        performAction();
        tankFilling = TANK_FILLING_START;
        actionPoints--;
        performedAction = true;
    }

    /**
     * Shows if perform Action is allowed
     *
     * @throws SemanticsException if there a not enough Action Points to perform an action.
     */
    public void performAction() throws SemanticsException {
        if (actionPoints == 0) throw new SemanticsException(Errors.NOT_ENOUGH_ACTION_POINTS);
    }


    /**
     * Gets unique identifier.
     *
     * @return the unique identifier
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Sets position of fire station.
     *
     * @param positionOfFireStation the new position of fire station
     */
    public void setPositionOfFireStation(Position positionOfFireStation) {
        this.positionOfFireStation = positionOfFireStation;
    }

    /**
     * Gets action points.
     *
     * @return the action points
     */
    public int getActionPoints() {
        return actionPoints;
    }

    /**
     * Reduce action points.
     */
    public void reduceActionPoints() {
        this.actionPoints--;
    }

    /**
     * Shows if the extinguish Command is valid.
     *
     * @param positionWhichNeedsToBeExtinguish the position which needs to be extinguish
     * @throws SemanticsException if the position was already extinguished or the tank is empty.
     */
    public void extinguishValid(Position positionWhichNeedsToBeExtinguish) throws SemanticsException {
        if (positionsWhichWereExtinguished.contains(positionWhichNeedsToBeExtinguish))
            throw new SemanticsException(Errors.POSITION_WAS_ALREADY_EXTINGUISHED);
        if (tankFilling > 0) return;
        throw new SemanticsException(Errors.TANK_IS_EMPTY);
    }

    /**
     * Updates the fireBrigade after successfully extinguishing a fire.
     *
     * @param positionWhichWasExtinguished the position which was extinguished
     */
    public void extinguishFire(Position positionWhichWasExtinguished) {
        tankFilling--;
        positionsWhichWereExtinguished.add(positionWhichWasExtinguished);
        performedAction = true;
        actionPoints--;
    }

    /**
     * Checks if the move command is allowed.
     *
     * @throws SemanticsException if the move Command is not allowed.
     */
    public void moveAllowed() throws SemanticsException {
        performAction();
        if (performedAction) throw new SemanticsException(Errors.ALREADY_PERFORMED_ACTION);
    }



    private void clearExtinguishList() {
        positionsWhichWereExtinguished.clear();
    }

    /**
     * Returns a boolean if the fire Brigade is burning
     *
     * @return the boolean if its burning
     */
    public boolean isBurning() {
        return isBurning;
    }

    /**
     * Sets a fire Brigade to burning.
     */
    public void setBurning() {
        isBurning = true;
    }



    /**
     * Gets position of fire brigade.
     *
     * @return the position of fire brigade
     */
    public Position getPositionOfFireBrigade() {
        return new Position(positionOfFireStation.getXCoordinate(), positionOfFireStation.getYCoordinate());
    }

    /**
     * Resets fire brigade.
     */
    public void resetFireBrigade() {
        clearExtinguishList();
        performedAction = false;
        actionPoints = DEFAULT_ACTION_POINTS;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING, uniqueIdentifier, tankFilling, actionPoints
                , positionOfFireStation.getXCoordinate(), positionOfFireStation.getYCoordinate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FireBrigade that = (FireBrigade) o;
        return Objects.equals(uniqueIdentifier, that.uniqueIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier);
    }

    @Override
    public int compareTo(FireBrigade o) {
        return uniqueIdentifier.compareTo(o.getUniqueIdentifier());
    }
}
