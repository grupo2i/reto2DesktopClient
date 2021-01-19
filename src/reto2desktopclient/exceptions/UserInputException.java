/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ander
 */
public class UserInputException extends Exception{
   /* public UserInputException(String username){
        super(username + "' is already registered.");
        Logger.getLogger(UnexpectedErrorException.class.getName()).log(Level.SEVERE, this.getMessage());
    }*/
        private static final String DEFAULT_MESSAGE
            = "An unexpected error occured, please try later.";

    /**
     * Constructs an UserInputException with the {@link #DEFAULT_MESSAGE}
     * and the specified cause.
     *
     * @param cause The specified cause.
     */
    public UserInputException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * Constructs an UserInputException with the specified message.
     *
     * @param message The specified message.
     */
    public UserInputException(String message) {
        super(message);
    }

    /**
     * Constructs an UserInputException with the specified message and
     * cause.
     *
     * @param message The specified message.
     * @param cause The specified cause.
     */
    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
