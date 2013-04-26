package org.lsst.ccs.command.dictionary;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Performs command completion, based on the information available in the command
 * dictionary. THis is designed to be jline compatible, but not actually implement
 * jline's command completer to avoid adding a permanent dependency on jline.
 * @author tonyj
 */
public class DictionaryCompleter {

    private final CommandDictionary dict;

    public DictionaryCompleter(CommandDictionary dict) {
        this.dict = dict;
    }

    public int complete(String buffer, int index, List<CharSequence> list) {
        TokenizedCommand tc = new TokenizedCommand(buffer.substring(0,index));
        if (tc.isEmpty()) {
            SortedSet<CharSequence> set = new TreeSet<>();
            for (CommandDefinition def : dict) {
                set.add(def.getCommandName());
            }
            list.addAll(set);
        } else if (tc.getArgumentCount() == 0) {
            String prefix = tc.getCommand();
            SortedSet<CharSequence> set = new TreeSet<>();
            for (CommandDefinition def : dict) {
                String command = def.getCommandName();
                if (command.startsWith(prefix)) {
                    if (def.getParams().length>0) {
                        command = command + " ";
                    }
                    set.add(command);
                }
            }
            list.addAll(set);
            return 0;
        }
        return index;
    }
}
