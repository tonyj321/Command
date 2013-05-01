package org.lsst.ccs.command.dictionary;

/**
 * A command dictionary contains all the information needed to provide
 * help and perform tab completion. It does not by itself provide sufficient
 * functionality to invoke a command, for this a CommandSet which is a combination of a 
 * command dictionary and a command invoker is required. A CommandDictionary is a collection of
 * CommandDefinitions, plus a few convenience methods.
 * @author tonyj
 */
public interface CommandDictionary extends Iterable<CommandDefinition> {

    /**
     * Test if a given command is present in a dictionary.
     * @param tc The command to search for
     * @return <code>true</code>If the command is found
     */
    public boolean containsCommand(TokenizedCommand tc);
    
    /**
     * Find a given command in the dictionary
     * @param tc The command to search for
     * @return The index of the command in the dictionary, or <code>-1</code> if 
     * the command is not found.
     */
    public int findCommand(TokenizedCommand tc);

    /**
     * The size of the dictionary.
     * @return The number of commands in this dictionary.
     */
    public int size();

    /**
     * Get the command at the given index.
     * @param index
     * @return The command found
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public CommandDefinition get(int index) throws IndexOutOfBoundsException;
    
}
