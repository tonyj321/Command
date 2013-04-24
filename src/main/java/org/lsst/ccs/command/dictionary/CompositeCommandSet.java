package org.lsst.ccs.command.dictionary;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author tonyj
 */
public class CompositeCommandSet implements CommandSet {
    private Set<CommandSet> commands = new LinkedHashSet<>();
    private CompositeCommandDictionary dict = new CompositeCommandDictionary(); 
    
    public void add(CommandSet set) {
        commands.add(set);
        // TODO: Check for ambigous commands and if found warn
        dict.add(set.getCommandDictionary());
    }
    public void remove(CommandSet set) {
        commands.remove(set);
        dict.remove(set.getCommandDictionary());
    }
    
    public Set<CommandSet> getCommandSets() {
        return Collections.unmodifiableSet(commands);
    }

    @Override
    public CommandDictionary getCommandDictionary() {
        return dict;
    }

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
