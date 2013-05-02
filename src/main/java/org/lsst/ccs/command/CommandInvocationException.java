package org.lsst.ccs.command;

import java.lang.reflect.InvocationTargetException;

/**
 * An exception thrown when a command invocation fails, either because the
 * command cannot be invoked, or because it was invoked and generated an exception.
 * @author tonyj
 */
public class CommandInvocationException extends Exception {

    public CommandInvocationException(String message, Throwable t, Object... args) {
        super(String.format(message, args),t);
    }
    
    public CommandInvocationException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CommandInvocationException(String message, Throwable t) {
        super(message, t);
    }

    public CommandInvocationException(InvocationTargetException t) {
        super(t.getTargetException().getMessage(), t.getTargetException());
    }
    
}
