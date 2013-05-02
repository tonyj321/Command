package org.lsst.ccs.command;

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

    private final Dictionary dict;

    /**
     * Create a command completer for the given dictionary.
     * @param dict 
     */
    public DictionaryCompleter(Dictionary dict) {
        this.dict = dict;
    }

    /**
     * Generate a list of possible command completions. This method is coped from JLine
     * but seems to have a number of short-comings, in particular the return value seems to
     * only be used if a single item is put into the list, and thus used automatically for 
     * completion. It also gives no way to do more than just present a list of choices, for
     * example if the cursor is positioned on an argument we might want to give the user the
     * description of the parameter, rather than just a list of choices.
     * @param buffer The current content of the input line
     * @param index The index within the input line of the cursor
     * @param list A list which this routine should fill with possible command
     * completions.
     * @return The position at which the cursor should be positioned. This is only used
     * if a single item is returned in list, in which case it will automatically be applied
     * to the buffer.
     */
    public int complete(String buffer, int index, List<CharSequence> list) {
        TokenizedCommand tc = new TokenizedCommand(buffer.substring(0,index));
        if (tc.isEmpty()) {
            SortedSet<CharSequence> set = new TreeSet<>();
            for (DictionaryCommand def : dict) {
                set.add(def.getCommandName());
            }
            list.addAll(set);
        } else if (tc.getArgumentCount() == 0) {
            String prefix = tc.getCommand();
            SortedSet<CharSequence> set = new TreeSet<>();
            for (DictionaryCommand def : dict) {
                String command = def.getCommandName();
                if (command.startsWith(prefix)) {
                    // If there are more parameters to come, then add a space
                    // on the end of the line, so that the cursor will be 
                    // positioned after the space, ready for the next command.
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
