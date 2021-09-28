package edu.kit.stephan.firecracker.model.firebreaker.board;


import edu.kit.stephan.firecracker.model.firebreaker.CardinalDirection;
import edu.kit.stephan.firecracker.model.firebreaker.FireBrigade;
import edu.kit.stephan.firecracker.model.firebreaker.Position;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;
import edu.kit.stephan.firecracker.core.Pair;
import edu.kit.stephan.firecracker.core.Triple;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * The class models a gameBoard.
 * @author Johannes Stephan
 * @version 1.0
 */
public class GameBoard {
    /**
     * The minimum game board size.
     */
    public static final int MINIMUM_GAME_BOARD_SIZE = 5;

    /**
     * String representation of a win position
     */
    public static final String PLAYER_HAVE_WON = "win";
    /**
     * String representation of a lose position
     */
    public static final String PLAYER_HAVE_LOST = "lose";
    private static final String FIELD_SEPARATOR_TO_STRING = ",";
    private static final String NOT_BURNING = "x";
    private static final int DISTANCE_OF_MOVE_ALLOWED = 2;
    private static final boolean ALLOW_CORNERS_TRUE = true;
    private static final boolean ALLOW_CORNERS_FALSE = false;
    private static final boolean ONLY_FOREST_TRUE = true;
    private static final boolean ONLY_FOREST_FALSE = false;


    private final GameField[][] gameBoardOfFireBreaker;
    private final GameField[][] gameBoardCopied;

    private final int rowLength;
    private final int columnLength;

    /**
     * Instantiates a new Game board.
     *
     * @param rowLength    the row length
     * @param columnLength the column length
     * @param createdBoard the created board
     */
    public GameBoard(int rowLength, int columnLength, GameField[][] createdBoard) {

        this.rowLength = rowLength;
        this.columnLength = columnLength;
        gameBoardOfFireBreaker = createdBoard;
        gameBoardCopied = copyGameBoard(gameBoardOfFireBreaker);
    }

    /**
     * Show the field representation of a specific point.
     *
     * @param positionToShowField the point which correlates to the field.
     * @return the representation.
     * @throws SemanticsException if the position is invalid
     */
    public String showField(Position positionToShowField) throws SemanticsException {
        positionToShowField.checkPosition(rowLength, columnLength);
        return gameBoardOfFireBreaker[positionToShowField.getXCoordinate()][positionToShowField.getYCoordinate()]
                .toString();
    }


    /**
     * Method is used to determine if there is an refill Point next to the inputted fire Brigade
     *
     * @param fireBrigade the fire brigade which wants to refill
     * @throws SemanticsException an error if its not possible to refill.
     */
    public void hasLakeOrStationNextToIt(FireBrigade fireBrigade)
            throws SemanticsException {
        Position p1 = fireBrigade.getPositionOfFireBrigade();
        List<Position> neighbours = getNeighboursOfPosition(p1, ALLOW_CORNERS_TRUE, ONLY_FOREST_FALSE
                , CardinalDirection.ALL_DIRECTIONS);
        for (Position neighbour : neighbours) {
            GameField gameField = gameBoardOfFireBreaker[neighbour.getXCoordinate()][neighbour.getYCoordinate()];
            if (gameField.getClass() == Lake.class || gameField.getClass() == FireStation.class) {
                return;
            }
        }
        throw new SemanticsException(Errors.NO_REFILL_STATION);

    }

    /**
     * Method to actually extinguish a fire on the GameBoard.
     *
     * @param fireBrigade          the fire brigade which extinguishes the fire
     * @param positionToExtinguish the position to extinguish
     * @return firstElement represents if the game is won,
     * the second if the fireBrigade earned a Point, the third the new condition of the Field which got extinguished.
     * @throws SemanticsException if the extinguish process is invalid.
     */
    public Triple<Boolean, Boolean, String> extinguishMethod(FireBrigade fireBrigade, Position positionToExtinguish)
            throws SemanticsException {

        List<Position> neighbours = getNeighboursOfPosition(fireBrigade.getPositionOfFireBrigade()
                , ALLOW_CORNERS_FALSE, ONLY_FOREST_TRUE, CardinalDirection.ALL_DIRECTIONS);

        if (!neighbours.contains(positionToExtinguish)) throw new SemanticsException(Errors.RANGE_TO_EXTINGUISH);
        GameField gameField = gameBoardOfFireBreaker[positionToExtinguish.getXCoordinate()]
                [positionToExtinguish.getYCoordinate()];
        if (gameField.getClass() == Lake.class) throw new SemanticsException(Errors.YOU_CANT_EXTINGUISH_LAKES);
        if (gameField.getClass() == FireStation.class)
            throw new SemanticsException(Errors.YOU_CANT_EXTINGUISH_FIRE_STATIONS);

        Forest forest = (Forest) gameField;
        Pair<Boolean, String> resultOfExtinguish = forest.extinguishFire();
        return new Triple<>(playerHaveWon(), resultOfExtinguish);
    }

