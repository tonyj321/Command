package org.lsst.ccs.command.dictionary;

/**
 * A command dictionary contains all the information needed to provide
 * help and perform tab completion. It does not by itself provide sufficient
 * functionality to invoke a command, for this a CommandSet which is a combination of a 
 * command dictionary and a command invoker is required. A CommandDictionary is just a collection of
 * CommandDefinitions.
 * @author tonyj
 */
public interface CommandDictionary extends Iterable<CommandDefinition> {

    public boolean containsCommand(TokenizedCommand tc);
    
    public int findCommand(TokenizedCommand tc);

    public int size();

    public CommandDefinition get(int i);
    
}
