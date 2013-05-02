package org.lsst.ccs.command;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class which can combine multiple command sets to form one combined command set.
 * Individual command sets can be dynamically added and removed. Command sets can
 * be nested, in that one CompositeCommandSet may include another.
 * @author tonyj
 */
public class CompositeCommandSet implements CommandSet {
    private Set<CommandSet> commands = new LinkedHashSet<>();
    private CompositeCommandDictionary dict = new CompositeCommandDictionary(); 
    
    /**
     * Add a new command set
     * @param set The command set to add.
     */
    public void add(CommandSet set) {
        commands.add(set);
        // TODO: Check for ambigous commands and if found warn
        dict.add(set.getCommandDictionary());
    }
    /**
     * Remove a command set
     * @param set The command set to be removed.
     */
    public void remove(CommandSet set) {
        commands.remove(set);
        dict.remove(set.getCommandDictionary());
    }
    /**
     * Returns the list of command sets. 
     * @return An unmodifiable collection of command sets 
     */
    public Set<CommandSet> getCommandSets() {
        return Collections.unmodifiableSet(commands);
    }

    /**
     * Get the dictionary associated with this command set. If there are
     * multiple command sets included in this composite command set the 
     * dictionary will include entries for all of the commands in all of the
     * included command sets.
     * @return 
     */
    @Override
    public Dictionary getCommandDictionary() {
        return dict;
    }

    /**
     * Invoke a command included in this command set.
     * @param tc The tokenized command to be invoke
     * @return The result of executing the command, or <code>null</code> if
     * the command does not return a result.
     * @throws CommandInvocationException If the command fails, either because it cannot
     * be found, the arguments do not match the expected arguments, or the command fails 
     * during invocation.
     */
    @Override
    public Object invoke(TokenizedCommand tc) throws CommandInvocationException {
        for (CommandSet set : commands) {
            if (set.getCommandDictionary().containsCommand(tc)) {
                return set.invoke(tc);
            }
        }
        throw new CommandInvocationException("No handler found for command %s with %d arguments",tc.getCommand(),tc.getArgumentCount());
    }
}
