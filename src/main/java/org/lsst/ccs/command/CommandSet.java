package org.lsst.ccs.command;

/**
 * A CommandSet is a combination of a Dictionary plus the ability to
 * invoke a tokenized command. Note that unlike a Dictionary, a CommandSet
 * is not serializable. To make commands accessible remotely you need to wrap the 
 * CommandSet using an implementation of {@link org.lsst.ccs.command.remote.CommandServer}.
 * @author tonyj
 */
public interface CommandSet {

    Dictionary getCommandDictionary();

    Object invoke(TokenizedCommand tc) throws CommandInvocationException;
}
