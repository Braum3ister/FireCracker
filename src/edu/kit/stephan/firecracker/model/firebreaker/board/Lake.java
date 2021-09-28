package edu.kit.stephan.firecracker.model.firebreaker.board;

/**
 * This class describes a Lake
 * @author Johannes Stephan
 * @version 1.0
 */
public class Lake extends GameField {
    /**
     * String representation of a Lake
     */
    public static final String REPRESENTATION_OF_LAKE = "L";


    @Override
    public String toString() {
        return REPRESENTATION_OF_LAKE;
    }
}
