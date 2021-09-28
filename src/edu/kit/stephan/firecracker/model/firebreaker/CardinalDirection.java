package edu.kit.stephan.firecracker.model.firebreaker;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This enum models a Cardinal Direction.
 * @author Johannes Stephan
 * @version 1.0
 */
public enum CardinalDirection {


    /**
     * All directions
     */
    ALL_DIRECTIONS(1) {

        @Override
        public List<Position> getNeighbours(Position position) {
            List<Position> positions = new LinkedList<>();
            positions.add(new Position(position.getXCoordinate() - 1, position.getYCoordinate()));
            positions.add(new Position(position.getXCoordinate(), position.getYCoordinate() + 1));
            positions.add(new Position(position.getXCoordinate() + 1, position.getYCoordinate()));
            positions.add(new Position(position.getXCoordinate(), position.getYCoordinate() - 1));

            return positions;
        }
    },
    /**
     * North cardinal direction.
     */
    NORTH(2) {
        @Override
        public List<Position> getNeighbours(Position position) {
            List<Position> positions = new LinkedList<>();
            positions.add(new Position(position.getXCoordinate() - 1, position.getYCoordinate()));
            return positions;
        }
    },

    /**
     * East cardinal direction.
     */
    EAST(3) {
        @Override
        public List<Position> getNeighbours(Position position) {
            List<Position> positions = new LinkedList<>();
            positions.add(new Position(position.getXCoordinate(), position.getYCoordinate() + 1));
            return positions;
        }
    },

    /**
     * South cardinal direction.
     */
    SOUTH(4) {
        @Override
        public List<Position> getNeighbours(Position position) {
            List<Position> positions = new LinkedList<>();
            positions.add(new Position(position.getXCoordinate() + 1, position.getYCoordinate()));
            return positions;
        }
    },

    /**
     * West cardinal direction.
     */
    WEST(5) {
        @Override
        public List<Position> getNeighbours(Position position) {
            List<Position> positions = new LinkedList<>();
            positions.add(new Position(position.getXCoordinate(), position.getYCoordinate() - 1));
            return positions;
        }
    },

    /**
     * None
     */
    NONE(6) {
        @Override
        public List<Position> getNeighbours(Position position) {
            return new LinkedList<>();
        }
    };

    private final int directionNumber;

    /**
     * Constructor of a Cardinal Direction
     * @param directionNumber a number which represents the cardinal direction
     */
    CardinalDirection(int directionNumber) {
        this.directionNumber = directionNumber;
    }

    /**
     * Gets direction number.
     *
     * @return the direction number
     */
    public int getDirectionNumber() {
        return directionNumber;
    }

    /**
     * Gets the Neighbours of a position which is associated to the cardinal direction.
     * @param position the position to get the neighbours.
     * @return a List of Positions which are valid neighbours.
     */
    public abstract List<Position> getNeighbours(Position position);

    /**
     * Find direction through integer cardinal direction.
     *
     * @param directionNumber the direction number
     * @return the cardinal direction
     */
    public static CardinalDirection findDirectionThroughInteger(int directionNumber) {
        return Arrays.stream(CardinalDirection.values())
                .filter(cardinalDirection -> cardinalDirection.getDirectionNumber() == directionNumber)
                .findFirst().orElse(null);
    }
}
