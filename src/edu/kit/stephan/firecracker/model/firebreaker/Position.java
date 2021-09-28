package edu.kit.stephan.firecracker.model.firebreaker;

import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a Position
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class Position {
    private final int xCoordinate;
    private final int yCoordinate;


    /**
     * Instantiates a new Position.
     *
     * @param xCoordinate the x coordinate
     * @param yCoordinate the y coordinate
     */
    public Position(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Gets x coordinate.
     *
     * @return the x coordinate
     */
    public int getXCoordinate() {
        return xCoordinate;
    }

    /**
     * Gets y coordinate.
     *
     * @return the y coordinate
     */
    public int getYCoordinate() {
        return yCoordinate;
    }


    private List<Position> getDiagonalNeighbours() {
        List<Position> outputNeighbours = new LinkedList<>();
        outputNeighbours.add(new Position(xCoordinate + 1, yCoordinate + 1));
        outputNeighbours.add(new Position(xCoordinate + 1, yCoordinate - 1));
        outputNeighbours.add(new Position(xCoordinate - 1, yCoordinate + 1));
        outputNeighbours.add(new Position(xCoordinate - 1, yCoordinate - 1));

        return outputNeighbours;
    }

    /**
     * Checks if a position is in a certain parameter.
     * @param rowLength the upperBound of the x-coordinate.
     * @param columnLength the upperBound of the y-coordinate.
     * @throws SemanticsException if the point does not match the criteria.
     */
    public void checkPosition(int rowLength, int columnLength) throws SemanticsException {
        if (xCoordinate >= rowLength || xCoordinate < 0)
            throw new SemanticsException(Errors.POSITION_INVALID);

        if (yCoordinate >= columnLength || yCoordinate < 0)
            throw new SemanticsException(Errors.POSITION_INVALID);
    }

    private List<Position> getStraightNeighboursWithDirection(CardinalDirection cardinalDirection) {
        return cardinalDirection.getNeighbours(this);
    }

    /**
     * This method is used to get the adjacent Neighbours of a Position, which must fulfill the criteria inputted.
     * @param cardinalDirection selects if the North,South,East,West Point should be allowed as Neighbour.
     * @param allowCorners boolean to represent if corners should be allowed.
     * @param rowLength the upperBound of the x-coordinate.
     * @param columnLength the upperBound of the y-coordinate.
     * @return a List of Neighbours
     * @throws SemanticsException if the point on which the method is called is invalid.
     */
    public List<Position> getAllNeighbours(CardinalDirection cardinalDirection, boolean allowCorners
            , int rowLength, int columnLength) throws SemanticsException {
        checkPosition(rowLength, columnLength);
        List<Position> elementsToRemove = new LinkedList<>();
        List<Position> output = new LinkedList<>(getStraightNeighboursWithDirection(cardinalDirection));
        if (allowCorners) {
            output.addAll(getDiagonalNeighbours());
        }
        for (Position neighbour : output) {
            try {
                neighbour.checkPosition(rowLength, columnLength);
            } catch (SemanticsException e) {
                elementsToRemove.add(neighbour);
            }
        }
        for (Position elementToRemove : elementsToRemove) {
            output.remove(elementToRemove);
        }
        return output;
    }


    @Override
    public String toString() {
        return "Position{" + "xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return xCoordinate == position.xCoordinate && yCoordinate == position.yCoordinate;
    }


    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }
}
