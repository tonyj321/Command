package org.lsst.ccs.command;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Performs command completion, based on the information available in the
 * command dictionary. This is designed to be jline compatible, but not actually
 * implement jline's command completer to avoid adding a permanent dependency on
 * jline.
 *
 * @author tonyj
 */
public class DictionaryCompleter {

    private final Dictionary dict;

    /**
     * Create a command completer for the given dictionary.
     *
     * @param dict
     */
    public DictionaryCompleter(Dictionary dict) {
        this.dict = dict;
    }

    /**
     * Generate a list of possible command completions. This method is coped
     * from JLine but seems to have a number of short-comings, in particular the
     * return value seems to only be used if a single item is put into the list,
     * and thus used automatically for completion. It also gives no way to do
     * more than just present a list of choices, for example if the cursor is
     * positioned on an argument we might want to give the user the description
     * of the parameter, rather than just a list of choices.
     *
     * @param buffer The current content of the input line
     * @param index The index within the input line of the cursor
     * @param list A list which this routine should fill with possible command
     * completions.
     * @return The position at which the cursor should be positioned. This is
     * only used if a single item is returned in list, in which case it will
     * automatically be applied to the buffer.
     */
    public int complete(String buffer, int index, List<CharSequence> list) {
        TokenizedCommand tc = new TokenizedCommand(buffer.substring(0, index));
        int argCount = tc.getArgumentCount();
        boolean endsWithWhiteSpace = !tc.isEmpty() && Character.isWhitespace(buffer.charAt(index-1));
        if (endsWithWhiteSpace) {
            argCount++;
        }
        if (tc.isEmpty()) {
            SortedSet<CharSequence> set = new TreeSet<>();
            for (DictionaryCommand def : dict) {
                set.add(def.getCommandName());
            }
            list.addAll(set);
        } else if (argCount == 0) {
            //FIXME: We should not go here if the line ends with space
            String prefix = tc.getCommand();
            SortedSet<CharSequence> set = new TreeSet<>();
            for (DictionaryCommand def : dict) {
                String command = def.getCommandName();
                if (command.startsWith(prefix)) {
                    // If there are more parameters to come, then add a space
                    // on the end of the line, so that the cursor will be 
                    // positioned after the space, ready for the next command.
                    if (def.getParams().length > 0) {
                        command = command + " ";
                    }
                    set.add(command);
                }
            }
            list.addAll(set);
            return 0;
        } else {
            String command = tc.getCommand();
            String lastArg = endsWithWhiteSpace ? "" : tc.getArgument(argCount - 1);
            for (DictionaryCommand def : dict) {
                if (command.equals(def.getCommandName()) && def.getParams().length >= argCount) {
                    List<String> values = def.getParams()[argCount - 1].getValues();
                    if (!values.isEmpty()) {
                        for (String value : values) {
                            if (value.startsWith(lastArg)) {
                                if (def.getParams().length > argCount) {
                                    value = value + " ";
                                }
                                list.add(value);
                            }
                        }
                    }
                }
            }
            return endsWithWhiteSpace ? index : tc.getArgumentIndex(argCount - 1);
        }
        return index;
    }
}
