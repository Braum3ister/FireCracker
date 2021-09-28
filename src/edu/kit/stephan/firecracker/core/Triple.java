package edu.kit.stephan.firecracker.core;

import java.util.Objects;

/**
 * This class describes a Triple
 *
 * @param <S> the type parameter of first Element
 * @param <T> the type parameter of second Element
 * @param <F> the type parameter of third Element
 * @author Johannes Stephan
 * @version 1.0
 */
public class Triple<S, T, F> {

    private S firstElement;
    private T secondElement;
    private F thirdElement;

    /**
     * Instantiates a new Triple.
     *
     * @param firstElement  the first element
     * @param secondElement the second element
     * @param thirdElement  the third element
     */
    public Triple(S firstElement, T secondElement, F thirdElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
        this.thirdElement = thirdElement;
    }

    /**
     * Instantiates a new Triple.
     *
     * @param firstElement          the first element
     * @param secondAndThirdElement the second and third element
     */
    public Triple(S firstElement, Pair<T, F> secondAndThirdElement) {
        this.firstElement = firstElement;
        this.secondElement = secondAndThirdElement.getFirstElement();
        this.thirdElement = secondAndThirdElement.getSecondElement();
    }

    /**
     * Sets first element.
     *
     * @param firstElement the first element
     */
    public void setFirstElement(S firstElement) {
        this.firstElement = firstElement;
    }

    /**
     * Sets second element.
     *
     * @param secondElement the second element
     */
    public void setSecondElement(T secondElement) {
        this.secondElement = secondElement;
    }

    /**
     * Sets third element.
     *
     * @param thirdElement the third element
     */
    public void setThirdElement(F thirdElement) {
        this.thirdElement = thirdElement;
    }

    /**
     * Gets first element.
     *
     * @return the first element
     */
    public S getFirstElement() {
        return firstElement;
    }

    /**
     * Gets second element.
     *
     * @return the second element
     */
    public T getSecondElement() {
        return secondElement;
    }

    /**
     * Gets third element.
     *
     * @return the third element
     */
    public F getThirdElement() {
        return thirdElement;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(getFirstElement(), triple.getFirstElement()) && Objects.equals(getSecondElement()
                , triple.getSecondElement()) && Objects.equals(getThirdElement(), triple.getThirdElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstElement(), getSecondElement(), getThirdElement());
    }
}
