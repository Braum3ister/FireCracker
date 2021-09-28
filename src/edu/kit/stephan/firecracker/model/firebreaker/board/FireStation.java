package edu.kit.stephan.firecracker.model.firebreaker.board;

/**
 * This class models a FireStation.
 * @author Johannes Stephan
 * @version 1.0
 */
public class FireStation extends GameField {
    private final String ownerOfFireStation;

    /**
     * Instantiates a new Fire station.
     *
     * @param ownerOfFireStation the owner of fire station
     */
    public FireStation(String ownerOfFireStation) {
        this.ownerOfFireStation = ownerOfFireStation;
    }

    @Override
    public String toString() {
        return ownerOfFireStation;
    }
}