    /**
     * Place a fire brigade on the gameBoard which was bought or was the initial starting fire Brigade.
     *
     * @param fireBrigade           the fire brigade which needs to be placed
     * @param positionOfFireBrigade the position on which the fireBrigade should be placed
     * @param positionOfBase        the position of base
     * @throws SemanticsException if its not possible,
     * to place the fire brigade because of restriction due to game rules.
     */
    public void placeFireBrigade(FireBrigade fireBrigade, Position positionOfFireBrigade, Position positionOfBase)
            throws SemanticsException {
        positionOfFireBrigade.checkPosition(rowLength, columnLength);
        checkIfPositionsAreReachable(positionOfFireBrigade, positionOfBase);

        if (gameBoardOfFireBreaker[positionOfFireBrigade.getXCoordinate()][positionOfFireBrigade.getYCoordinate()]
                .getClass() == Forest.class) {
            Forest forest = (Forest) gameBoardOfFireBreaker
                    [positionOfFireBrigade.getXCoordinate()][positionOfFireBrigade.getYCoordinate()];
            forest.addFireBrigade(fireBrigade);
            fireBrigade.setPositionOfFireStation(positionOfFireBrigade);
        }
    }

    /**
     * Executes the move Command.
     *
     * @param fireBrigade the fire brigade which is moving
     * @param end         the destination point
     * @throws SemanticsException the semantics exception if the move was invalid
     */
    public void executeMoveMethod(FireBrigade fireBrigade, Position end) throws SemanticsException {
        end.checkPosition(rowLength, columnLength);
        Position start = fireBrigade.getPositionOfFireBrigade();

        if (start.equals(end))
            throw new SemanticsException(Errors.POINTS_CANNOT_BE_EQUAL);

        if (gameBoardOfFireBreaker[end.getXCoordinate()]
                [end.getYCoordinate()].getClass() != Forest.class) {
            throw new SemanticsException(Errors.END_POINT_IS_NOT_VALID);
        }

        Forest forest = (Forest) gameBoardOfFireBreaker[end.getXCoordinate()][end.getYCoordinate()];
        if (forest.isBurning()) throw new SemanticsException(Errors.END_POINT_IS_NOT_VALID);
        checkIfMoveIsAllowed(start, end);
        /*
        Adding new FireStation
         */
        fireBrigade.setPositionOfFireStation(end);
        forest.addFireBrigade(fireBrigade);
        /*
        Removing oldFireStation
         */
        Forest oldForest = (Forest) gameBoardOfFireBreaker[start.getXCoordinate()][start.getYCoordinate()];
        oldForest.deleteFireBrigade(fireBrigade);

    }

    /**
     * Executes the Roll Fire Command
     *
     * @param cardinalDirection the cardinal direction in which the fire is spreading
     * @return a boolean, which describes if the game is lost.
     * @throws SemanticsException the semantics exception if an error occurred.
     */
    public boolean executeRollFire(CardinalDirection cardinalDirection) throws SemanticsException {
        if (cardinalDirection.equals(CardinalDirection.NONE))
            return false;

        Set<Position> positionsWhichNeedsToBeIncreased = getPositionsWhichNeedToBeIncreased(cardinalDirection);
        /*
        Increase the Fields
         */
        for (Position positionWhichNeedToBeIncrease : positionsWhichNeedsToBeIncreased) {
            GameField gameField = gameBoardOfFireBreaker[positionWhichNeedToBeIncrease.getXCoordinate()]
                    [positionWhichNeedToBeIncrease.getYCoordinate()];
            Forest forest = (Forest) gameField;
            forest.increaseBurning();
        }

        /*
        Check If Losing
         */
        return checkIfLost();
    }


