package org.lsst.ccs.command.dictionary;

/**
 *
 * @author tonyj
 */
public interface CommandSet {

    CommandDictionary getCommandDictionary();

    Object invoke(TokenizedCommand tc) throws CommandInvocationException;

    public static class CommandInvocationException extends Exception {

        CommandInvocationException(String message, Object... args) {
            super(String.format(message, args));
        }

        CommandInvocationException(String message, Throwable t) {
            super(message, t);
        }
    }
}
