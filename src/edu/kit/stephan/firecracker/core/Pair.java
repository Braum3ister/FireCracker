/*
 * Copyright (c) 2021, KASTEL. All rights reserved.
 */

package edu.kit.stephan.firecracker.core;

import java.util.Objects;

/**
 * This class describes a pair.
 *
 * @author Lucas Alber
 * @author Johannes Stephan
 * @version 1.0
 *
 * @param <S> type of the first element
 * @param <T> type of the second element
 */
public class Pair<S, T> implements Comparable<Pair<S, T>> {
    private S firstElement;
    private T secondElement;

    /**
     * Constructs a new pair.
     *
     * @param firstElement the first element
     * @param secondElement the second element
     */
    public Pair(S firstElement, T secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }



    /**
     * Returns the first element
     *
     * @return the first element
     */
    public S getFirstElement() {
        return firstElement;
    }

    /**
     * Returns the second element
     *
     * @return the second element
     */
    public T getSecondElement() {
        return secondElement;
    }

    /**
     * Sets the first element
     * @param firstElement the new firstElement
     */
    public void setFirstElement(S firstElement) {
        this.firstElement = firstElement;
    }

    /**
     * Sets the second element
     * @param secondElement the new secondElement
     */
    public void setSecondElement(T secondElement) {
        this.secondElement = secondElement;
    }

    /**
     * Compare Method
     * @param o the value to compare
     * @return the object compared with the String Value of the firstElement
     */
    @Override
    public int compareTo(Pair<S, T> o) {
        return this.firstElement.toString().compareTo(o.firstElement.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getFirstElement()
                , pair.getFirstElement()) && Objects.equals(getSecondElement(), pair.getSecondElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstElement(), getSecondElement());
    }
}