    /**
     * Gets a new starting gameBoard
     *
     * @return the initial GameBoard
     */
    public GameBoard getGameBoardCopied() {
        return new GameBoard(rowLength, columnLength, copyGameBoard(gameBoardCopied));
    }

    /**
     * Gets the column length.
     *
     * @return the column length
     */
    public int getColumnLength() {
        return columnLength;
    }

    /**
     * Gets the row length.
     *
     * @return the row length
     */
    public int getRowLength() {
        return rowLength;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (gameBoardOfFireBreaker[i][j].getClass() == Forest.class) {
                    Forest forest = (Forest) gameBoardOfFireBreaker[i][j];
                    output.append(forest.getBurning()).append(FIELD_SEPARATOR_TO_STRING);
                    continue;
                }
                output.append(NOT_BURNING).append(FIELD_SEPARATOR_TO_STRING);
            }
            output.deleteCharAt(output.length() - 1);
            output.append(System.lineSeparator());
        }
        return output.deleteCharAt(output.length() - 1).toString();
    }

    private GameField[][] copyGameBoard(GameField[][] gameBoardToCopy) {
        GameField[][] copiedBoard = new GameField[rowLength][columnLength];
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                GameField gameField = gameBoardToCopy[i][j];
                if (gameField.getClass() == Forest.class) {
                    Forest forest = (Forest) gameField;
                    copiedBoard[i][j] = new Forest(forest.getCondition());
                }
                if (gameField.getClass() == Lake.class) {
                    copiedBoard[i][j] = new Lake();
                }
                if (gameField.getClass() == FireStation.class) {
                    FireStation fireStation = (FireStation) gameField;
                    copiedBoard[i][j] = new FireStation(fireStation.toString());
                }
            }
        }
        return copiedBoard;
    }

    private boolean playerHaveWon() {

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                GameField gameField = gameBoardOfFireBreaker[i][j];
                if (gameField.getClass() == Forest.class) {
                    Forest forest = (Forest) gameField;
                    if (forest.isBurning()) return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfLost() {
        boolean fireBrigadeAlive = false;
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (gameBoardOfFireBreaker[i][j].getClass() == Forest.class) {
                    Forest forest = (Forest) gameBoardOfFireBreaker[i][j];
                    if (forest.somethingAliveInForest()) fireBrigadeAlive = true;
                }
            }
        }
        return !fireBrigadeAlive;
    }

    private List<Position> getNeighboursOfPosition(Position positionToGetNeighboursFrom, boolean allowCorners
            , boolean onlyForest, CardinalDirection cardinalDirection) throws SemanticsException {
        List<Position> potentialNeighbours = positionToGetNeighboursFrom
                .getAllNeighbours(cardinalDirection, allowCorners, rowLength, columnLength);
        if (onlyForest) {
            List<Position> elementsToRemove = new LinkedList<>();
            for (Position neighbour : potentialNeighbours) {
                GameField gameField = gameBoardOfFireBreaker[neighbour.getXCoordinate()][neighbour.getYCoordinate()];
                if (gameField.getClass() == Lake.class || gameField.getClass() == FireStation.class) {
                    elementsToRemove.add(neighbour);
                }
            }
            for (Position elementToRemove : elementsToRemove) {
                potentialNeighbours.remove(elementToRemove);
            }
        }
        return potentialNeighbours;
    }

    private void checkIfPositionsAreReachable(Position startPosition, Position endPosition) throws SemanticsException {
        if (startPosition.equals(endPosition)) throw new SemanticsException(Errors.POINTS_ARE_THE_SAME);
        if (!startPosition.getAllNeighbours(CardinalDirection.ALL_DIRECTIONS
                , ALLOW_CORNERS_TRUE, rowLength, columnLength).contains(endPosition)) {
            throw new SemanticsException(Errors.POINTS_ARE_NOT_REACHABLE);
        }
    }

    private void checkIfMoveIsAllowed(Position startPosition, Position endPosition) throws SemanticsException {
        Pair<Boolean, Map<Position, Position>> resultsOfPathFinding = searchPath(startPosition, endPosition);
        if (!(boolean) resultsOfPathFinding.getFirstElement())
            throw new SemanticsException(Errors.POINTS_ARE_NOT_REACHABLE);
        int lengthOfWay = getLengthOfFoundWay(resultsOfPathFinding.getSecondElement(), startPosition, endPosition);
        if (lengthOfWay > DISTANCE_OF_MOVE_ALLOWED) throw new SemanticsException(Errors.POINTS_ARE_NOT_REACHABLE);
    }

    private Pair<Boolean, Map<Position, Position>> searchPath(Position startPosition, Position endPosition)
            throws SemanticsException {
        Map<Position, Position> parentMap = new HashMap<>();
        Queue<Position> positionQueue = new ArrayDeque<>();
        Set<Position> visitedPositions = new HashSet<>();

        positionQueue.add(startPosition);
        while (true) {
            Position current = positionQueue.peek();
            if (current == null) {
                return new Pair<>(false, new HashMap<>());
            }
            if (current.equals(endPosition)) {
                return new Pair<>(true, parentMap);
            }
            current = positionQueue.poll();
            visitedPositions.add(current);
            for (Position position : getNeighboursWhichAreNotSevereBurning(current)) {
                if (!visitedPositions.contains(position)) {
                    visitedPositions.add(position);
                    positionQueue.add(position);
                    parentMap.put(position, current);
                }
            }
        }
    }

    private List<Position> getNeighboursWhichAreNotSevereBurning(Position p1) throws SemanticsException {
        List<Position> validNeighbours = getNeighboursOfPosition(p1, ALLOW_CORNERS_FALSE, ONLY_FOREST_TRUE
                , CardinalDirection.ALL_DIRECTIONS);
        List<Position> elementsToRemove = new LinkedList<>();
        for (Position neighbour : validNeighbours) {
            Forest forest = (Forest) gameBoardOfFireBreaker[neighbour.getXCoordinate()][neighbour.getYCoordinate()];
            if (!forest.getCondition().equals(Forest.ConditionOfForestSection.BIG_FIRE)) {
                continue;
            }
            elementsToRemove.add(neighbour);
        }
        for (Position elementToRemove : elementsToRemove) {
            validNeighbours.remove(elementToRemove);
        }
        return validNeighbours;
    }

    private int getLengthOfFoundWay(Map<Position, Position> parentMap, Position start, Position end) {
        Position child = end;
        Position parent = parentMap.get(end);
        int lengthOfWay = 0;
        while (!child.equals(start)) {
            Position safeParent = parent;
            parent = parentMap.get(parent);
            child = safeParent;
            lengthOfWay++;
        }
        return lengthOfWay;
    }

    private Set<Position> getPositionsWhichNeedToBeIncreased(CardinalDirection cardinalDirection)
            throws SemanticsException {
        Set<Position> positionsWhichAreSevereBurning = new HashSet<>();
        Set<Position> positionsWhichNeedsToBeIncreased = new HashSet<>();
        Set<Position> positionsWithLittleFire = new HashSet<>();
        /*
        Set of Positions which are severe Burning
         */
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (gameBoardOfFireBreaker[i][j].getClass() == Forest.class) {
                    Forest forest = (Forest) gameBoardOfFireBreaker[i][j];
                    if (forest.isSevereBurning()) {
                        positionsWhichAreSevereBurning.add(new Position(i, j));
                    }
                    if (forest.hasSmallFire()) {
                        positionsWithLittleFire.add(new Position(i, j));
                    }
                }
            }
        }
        /*
        Get the fields which need to be increased
         */
        for (Position positionWhichAreSevereBurning : positionsWhichAreSevereBurning) {
            List<Position> neighbours = getNeighboursOfPosition(positionWhichAreSevereBurning, ALLOW_CORNERS_FALSE
                    , ONLY_FOREST_TRUE, cardinalDirection);

            positionsWhichNeedsToBeIncreased.addAll(neighbours);
        }
        /*
        Adds the section which had a littleFire
         */
        positionsWhichNeedsToBeIncreased.addAll(positionsWithLittleFire);
        return positionsWhichNeedsToBeIncreased;
    }

}

