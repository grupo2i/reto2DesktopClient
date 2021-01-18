package reto2desktopclient.exceptions;

import java.io.Serializable;

/**
 * Thrown to indicate that an unexpected error occured during runtime.
 *
 * @author Aitor Fidalgo
 */
public class UnexpectedErrorException extends Exception implements Serializable {

    /**
     * Default message for the exception.
     */
    private static final String DEFAULT_MESSAGE
            = "An unexpected error occurred, please try later.";

    /**
     * Constructs an UnexpectedErrorException.
     */
    public UnexpectedErrorException() {
        
    }
    
    /**
     * Constructs an UnexpectedErrorException with the {@link #DEFAULT_MESSAGE}
     * and the specified cause.
     *
     * @param cause The specified cause.
     */
    public UnexpectedErrorException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructs an UnexpectedErrorException with the specified message.
     *
     * @param message The specified message.
     */
    public UnexpectedErrorException(String message) {
        super(message);
    }

    /**
     * Constructs an UnexpectedErrorException with the specified message and
     * cause.
     *
     * @param message The specified message.
     * @param cause The specified cause.
     */
    public UnexpectedErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
