package org.lsst.ccs.command.dictionary;

import java.lang.reflect.InvocationTargetException;

/**
 * A CommandSet is a combination of a CommandDictionary plus the ability to
 * invoke commands. 
 * @author tonyj
 */
public interface CommandSet {

    CommandDictionary getCommandDictionary();

    Object invoke(TokenizedCommand tc) throws CommandInvocationException;

    public static class CommandInvocationException extends Exception {

        public CommandInvocationException(String message, Object... args) {
            super(String.format(message, args));
        }

        public CommandInvocationException(String message, Throwable t) {
            super(message, t);
        }
        
        public CommandInvocationException(InvocationTargetException t) {
            super(t.getTargetException().getMessage(),t.getTargetException());
        }
    }
}
