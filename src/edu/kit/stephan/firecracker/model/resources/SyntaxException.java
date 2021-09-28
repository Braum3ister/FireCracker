package edu.kit.stephan.firecracker.model.resources;

/**
 * An exception thrown if Syntax is wrong/Parsing Failed
 * @author Johannes Stephan
 * @version 1.0
 */
public final class SyntaxException extends FireCrackerException {
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 8533547557133386227L;

    /**
     * Constructs a exception with message.
     * @param message the message describing the exception
     */
    public SyntaxException(String message) {
        super(message);
    }
}
