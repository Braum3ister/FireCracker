package edu.kit.stephan.firecracker.model.firebreaker.board;

import edu.kit.stephan.firecracker.model.firebreaker.FireBrigade;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;
import edu.kit.stephan.firecracker.core.Pair;
import edu.kit.stephan.firecracker.model.resources.SyntaxException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class models a Forest.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class Forest extends GameField {

    private static final String TO_STRING_SEPARATOR = ",";
    private static final String REPRESENTATION_FOR_NOT_BURNING = "x";
    private final Set<FireBrigade> fireBrigades;
    private ConditionOfForestSection condition;

    /**
     * Instantiates a new Forest with default Condition.
     */
    public Forest() {
        this(ConditionOfForestSection.DRY);
    }

    /**
     * Instantiates a new Forest.
     *
     * @param condition the condition of the Forest.
     */
    public Forest(ConditionOfForestSection condition) {
        this.condition = condition;
        this.fireBrigades = new TreeSet<>();
    }


    /**
     * Gets condition.
     *
     * @return the condition
     */
    public ConditionOfForestSection getCondition() {
        return condition;
    }


    /**
     * Method to return the String representation of the forest.
     *
     * @return "x" -> if the forest is not burning
     * the string-representation of the forest piece -> if its burning.
     */
    public String getBurning() {
        if (isBurning()) return condition.getRepresentationAsString();
        return REPRESENTATION_FOR_NOT_BURNING;
    }

    /**
     * Method to return if a forest is burning.
     *
     * @return true-> if the forest is burning
     * false-> if the forest is not burning.
     */
    public boolean isBurning() {
        return condition.equals(ConditionOfForestSection.SMALL_FIRE)
                || condition.equals(ConditionOfForestSection.BIG_FIRE);

    }

    /**
     * Method to return if a forest is severe burning.
     *
     * @return true -> if the forest is severe burning.
     * false -> if the forest is not severe burning.
     */
    public boolean isSevereBurning() {
        return condition.equals(ConditionOfForestSection.BIG_FIRE);
    }

    /**
     * Method to return if a forest is a little bit burning.
     *
     * @return true -> if the forest is small burning.
     * false -> if the forest is not small burning.
     */
    public boolean hasSmallFire() {
        return condition.equals(ConditionOfForestSection.SMALL_FIRE);
    }

    /**
     * Add fire brigade to forest.
     *
     * @param fireBrigade the fire brigade which should be added
     * @throws SemanticsException if the Forest has a Big Fire.
     */
    public void addFireBrigade(FireBrigade fireBrigade) throws SemanticsException {
        if (isBurning()) throw new SemanticsException(Errors.CANT_PLACE_FIRE_BRIGADE);
        fireBrigades.add(fireBrigade);
    }


    /**
     * Delete fire brigade out of Forest.
     *
     * @param fireBrigade the fire brigade which should be removed.
     */
    public void deleteFireBrigade(FireBrigade fireBrigade) {
        fireBrigades.remove(fireBrigade);
    }

    /**
     * Extinguish Command performed on Forest.
     *
     * @return a Pair: firstElement(boolean) -> true if player earned a point.
     * secondElement(String) -> the new representation of the forest as String
     * @throws SemanticsException if the field is already wet.
     */
    public Pair<Boolean, String> extinguishFire() throws SemanticsException {
        if (condition.equals(ConditionOfForestSection.DRY)) {
            condition = condition.extinguishFire();
            return new Pair<>(false, condition.getRepresentationAsString());
        } else {
            condition = condition.extinguishFire();
            return new Pair<>(true, condition.getRepresentationAsString());
        }
    }


    /**
     * Increase burning.
     */
    public void increaseBurning() {
        condition = condition.increaseFire();
        if (condition.equals(ConditionOfForestSection.BIG_FIRE)) clearBrigades();
    }

    /**
     * Method to determine when is something is alive in forest
     *
     * @return the result as a boolean.
     */
    public boolean somethingAliveInForest() {
        return !fireBrigades.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(condition.getRepresentationAsString());
        if (fireBrigades.isEmpty()) return output.toString();

        for (FireBrigade fireBrigade : fireBrigades) {
            output.append(TO_STRING_SEPARATOR).append(fireBrigade.getUniqueIdentifier());
        }
        return output.toString();
    }


    private void clearBrigades() {
        fireBrigades.forEach(FireBrigade::setBurning);
        fireBrigades.clear();
    }

    /**
     * This enum models a condition of a Forest.
     * @see Forest
     */
    public enum ConditionOfForestSection {

        /**
         * Dry condition of forest section.
         */
        DRY("d") {
            @Override
            public ConditionOfForestSection increaseFire() {
                return SMALL_FIRE;
            }

            @Override
            public ConditionOfForestSection extinguishFire() {
                return WET;
            }
        },
        /**
         * Wet Condition of forest section.
         */
        WET("w") {
            @Override
            public ConditionOfForestSection increaseFire() {
                return DRY;
            }

            @Override
            public ConditionOfForestSection extinguishFire() throws SemanticsException {
                throw new SemanticsException(Errors.FOREST_IS_ALREADY_WET);
            }
        },
        /**
         * Small fire condition of forest section.
         */
        SMALL_FIRE("+") {
            @Override
            public ConditionOfForestSection increaseFire() {
                return BIG_FIRE;
            }

            @Override
            public ConditionOfForestSection extinguishFire() {
                return WET;
            }
        },
        /**
         * Big fire condition of forest section.
         */
        BIG_FIRE("*") {
            @Override
            public ConditionOfForestSection increaseFire() {
                return BIG_FIRE;
            }

            @Override
            public ConditionOfForestSection extinguishFire() {
                return SMALL_FIRE;
            }
        };

        private final String representationAsString;

        /**
         * Constructor of a Forest Condition
         * @param representationAsString representation which represents the condition
         */
        ConditionOfForestSection(String representationAsString) {
            this.representationAsString = representationAsString;
        }

        /**
         * Method to return the Condition, if a forest-fire would be increased.
         * @return the increased Fire Condition.
         */
        public abstract ConditionOfForestSection increaseFire();

        /**
         * Method to return the Condition, if a Forest would be extinguished.
         * @return the extinguished condition.
         */
        public abstract ConditionOfForestSection extinguishFire() throws SemanticsException;


        /**
         * Gets representation.
         *
         * @return the representation
         */
        public String getRepresentationAsString() {
            return representationAsString;
        }

        /**
         * Find condition of forest section by String.
         *
         * @param conditionOfForest the condition of forest
         * @return the condition of forest section
         * @throws SyntaxException if there is no corresponding section to the inputted string.
         */
        public static ConditionOfForestSection findConditionOfForestSection(String conditionOfForest)
                throws SyntaxException {
            return Arrays
                    .stream(ConditionOfForestSection.values())
                    .filter(condition -> condition.getRepresentationAsString().equals(conditionOfForest))
                    .findFirst()
                    .orElseThrow(() -> new SyntaxException(Errors.NOT_IMPLEMENTED));
        }
    }
}
