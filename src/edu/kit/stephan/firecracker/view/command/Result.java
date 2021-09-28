/*
 * Copyright (c) 2021, KASTEL. All rights reserved.
 */

package edu.kit.stephan.firecracker.view.command;

/**
 * This class describes a result of a command execution.
 *
 * @author Lucas Alber
 * @author Johannes Stephan
 * @version 1.0
 */
public class Result {

    private final ResultType type;
    private final String message;

    /**
     * Constructs a new Result without message.
     *
     * @param type the type of the result.
     */
    public Result(final ResultType type) {
        this(type, null);
    }

    /**
     * Constructs a new Result with message.
     *
     * @param type    the type of the result.
     * @param message message to carry
     */
    public Result(final ResultType type, final String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Returns the type of result.
     *
     * @return the type of result.
     */
    public ResultType getType() {
        return this.type;
    }

    /**
     * Returns the carried message of the result or {@code null} if there is none.
     *
     * @return the message or {@code null}
     */
    public String getMessage() {
        return this.message;
    }


    /**
     * The type of Result of a execution.
     */
    public enum ResultType {
        /**
         * The execution did not end with success.
         */
        FAILURE,
        /**
         * The execution did end with success.
         */
        SUCCESS
    }

}