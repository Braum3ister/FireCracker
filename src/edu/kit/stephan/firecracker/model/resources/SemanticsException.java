package edu.kit.stephan.firecracker.model.resources;

/**
 * An exception thrown if the input validates against the semantic rules
 * @author Johannes Stephan
 * @version 1.0
 */
public class SemanticsException extends FireCrackerException {
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 7123547557633386227L;

    /**
     * Constructs a exception with message.
     * @param message the message describing the exception
     */
    public SemanticsException(String message) {
        super(message);
    }
}
